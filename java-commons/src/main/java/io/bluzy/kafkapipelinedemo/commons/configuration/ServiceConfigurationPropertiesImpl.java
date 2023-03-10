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
package io.bluzy.kafkapipelinedemo.commons.configuration;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.springframework.util.StringUtils.hasLength;


@Data
@Configuration
@ConfigurationProperties(prefix = "idp")
public class ServiceConfigurationPropertiesImpl implements ServiceConfigurationProperties {
    private String clientId;
    private String clientSecret;
    private String idPConfigurationEndpointUrl;
    private String eventsSourceUrl;

    @ToString.Include(name = "clientSecret")
    private String clientSecretMasker() {
        return !hasLength(clientSecret) ? ""
                : clientSecret.substring(0, 3) + "******" + clientSecret.substring(clientSecret.length() - 3); }
}
