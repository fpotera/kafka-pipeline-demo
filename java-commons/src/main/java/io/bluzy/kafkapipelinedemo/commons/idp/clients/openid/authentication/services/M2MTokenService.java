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

import com.nimbusds.jwt.SignedJWT;
import io.bluzy.kafkapipelinedemo.commons.configuration.ServiceConfigurationProperties;
import io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.configuration.model.OpenIDConfiguration;
import io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.configuration.model.TokenResponse;
import io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.configuration.services.OpenIDConfigurationService;
import io.bluzy.kafkapipelinedemo.commons.idp.clients.web.services.WebClientBuilderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Optional;

import static io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.util.Utils.jwtToString;
import static io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.util.Utils.maskToken;
import static io.bluzy.kafkapipelinedemo.commons.logging.Markers.SECURITY;
import static java.util.Objects.isNull;

@Slf4j
@Service
public class M2MTokenService {

    private final MessageFormat entryFormat = new MessageFormat("entry[getM2MToken] params[forcedRefresh={0}]");
    private final MessageFormat exitFormat = new MessageFormat("exit[getM2MToken] return[{0}]");

    private ServiceConfigurationProperties serviceProperties;
    private OpenIDConfiguration idPConfiguration;
    private OpenIDConfigurationService idPConfigurationService;

    private boolean hasExpired = true;
    private String cachedToken;
    private Long expiresIn;

    private WebClient webClient;

    public M2MTokenService(ServiceConfigurationProperties serviceProperties,
                           OpenIDConfigurationService idPConfigurationService,
                           WebClientBuilderService webClientBuilderService) {
        this.serviceProperties = serviceProperties;
        this.idPConfigurationService = idPConfigurationService;

        webClient = webClientBuilderService.buildBaseWebClient()
                .build();
    }

    public String getM2MToken() throws ParseException {
        return getM2MToken(false);
    }

    public String getM2MToken(boolean forcedRefresh) throws java.text.ParseException {
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

            Optional<TokenResponse> tokenResponse = buildRequest(idPConfiguration.getTokenEndpoint()).retrieve()
                    .bodyToMono(TokenResponse.class)
                    .blockOptional();

            if (tokenResponse.isPresent()) {
                log.debug("Received M2M token successfully and started parsing...");
                jwt = tokenResponse.get().getAccessToken();

                expiresIn = SignedJWT.parse(jwt).getJWTClaimsSet().getExpirationTime().getTime() / 1000L;

                log.debug("M2M token parsed successfully with expiration time  -> " + expiresIn.toString());
                cachedToken = jwt;
            } else {
                throw new RuntimeException("Unknown error.");
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

    private WebClient.RequestHeadersSpec<?> buildRequest(String idPTokenUrl) {
        MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();

        bodyValues.add("grant_type", "client_credentials");
        bodyValues.add("client_id", serviceProperties.getClientId());
        bodyValues.add("client_secret", serviceProperties.getClientSecret());

        log.debug("Created RequestBody...");
        log.debug("grant_type -> " + "client_credentials");
        log.debug("client_id -> " + serviceProperties.getClientId());

        WebClient.RequestHeadersSpec<?> request = webClient.post()
                .uri(idPTokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(bodyValues));

        log.debug("Created Request...");
        log.debug("Url -> " + idPTokenUrl);

        return request;
    }
}
