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
package io.bluzy.kafkapipelinedemo.commons.events.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder
public class Body {
    @JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "type", defaultImpl = Type.class)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = CANCEL_TAKEOUT_SESSION.class),
            @JsonSubTypes.Type(value = COMPOSITE_SCOPE_ELEMENTS_CHANGED.class),
            @JsonSubTypes.Type(value = CONSENT_GROUP_CLIENT_IDS_CHANGED.class),
            @JsonSubTypes.Type(value = DELETE.class),
            @JsonSubTypes.Type(value = DOCUMENT_CONSENT_CHANGED.class),
            @JsonSubTypes.Type(value = GRANT_SCOPE_CONSENT.class),
            @JsonSubTypes.Type(value = INSERT.class),
            @JsonSubTypes.Type(value = RESET_PASSWORD.class),
            @JsonSubTypes.Type(value = REVOKE_SCOPE_CONSENT.class),
            @JsonSubTypes.Type(value = START_TAKEOUT_SESSION.class),
            @JsonSubTypes.Type(value = UPDATE.class),
            @JsonSubTypes.Type(value = UPDATE_MARKETING_PERMISSION.class),
            @JsonSubTypes.Type(value = VERIFIED_DATA_UPDATED.class),
            @JsonSubTypes.Type(value = VERIFIED_DATA_STATUS.class)})
    Type message;
    String timestamp;
}
