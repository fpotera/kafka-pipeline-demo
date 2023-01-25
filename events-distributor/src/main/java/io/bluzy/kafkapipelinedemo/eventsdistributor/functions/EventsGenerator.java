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
package io.bluzy.kafkapipelinedemo.eventsdistributor.functions;

import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import io.bluzy.kafkapipelinedemo.commons.events.model.*;
import io.bluzy.kafkapipelinedemo.eventsdistributor.configproperties.EventsGeneratorConfigProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.iterators.LoopingIterator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("generator")
@RequiredArgsConstructor
public class EventsGenerator implements Supplier<Messages> {
    private Iterator<String> uidIterable;
    private final EventsGeneratorConfigProperties config;

    public void init() {
        uidIterable = new LoopingIterator<>(config.getUidsPool().isEmpty() ?
                Collections.singletonList(UUID.randomUUID().toString()): config.getUidsPool());
    }

    Supplier<CANCEL_TAKEOUT_SESSION> cancel_takeout_session = () -> CANCEL_TAKEOUT_SESSION.builder()
            .takeoutSessionId(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString())
            .build();

    Supplier<COMPOSITE_SCOPE_ELEMENTS_CHANGED> composite_scope_elements_changed = () -> COMPOSITE_SCOPE_ELEMENTS_CHANGED.builder()
            .element(UUID.randomUUID().toString())
            .element(UUID.randomUUID().toString())
            .scope("scope")
            .build();

    Supplier<CONSENT_GROUP_CLIENT_IDS_CHANGED> consent_group_client_ids_changed = () -> CONSENT_GROUP_CLIENT_IDS_CHANGED.builder()
            .clientId(UUID.randomUUID().toString())
            .clientId(UUID.randomUUID().toString())
            .groupId(UUID.randomUUID().toString())
            .build();

    Supplier<DELETE> delete = () -> DELETE.builder()
            .id(uidIterable.next())
            .build();

    Supplier<DOCUMENT_CONSENT_CHANGED> document_consent_changed = () -> DOCUMENT_CONSENT_CHANGED.builder()
            .clientId(UUID.randomUUID().toString())
            .clientId(UUID.randomUUID().toString())
            .documentId(UUID.randomUUID().toString())
            .version(21)
            .build();

    Supplier<GRANT_SCOPE_CONSENT> grant_scope_consent = () -> GRANT_SCOPE_CONSENT.builder()
            .build();

    Supplier<INSERT> insert = () -> INSERT.builder()
            .build();

    Supplier<RESET_PASSWORD> reset_password = () -> RESET_PASSWORD.builder()
            .id(UUID.randomUUID().toString())
            .build();

    Supplier<REVOKE_SCOPE_CONSENT> revoke_scope_consent = () -> REVOKE_SCOPE_CONSENT.builder()
            .build();

    Supplier<START_TAKEOUT_SESSION> start_takeout_session = () -> START_TAKEOUT_SESSION.builder()
            .build();

    Supplier<UPDATE> update = () -> UPDATE.builder()
            .field("/personalData/emailAddresses")
            .field("/personalData/phoneNumbers")
            .field("/personalData/firstName")
            .field("/personalData/lastName")
            .field("/personalData/addresses")
            .field("/personalData/preferredLanguages")
            .id(UUID.randomUUID().toString())
            .build();

    Supplier<UPDATE> updatePicture = () -> UPDATE.builder()
            .field("/personalData/nickname")
            .field("/personalData/profilePicture")
            .id(UUID.randomUUID().toString())
            .build();

    Supplier<UPDATE_MARKETING_PERMISSION> update_marketing_permission = () -> UPDATE_MARKETING_PERMISSION.builder()
            .build();

    Supplier<VERIFIED_DATA_UPDATED> verified_data_updated = () -> VERIFIED_DATA_UPDATED.builder()
            .build();

    Function<Supplier<? extends Type>, Message> message = (type) -> Message.builder()
            .receiptHandle("TEST-" + UUID.randomUUID().toString())
            .body(Body.builder()
                    .timestamp(String.valueOf(Instant.now().toEpochMilli()))
                    .message(type.get())
                    .build())
            .build();

    Map<String, Supplier<? extends Type>> mappingSuppliers = Map.ofEntries(
            Map.entry("CANCEL_TAKEOUT_SESSION", cancel_takeout_session),
            Map.entry("COMPOSITE_SCOPE_ELEMENTS_CHANGED", composite_scope_elements_changed),
            Map.entry("CONSENT_GROUP_CLIENT_IDS_CHANGED", consent_group_client_ids_changed),
            Map.entry("DELETE", delete),
            Map.entry("DOCUMENT_CONSENT_CHANGED", document_consent_changed),
            Map.entry("GRANT_SCOPE_CONSENT", grant_scope_consent),
            Map.entry("INSERT", insert),
            Map.entry("RESET_PASSWORD", reset_password),
            Map.entry("REVOKE_SCOPE_CONSENT", revoke_scope_consent),
            Map.entry("START_TAKEOUT_SESSION", start_takeout_session),
            Map.entry("UPDATE", update),
            Map.entry("UPDATEPICTURE", updatePicture),
            Map.entry("UPDATE_MARKETING_PERMISSION", update_marketing_permission),
            Map.entry("VERIFIED_DATA_UPDATED", verified_data_updated)
    );


    @Override
    public Messages get() {
        Messages.MessagesBuilder builder = Messages.builder();
        config.getEventTypes().forEach(type -> builder.message(message.apply(mappingSuppliers.get(type))));
        return builder.build();
    }
}
