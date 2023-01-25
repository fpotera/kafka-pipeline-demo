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

import io.bluzy.kafkapipelinedemo.commons.kafka.model.EventV1;
import io.bluzy.kafkapipelinedemo.eventsdistributor.Application;
import io.bluzy.kafkapipelinedemo.eventsdistributor.components.AdapterConfiguration;
import io.bluzy.kafkapipelinedemo.eventsdistributor.functions.EventsFilter;
import io.bluzy.kafkapipelinedemo.eventsdistributor.functions.ReceiptHandleNotifier;
import io.bluzy.kafkapipelinedemo.eventsdistributor.services.EventConsumptionScheduler;
import io.bluzy.kafkapipelinedemo.eventsdistributor.services.ThrottleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.support.MessageBuilder;

import static io.bluzy.kafkapipelinedemo.commons.logging.Markers.SECURITY;
import static io.bluzy.kafkapipelinedemo.eventsdistributor.Application.getLogCategory;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Finalizers {
    private final Environment environment;

    private final Application application;

    private final EventsFilter eventsFilter;

    private final ReceiptHandleNotifier receiptHandleNotifier;

    private boolean dryRun;

    private final ThrottleService throttleService;

    private final EventConsumptionScheduler schedulingService;

    private final AdapterConfiguration adapterConfig;

    private LoggingHandler.Level logLevel = LoggingHandler.Level.INFO;

    /**
     * @return Flow for finalizing IdK messages by removing them.
     */
    @Bean
    public IntegrationFlow finalizerFlow() {
        IntegrationFlowBuilder flowBuilder = IntegrationFlow
                .from(application.finalizerChannel())
                .log(logLevel, getLogCategory(this), x-> "from finalizer channel: " + x.getPayload());

        if (dryRun) {
            flowBuilder
                    .log(logLevel, getLogCategory(this), x-> "dry-run handle receipt: " + x.getPayload());

        } else {
            // trigger idk delete for message
            flowBuilder
                    .filter(eventsFilter)
                    .handle(EventV1.class, (payload, headers) -> {
                        receiptHandleNotifier.accept(headers);
                        countCall(true);
                        log.info(SECURITY, "Triggering delete on IDP for message: " + payload);
                        return MessageBuilder.createMessage(payload, headers);
                    })
                    .log(logLevel, getLogCategory(this), x-> "handle receipt: " + x.getPayload());
        }
        return flowBuilder.get();
    }

    public void init() {
        dryRun = environment.acceptsProfiles(Profiles.of("dry-run"));
    }

    private void countCall(boolean callOk) {
        if(adapterConfig.getSchedulingEnabled()) {
            schedulingService.setLatestCallOk(callOk);
            return;
        }
        throttleService.count(callOk);
    }
}
