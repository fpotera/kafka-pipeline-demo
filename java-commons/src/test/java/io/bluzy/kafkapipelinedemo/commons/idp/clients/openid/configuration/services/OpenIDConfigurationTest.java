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
import io.bluzy.kafkapipelinedemo.commons.configuration.ServiceConfigurationPropertiesImpl;
import io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.authentication.services.M2MTokenService;
import io.bluzy.kafkapipelinedemo.commons.idp.clients.web.services.WebClientBuilderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class OpenIDConfigurationTest {
    @Autowired
    M2MTokenService m2mTokenService;

    @Test
    public void test() {
        AtomicReference<String> m2mToken = new AtomicReference<>(null);
        assertDoesNotThrow(()->{
            m2mToken.set(m2mTokenService.getM2MToken());
        });
        String token = m2mToken.get();
        assertNotNull(token);
        out.println(token);
    }
}


@SpringBootConfiguration
@EnableConfigurationProperties(ServiceConfigurationPropertiesImpl.class)
class Configuration {
    @Bean
    public OpenIDConfigurationService buildOpenIDConfigurationService() {
        return new OpenIDConfigurationService(buildServiceConfigurationProperties(),
                buildWebClientBuilderService());
    }

    @Bean
    public ServiceConfigurationProperties buildServiceConfigurationProperties() {
        return new ServiceConfigurationPropertiesImpl();
    }

    @Bean
    public M2MTokenService buildM2MTokenService() {
        return new M2MTokenService(buildServiceConfigurationProperties(), buildOpenIDConfigurationService(),
                buildWebClientBuilderService());
    }

    @Bean
    public WebClientBuilderService buildWebClientBuilderService() {
        return new WebClientBuilderService();
    }
}
