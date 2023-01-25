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
package io.bluzy.kafkapipelinedemo.eventsdistributor.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import io.bluzy.kafkapipelinedemo.eventsdistributor.components.AdapterConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Scope("singleton")
public class EventConsumptionScheduler implements Trigger {
    private final AdapterConfiguration config;

    private AtomicBoolean latestCallOk = new AtomicBoolean(true);

    private Optional<Integer> currentStep = empty();

    private Date currentDate = new Date();

    public void setLatestCallOk(boolean latestCallOk) {
        this.latestCallOk.set(latestCallOk);
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        Date lastExec = triggerContext.lastScheduledExecutionTime();

        Date nextExec = currentDate();
        if(lastExec != null) {
            Date next = Date.from(lastExec.toInstant().plus(pollingInterval()));
            if(next.after(nextExec)) {
                nextExec = next;
            }
        }
        log.debug("scheduling - nextExecutionTime: {}", nextExec);

        return nextExec;
    }

    @Override
    public Instant nextExecution(TriggerContext triggerContext) {
        return null;
    }

    private Duration pollingInterval() {
        Duration duration = config.getNotificationsPollingInterval();

        if(config.getSchedulingEnabled()) {
            if(latestCallOk.get()) {
                if(currentStep.isPresent()) {
                    currentStep = empty();
                }
            }
            else {
                if(currentStep.isEmpty()) {
                    currentStep = of(0);
                }
                else {
                    currentStep = of(currentStep.get() + 1);
                }

                if(currentStep.get()==0) {
                    duration = config.getSchedulingBaseDelay();
                }
                else {
                    duration = config.getSchedulingBaseDelay().multipliedBy((long) config.getSchedulingMultiplier() * currentStep.get());
                    if(duration.compareTo(config.getSchedulingMaxDelay()) > 0) {
                        duration = config.getSchedulingMaxDelay();
                    }
                }
            }
            log.trace("scheduling - latestCallOk: {} counter: {}", latestCallOk.get(), currentStep.isPresent()?currentStep.get():"null");
        }

        return duration;
    }

    protected Date getCurrentDate() {
        return currentDate;
    }

    private Date currentDate() {
        currentDate = new Date();
        return currentDate;
    }
}
