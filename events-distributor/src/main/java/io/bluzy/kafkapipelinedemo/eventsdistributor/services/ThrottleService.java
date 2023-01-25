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
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import static java.lang.Math.pow;

import io.bluzy.kafkapipelinedemo.eventsdistributor.components.AdapterConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.stereotype.Service;

@SuppressWarnings("deprecation")
@Service
@Slf4j
@RequiredArgsConstructor
@Scope("singleton")
public class ThrottleService implements Trigger {

    private final AdapterConfiguration config;

    private Supplier<Instant> currentMinuteSupplier = ThrottleService::currentMinute;

    private BiPredicate<Date, Date> after = Date::after;

    private Instant current;
    private int count = 0;
    private boolean latestCallOk = true;

    private Duration currentMultipliedDelay = null;
    private int currentStep = 0;

    public void count(boolean callOk) {
        synchronized (this) {
            latestCallOk = callOk;
            if(callOk) {
                if (!current.equals(currentMinuteSupplier.get())) {
                    current = currentMinuteSupplier.get();
                    count = 1;
                } else {
                    count++;
                }
                log.debug("throttling - minute: {} processed: {} messages", Date.from(current).getMinutes(), count);
            }
            else {
                log.debug("throttling - minute: {} call unsuccessful", Date.from(current).getMinutes());
            }
        }
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        Date lastExec = triggerContext.lastScheduledExecutionTime();
        Date nextExec = new Date();
        if(lastExec != null) {
            Date next = Date.from(lastExec.toInstant().plus(pollingInterval()));
            if(after.test(next, nextExec))
                nextExec = next;
        }
        log.debug("throttling - nextExecutionTime: {}", nextExec);
        return nextExec;
    }

    @Override
    public Instant nextExecution(TriggerContext triggerContext) {
        return null;
    }

    public void init() {
        current = currentMinuteSupplier.get();
        if(config.getThrottlingActive()) {
            log.trace("throttling is active - firstDelay: {} - multiplicator: {} - delay after threshold: {}",
                    config.getThrottlingFirstDelay(), config.getThrottlingRetryMultiplicator(), config.getThrottlingDelay());
        }
        else {
            log.trace("throttling is inactive");
        }
    }

    Instant getCurrent() {
        if(Objects.isNull(current)) {
            current = currentMinuteSupplier.get();
        }
        return current;
    }

    boolean isUnderThreshold() {
        return count <= config.getThrottlingThreshold();
    }

    void setCurrentMinuteSupplier(Supplier<Instant> currentMinuteSupplier) {
        this.currentMinuteSupplier = currentMinuteSupplier;
    }

    void setAfter(BiPredicate<Date, Date> after) {
        this.after = after;
    }

    Duration multiply(Duration last, Duration first, int multiplicator, int step) {
        return last.plus(first.multipliedBy((long) pow(multiplicator, step)));
    }

    private Duration pollingInterval() {
        Duration duration = config.getNotificationsPollingInterval();
        if(config.getThrottlingActive()) {
            if(!reset()) {
                if(latestCallOk && Objects.nonNull(currentMultipliedDelay)) {
                    currentMultipliedDelay = null;
                }
                if(!latestCallOk) {
                    if(Objects.isNull(currentMultipliedDelay)) {
                        currentMultipliedDelay = config.getThrottlingFirstDelay();
                        currentStep = 0;
                        duration = currentMultipliedDelay;
                    }
                    else if(isUnderThreshold()){
                        currentMultipliedDelay = multiply(currentMultipliedDelay, config.getThrottlingFirstDelay(), config.getThrottlingRetryMultiplicator(), currentStep++);
                        duration = currentMultipliedDelay;
                    }
                }
                if(!isUnderThreshold()) {
                    duration = config.getThrottlingDelay();
                }
            }
            else {
                currentMultipliedDelay = null;
            }

            log.debug("throttling active minute: {} count: {} threshold: {} - duration: {}", Date.from(current).getMinutes(), count, config.getThrottlingThreshold(), duration);
        }

        return duration;
    }

    private boolean reset() {
        synchronized (this) {
            if (!current.equals(currentMinuteSupplier.get())) {
                current = currentMinuteSupplier.get();
                count = 0;
                return true;
            }
            return false;
        }
    }

    private static Instant currentMinute() {
        return Instant.now().truncatedTo(ChronoUnit.MINUTES);
    }
}

