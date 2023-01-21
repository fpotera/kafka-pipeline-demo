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
package io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.configuration.services;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bluzy.kafkapipelinedemo.commons.configuration.ServiceConfigurationProperties;
import io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.configuration.model.OpenIDConfiguration;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class OpenIDConfigurationService {

    private ObjectMapper mapper = new ObjectMapper();

    private OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).build();

    private String idPConfigurationEndpointUrl;

    public OpenIDConfigurationService(ServiceConfigurationProperties serviceProperties) {
        this.idPConfigurationEndpointUrl = serviceProperties.getIdPConfigurationEndpointUrl();
    }

    public Optional<OpenIDConfiguration> getIdPConfiguration() {
        Optional<OpenIDConfiguration> openIDConfiguration = Optional.empty();
        try {
            Response response = httpClient.newCall(new Request.Builder().get().url(idPConfigurationEndpointUrl).build()).execute();
            try(response) {
                if (response.code() == 200 && nonNull(response.body())) {
                    log.debug("Received successful response from configEndpointUrl.");
                    String jsonString = response.body().string();
                    openIDConfiguration = Optional.of(mapper.readValue(jsonString, OpenIDConfiguration.class));
                }
            }
        } catch (IOException e) {
            log.error("Error in accessing configEndpointUrl: {} with error: {}", idPConfigurationEndpointUrl, e.getMessage());
            throw new RuntimeException("IdP configuration endpoint not reachable.", e);
        }
        return openIDConfiguration;
    }
}
