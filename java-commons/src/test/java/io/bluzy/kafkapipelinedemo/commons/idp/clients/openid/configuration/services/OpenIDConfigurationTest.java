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
import io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.configuration.model.OpenIDConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class OpenIDConfigurationTest {
    @Autowired
    OpenIDConfigurationService openIDConfigurationService;

    @Test
    public void test() {
        AtomicReference<Optional<OpenIDConfiguration>> openIDConfiguration = new AtomicReference<>(Optional.empty());
        assertDoesNotThrow(()->{
            openIDConfiguration.set(openIDConfigurationService.getIdPConfiguration());
        });

        assertTrue(openIDConfiguration.get().isPresent());
    }
}


@SpringBootConfiguration
@EnableConfigurationProperties(ServiceConfigurationPropertiesImpl.class)
class Configuration {
    @Bean
    public OpenIDConfigurationService buildOpenIDConfigurationService() {
        return new OpenIDConfigurationService(buildServiceConfigurationProperties());
    }

    @Bean
    public ServiceConfigurationProperties buildServiceConfigurationProperties() {
        return new ServiceConfigurationPropertiesImpl();
    }
}
