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
package io.bluzy.kafkapipelinedemo.commons.kafka.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.bluzy.kafkapipelinedemo.commons.events.model.Messages;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder
public class EventV1 {
    Object source;
    long timestamp = System.currentTimeMillis();
    Object container;

    String version = "customer-1.1.0";

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = String.class)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Messages.class, name = "Idp-Messages")})
    Object originalMessage;

    long originalTimestamp;

    String eventType;

    @NonNull
    String eventId;

    String customerId;

    String originalIdp;

    Object payload;

    String legalEntity;

}
