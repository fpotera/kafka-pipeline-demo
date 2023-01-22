/**  Copyright 2023 Florin Potera
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.authentication.services;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.nimbusds.jwt.SignedJWT;
import io.bluzy.kafkapipelinedemo.commons.configuration.ServiceConfigurationProperties;
import io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.configuration.model.OpenIDConfiguration;
import io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.configuration.services.OpenIDConfigurationService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import static io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.util.Utils.jwtToString;
import static io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.util.Utils.maskToken;
import static io.bluzy.kafkapipelinedemo.commons.logging.Markers.SECURITY;
import static java.util.Objects.isNull;
import static net.minidev.json.parser.JSONParser.ACCEPT_SIMPLE_QUOTE;
import static net.minidev.json.parser.JSONParser.MODE_JSON_SIMPLE;


@Slf4j
@Service
public class M2MTokenService {
    private MessageFormat entryFormat = new MessageFormat("entry[getM2MToken] params[forcedRefresh={0}]");
    private MessageFormat exitFormat = new MessageFormat("exit[getM2MToken] return[{0}]");

    private ServiceConfigurationProperties serviceProperties;
    private OpenIDConfiguration idPConfiguration;
    private OpenIDConfigurationService idPConfigurationService;
    private OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).build();
    private boolean hasExpired = true;
    private String cachedToken;
    private Long expiresIn;

    public M2MTokenService(ServiceConfigurationProperties serviceProperties, OpenIDConfigurationService idPConfigurationService) {
        this.serviceProperties = serviceProperties;
        this.idPConfigurationService = idPConfigurationService;
    }

    public String getM2MToken() {
        return getM2MToken(false);
    }

    public String getM2MToken(boolean forcedRefresh) {
        if(isNull(idPConfiguration)) {
            Optional<OpenIDConfiguration> openIDConfiguration = idPConfigurationService.getIdPConfiguration();
            if(openIDConfiguration.isEmpty()) {
                throw new RuntimeException("Failed to get the IdP configuration.");
            }
            idPConfiguration = openIDConfiguration.get();
        }
        String clientIdSuffix = " with reqId[" + serviceProperties.getClientId() + "]";
        log.trace(entryFormat.format(new Object[]{forcedRefresh}) + clientIdSuffix);
        String jwt = null;

        if (cachedToken != null) {
            Long timestamp = System.currentTimeMillis() / 1000L;
            validateToken(timestamp, expiresIn);
        }

        if (!hasExpired && !forcedRefresh) {
            jwt = cachedToken;
            log.trace("Cached token is valid ...");
        } else {
            log.debug("Executing getM2MToken request" + clientIdSuffix);
            try (Response response = client.newCall(buildRequest(idPConfiguration.getTokenEndpoint())).execute()) {

                String responseBody = response.body() != null ? response.body().string() : null;
                if (responseBody != null) {
                    JSONObject json = (JSONObject) new JSONParser(MODE_JSON_SIMPLE | ACCEPT_SIMPLE_QUOTE).parse(responseBody);

                    if (response.code() == 200) {
                        log.debug("Received M2M token successfully and started parsing...");
                        jwt = json.getAsString("access_token");

                        expiresIn = SignedJWT.parse(jwt).getJWTClaimsSet().getExpirationTime().getTime() / 1000L;

                        log.debug("M2M token parsed successfully with expiration time  -> " + expiresIn.toString());
                        cachedToken = jwt;

                    } else {
                        String errorMsg = json.getAsString("error_description");

                        if (response.code() == 401) {
                            log.error(SECURITY, "IdP.getM2MToken failed -> HTTP RESPONSE CODE [401] -> {}. clientId: {}", errorMsg, serviceProperties.getClientId());
                            throw new RuntimeException("Client is not authorized. " + errorMsg);
                        }
                        log.error(SECURITY, "IdP.getM2MToken failed -> HTTP RESPONSE CODE [{}] -> {}. clientId: {}", response.code(), errorMsg, serviceProperties.getClientId());
                        throw new RuntimeException("Unknown error. " + errorMsg);
                    }
                } else {
                    log.error(SECURITY,"IdP.getM2MToken failed -> HTTP RESPONSE CODE [" + response.code() + "] -> " + "Internal Error with empty response. clientId: {}", serviceProperties.getClientId());
                    throw new RuntimeException("IdP not reachable. " + "Internal Error with empty response");
                }
            } catch (ParseException | java.text.ParseException ex) {
                log.error(SECURITY, "IdP.getM2MToken Error in parsing IdP token: {} clientId: {}", ex.getMessage(), serviceProperties.getClientId());
                throw new RuntimeException("Wrong token. " + "failed to get token");
            } catch (IOException e) {
                log.error(SECURITY, "IdP.getM2MToken Failed to connect IdP: {} clientId: {}", e.getMessage(), serviceProperties.getClientId());
                throw new RuntimeException("IdP not reachable. " + "failed to connect IdP");
            }
        }
        log.info(SECURITY, "IdP.getM2MToken success with clientId: {} M2MToken: [{}]", serviceProperties.getClientId(), jwtToString(jwt));
        log.trace(exitFormat.format(new Object[]{maskToken(jwt)}) + clientIdSuffix);
        return jwt;
    }

    public ServiceConfigurationProperties getServiceProperties() {
        return serviceProperties;
    }

    public String getCachedToken() {
        return cachedToken;
    }

    public boolean isExpired() {
        return hasExpired;
    }

    public void validateToken(Long timestamp, Long expiresIn) {
        hasExpired = timestamp > expiresIn;
    }

    private Request buildRequest(String idPTokenUrl) {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", serviceProperties.getClientId())
                .add("client_secret", serviceProperties.getClientSecret())
                .build();

        log.debug("Created RequestBody...");
        log.debug("grant_type -> " + "client_credentials");
        log.debug("client_id -> " + serviceProperties.getClientId());

        Request request = new Request.Builder()
                .url(idPTokenUrl)
                .post(body)
                .build();

        log.debug("Created Request...");
        log.debug("Url -> " + idPTokenUrl);

        return request;
    }
}
