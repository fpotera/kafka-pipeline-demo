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
package io.bluzy.kafkapipelinedemo.eventsdistributor.configproperties;

import io.bluzy.kafkapipelinedemo.eventsdistributor.functions.EventsSupplier;
import io.bluzy.kafkapipelinedemo.eventsdistributor.functions.ReceiptHandleNotifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class EventsSupplierConfigProperties {
    @Bean
    @Primary
    @ConfigurationProperties("idp")
    IdPConfig buildIdPConfig() {
        return new IdPConfig();
    }

    @Bean
    public EventsSupplier buildEventsSupplier() {
        return new EventsSupplier();
    }

    @Bean
    ReceiptHandleNotifier buildReceiptHandleNotifier() {
        return new ReceiptHandleNotifier();
    }
}
