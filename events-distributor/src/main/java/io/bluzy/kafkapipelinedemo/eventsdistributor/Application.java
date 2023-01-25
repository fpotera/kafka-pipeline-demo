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
package io.bluzy.kafkapipelinedemo.eventsdistributor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.context.request.RequestContextListener;
import static org.springframework.util.ClassUtils.getUserClass;

@SpringBootApplication(exclude = { LdapAutoConfiguration.class })
@EnableIntegration
@ComponentScan({"io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.authentication.services",
                "io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.configuration.services"})
@Import({})
@Slf4j
@RequiredArgsConstructor
public class Application extends SpringBootServletInitializer {

    @SuppressWarnings("squid:S2095")    // No strict cleanup required. This is the entry point.
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public MessageChannel inboundChannelAdapter() {
        return MessageChannels.flux().get();
    }

    @Bean
    public MessageChannel finalizerChannel() {
        return MessageChannels.flux().get();
    }

    @Bean
    MessageChannel kafkaChannel() {
        return MessageChannels.flux().get();
    }

    @Bean
    RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    public static String getLogCategory(Object o) {
        return o.getClass().getPackageName() + "." + getUserClass(o.getClass()).getSimpleName();
    }
}
