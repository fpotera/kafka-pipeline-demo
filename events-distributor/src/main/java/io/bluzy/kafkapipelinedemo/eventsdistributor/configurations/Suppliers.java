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
package io.bluzy.kafkapipelinedemo.eventsdistributor.configurations;

import java.util.Optional;

import io.bluzy.kafkapipelinedemo.commons.events.model.Messages;
import io.bluzy.kafkapipelinedemo.eventsdistributor.Application;
import io.bluzy.kafkapipelinedemo.eventsdistributor.components.AdapterConfiguration;
import io.bluzy.kafkapipelinedemo.eventsdistributor.functions.EventsGenerator;
import io.bluzy.kafkapipelinedemo.eventsdistributor.functions.EventsSupplier;
import io.bluzy.kafkapipelinedemo.eventsdistributor.services.EventConsumptionScheduler;
import io.bluzy.kafkapipelinedemo.eventsdistributor.services.ThrottleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.scheduling.Trigger;

import static io.bluzy.kafkapipelinedemo.eventsdistributor.Application.getLogCategory;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Suppliers {

    private final EventsSupplier eventsSupplier;

    private final AdapterConfiguration adapterConfig;

    private final Application application;

    private final Optional<EventsGenerator> eventsGenerator;

    private final ThrottleService throttleService;

    private final EventConsumptionScheduler schedulingService;

    private LoggingHandler.Level logLevel = LoggingHandler.Level.INFO;

    @ConditionalOnProperty(value = "idp.supplier.enabled", matchIfMissing = true)
    @Bean
    public IntegrationFlow idkSupplier() {

        return IntegrationFlow
                // request idk messages with a fixed poller
                .fromSupplier(eventsSupplier,
                        sourcePollingChannelAdapterSpec -> sourcePollingChannelAdapterSpec.poller(Pollers.trigger(buildTrigger())))
                .filter(msgs -> {
                    boolean empty = ((Messages) msgs).getMessages().isEmpty();
                    if(empty) {
                        countCall(false);
                    }
                    return !empty;
                })
                .log(logLevel, getLogCategory(this), x-> "from idp: " + x.getPayload())

                .handle((p, h) -> {
                    return p;
                })

                // forward to channel
                .channel(application.inboundChannelAdapter())

                .get();
    }

    @Profile("generator")
    @Bean
    public IntegrationFlow idkGenerator() {
        return IntegrationFlow
                // request idk messages with a fixed poller
                .fromSupplier(eventsGenerator.get(),
                        sourcePollingChannelAdapterSpec -> sourcePollingChannelAdapterSpec.poller(Pollers.fixedDelay(adapterConfig.getNotificationsPollingInterval().plusSeconds(10).multipliedBy(10))))
                .log(logLevel, getLogCategory(this), x-> "from generator: " + x.getPayload())

                // forward to channel
                .channel(application.inboundChannelAdapter())

                .get();
    }

    private Trigger buildTrigger() {
        if(adapterConfig.getSchedulingEnabled()) {
            return schedulingService;
        }
        return throttleService;
    }

    private void countCall(boolean callOk) {
        if(adapterConfig.getSchedulingEnabled()) {
            schedulingService.setLatestCallOk(callOk);
            return;
        }
        throttleService.count(callOk);
    }
}

