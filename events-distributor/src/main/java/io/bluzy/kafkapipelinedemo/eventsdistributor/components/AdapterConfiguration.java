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
package io.bluzy.kafkapipelinedemo.eventsdistributor.components;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;

import io.bluzy.kafkapipelinedemo.commons.events.model.MessageTypes;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;

@Component
@Data
public class AdapterConfiguration {
    private Map<String, List<String>> filterExcludeUpdate = Collections.emptyMap();

    @Value("#{${idp.filter-include-update}}")
    private Map<String, List<String>> filterIncludeUpdate = Collections.emptyMap();

    @Value("#{${idp.idk-mapping-topics}}")
    private Map<MessageTypes, Map<String, List<String>>> idkMapping = Collections.emptyMap();

    @Value("${idp.notifications-polling-interval}")
    private Duration notificationsPollingInterval = Duration.ZERO;

    @Value("${idp.throttling.active:false}")
    private Boolean throttlingActive;

    @Value("${idp.throttling.first.delay:100}")
    @DurationUnit(MILLIS)
    private Duration throttlingFirstDelay;

    @Value("${idp.throttling.retry.multiplicator:1}")
    private int throttlingRetryMultiplicator;

    @Value("${idp.throttling.delay:100}")
    @DurationUnit(MILLIS)
    private Duration throttlingDelay;

    @Value("${idp.throttling.threshold:120}")
    private int throttlingThreshold;

    @Value("${idp.scheduling.enabled:false}")
    private Boolean schedulingEnabled;

    @Value("${idp.scheduling.base-delay:false}")
    @DurationUnit(MILLIS)
    private Duration schedulingBaseDelay;

    @Value("${idp.scheduling.multiplier:2}")
    private int schedulingMultiplier;

    @Value("${idp.scheduling.max-delay:60}")
    @DurationUnit(SECONDS)
    private Duration schedulingMaxDelay;
}
