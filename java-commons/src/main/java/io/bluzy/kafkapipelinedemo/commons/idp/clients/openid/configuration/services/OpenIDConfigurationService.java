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

import io.bluzy.kafkapipelinedemo.commons.configuration.ServiceConfigurationProperties;
import io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.configuration.model.OpenIDConfiguration;
import io.bluzy.kafkapipelinedemo.commons.idp.clients.web.services.WebClientBuilderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Slf4j
@Service
public class OpenIDConfigurationService {

    private WebClient webClient;

    public OpenIDConfigurationService(ServiceConfigurationProperties serviceProperties,
                                      WebClientBuilderService webClientBuilderService) {

        webClient = webClientBuilderService.buildBaseWebClient()
                .mutate()
                .baseUrl(serviceProperties.getIdPConfigurationEndpointUrl())
                .build();
    }

    public Optional<OpenIDConfiguration> getIdPConfiguration() {
        Optional<OpenIDConfiguration> openIDConfiguration = Optional.empty();

        return webClient.get()
                .retrieve()
                .bodyToMono(OpenIDConfiguration.class)
                .blockOptional();
    }
}
