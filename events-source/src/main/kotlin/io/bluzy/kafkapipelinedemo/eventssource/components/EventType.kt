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
package io.bluzy.kafkapipelinedemo.eventssource.components

import com.fasterxml.jackson.databind.ObjectMapper
import io.bluzy.kafkapipelinedemo.commons.events.model.Body
import io.bluzy.kafkapipelinedemo.commons.events.model.Message
import io.bluzy.kafkapipelinedemo.commons.events.model.Messages
import io.bluzy.kafkapipelinedemo.commons.events.model.Type
import io.bluzy.kafkapipelinedemo.commons.events.model.*
import org.apache.commons.lang3.tuple.ImmutablePair
import java.util.*


enum class EventType {
    CANCEL_TAKEOUT_SESSION, DELETE, INSERT, UPDATE, RESET_PASSWORD, COMPOSITE_SCOPE_ELEMENTS_CHANGED, CONSENT_GROUP_CLIENT_IDS_CHANGED, GRANT_SCOPE_CONSENT, REVOKE_SCOPE_CONSENT, UPDATE_MARKETING_PERMISSION, DOCUMENT_CONSENT_CHANGED;

    private val mapper = ObjectMapper()
    fun buildNotificationEvent(vararg args: Any): ImmutablePair<ImmutablePair<String, String>, Messages> {
        var evt: Type? = null
        evt = when (this) {
            CANCEL_TAKEOUT_SESSION -> io.bluzy.kafkapipelinedemo.commons.events.model.CANCEL_TAKEOUT_SESSION.builder()
                .takeoutSessionId(args[0] as String)
                .userId(args[1] as String)
                .build()

            DELETE -> io.bluzy.kafkapipelinedemo.commons.events.model.DELETE.builder()
                .id(args[0] as String)
                .build()

            INSERT -> io.bluzy.kafkapipelinedemo.commons.events.model.INSERT.builder()
                .id(args[0] as String)
                .build()

            UPDATE -> io.bluzy.kafkapipelinedemo.commons.events.model.UPDATE.builder()
                .fields(listOf(*args[0] as Array<String?>))
                .id(args[1] as String)
                .build()

            RESET_PASSWORD -> io.bluzy.kafkapipelinedemo.commons.events.model.RESET_PASSWORD.builder()
                .id(args[0] as String)
                .build()

            COMPOSITE_SCOPE_ELEMENTS_CHANGED -> io.bluzy.kafkapipelinedemo.commons.events.model.COMPOSITE_SCOPE_ELEMENTS_CHANGED.builder()
                .elements(listOf(*args[0] as Array<String?>))
                .scope(args[1] as String)
                .build()

            CONSENT_GROUP_CLIENT_IDS_CHANGED -> io.bluzy.kafkapipelinedemo.commons.events.model.CONSENT_GROUP_CLIENT_IDS_CHANGED.builder()
                .clientIds(listOf(*args[0] as Array<String?>))
                .groupId(args[1] as String)
                .build()

            GRANT_SCOPE_CONSENT -> io.bluzy.kafkapipelinedemo.commons.events.model.GRANT_SCOPE_CONSENT.builder()
                .clientIds(listOf(*args[0] as Array<String?>))
                .userId(args[1] as String)
                .scopes(listOf(*args[2] as Array<String?>))
                .consentedClientIds(listOf(*args[3] as Array<String?>))
                .build()

            REVOKE_SCOPE_CONSENT -> io.bluzy.kafkapipelinedemo.commons.events.model.REVOKE_SCOPE_CONSENT.builder()
                .clientIds(listOf(*args[0] as Array<String?>))
                .userId(args[1] as String)
                .scopes(listOf(*args[2] as Array<String?>))
                .consentedClientIds(listOf(*args[3] as Array<String?>))
                .build()

            UPDATE_MARKETING_PERMISSION -> io.bluzy.kafkapipelinedemo.commons.events.model.UPDATE_MARKETING_PERMISSION.builder()
                .clientIds(listOf(*args[0] as Array<String?>))
                .userId(args[1] as String)
                .countryCode(args[2] as String)
                .email(args[3] as String)
                .mail(args[4] as String)
                .mdKey(args[5] as String)
                .phone(args[6] as String)
                .expiresAt(args[7] as String)
                .build()

            DOCUMENT_CONSENT_CHANGED -> io.bluzy.kafkapipelinedemo.commons.events.model.DOCUMENT_CONSENT_CHANGED.builder()
                .userId(args[0] as String)
                .clientId(args[1] as String)
                .clientIds(listOf(*args[2] as Array<String?>))
                .documentId(args[3] as String)
                .legalOwner(args[4] as String)
                .namespace(args[5] as String)
                .locale(args[6] as String)
                .updatedAt(args[7] as String)
                .updatedBy(args[8] as String)
                .userDecision((args[9] as Boolean))
                .version((args[10] as Int))
                .scopes(listOf(*args[11] as Array<String?>))
                .groupId(args[12] as String)
                .build()
        }
        val body = Body.builder()
            .message(evt)
            .timestamp(System.currentTimeMillis().toString())
            .build()
        val msg = Message.builder()
            .body(body)
            .receiptHandle(UUID.randomUUID().toString())
            .build()
        return ImmutablePair(
            ImmutablePair(name, msg.receiptHandle),
            Messages.builder().messages(listOf(msg)).build()
        )
    }

    @Throws(Exception::class)
    fun buildNotificationEventFromJson(event: String?): ImmutablePair<ImmutablePair<String, String>, Messages> {
        var evt: Type? = null
        when (this) {
            CANCEL_TAKEOUT_SESSION -> evt = mapper.readValue(
                event,
                io.bluzy.kafkapipelinedemo.commons.events.model.CANCEL_TAKEOUT_SESSION::class.java
            )

            DELETE -> evt = mapper.readValue(event, io.bluzy.kafkapipelinedemo.commons.events.model.DELETE::class.java)
            INSERT -> evt = mapper.readValue(event, io.bluzy.kafkapipelinedemo.commons.events.model.INSERT::class.java)
            UPDATE -> evt = mapper.readValue(event, io.bluzy.kafkapipelinedemo.commons.events.model.UPDATE::class.java)
            RESET_PASSWORD -> evt = mapper.readValue(event, io.bluzy.kafkapipelinedemo.commons.events.model.RESET_PASSWORD::class.java)
            COMPOSITE_SCOPE_ELEMENTS_CHANGED -> evt = mapper.readValue(
                event,
                io.bluzy.kafkapipelinedemo.commons.events.model.COMPOSITE_SCOPE_ELEMENTS_CHANGED::class.java
            )

            CONSENT_GROUP_CLIENT_IDS_CHANGED -> evt = mapper.readValue(
                event,
                io.bluzy.kafkapipelinedemo.commons.events.model.CONSENT_GROUP_CLIENT_IDS_CHANGED::class.java
            )

            GRANT_SCOPE_CONSENT -> evt = mapper.readValue(
                event,
                io.bluzy.kafkapipelinedemo.commons.events.model.GRANT_SCOPE_CONSENT::class.java
            )

            REVOKE_SCOPE_CONSENT -> evt = mapper.readValue(
                event,
                io.bluzy.kafkapipelinedemo.commons.events.model.REVOKE_SCOPE_CONSENT::class.java
            )

            UPDATE_MARKETING_PERMISSION -> evt = mapper.readValue(
                event,
                io.bluzy.kafkapipelinedemo.commons.events.model.UPDATE_MARKETING_PERMISSION::class.java
            )

            DOCUMENT_CONSENT_CHANGED -> evt = mapper.readValue(
                event,
                io.bluzy.kafkapipelinedemo.commons.events.model.DOCUMENT_CONSENT_CHANGED::class.java
            )
        }
        val body = Body.builder()
            .message(evt)
            .timestamp(System.currentTimeMillis().toString())
            .build()
        val msg = Message.builder()
            .body(body)
            .receiptHandle(UUID.randomUUID().toString())
            .build()
        return ImmutablePair(
            ImmutablePair(name, msg.receiptHandle),
            Messages.builder().messages(listOf(msg)).build()
        )
    }
}