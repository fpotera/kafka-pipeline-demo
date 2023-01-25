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

import io.bluzy.kafkapipelinedemo.commons.events.model.Messages
import jakarta.annotation.PostConstruct
import org.apache.commons.lang3.tuple.ImmutablePair
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.time.Instant


@Component
@Scope("singleton")
class EventGenerator : MutableIterator<ImmutablePair<ImmutablePair<String, String>, Messages>?> {
    @Value("\${generate.events}")
    private lateinit var generateEvents: Array<String>

    @Value("\${updated.fields}")
    private lateinit var updatedFields: Array<String>

    @Value("\${userid}")
    private lateinit var userIds: Array<String>

    @Value("\${generator.cadence}")
    private val generatorCadence = 0

    private var generateAfter: Instant? = null
    private var millisInterval: Long = 0
    private var crtEvtType = -1
    private var crtUserId = -1
    override fun hasNext(): Boolean {
        return Instant.now().isAfter(generateAfter)
    }

    override fun next(): ImmutablePair<ImmutablePair<String, String>, Messages>? {
        generateAfter = generateAfter!!.plusMillis(millisInterval)
        val max = generateEvents.size
        if (++crtEvtType == max) crtEvtType = 0
        return buildEvent(generateEvents[crtEvtType])
    }

    override fun remove() {
        TODO("Not yet implemented")
    }

    private fun buildEvent(eventName: String): ImmutablePair<ImmutablePair<String, String>, Messages>? {
        val max = userIds.size
        if (++crtUserId == max) crtUserId = 0
        return when (EventType.valueOf(eventName)) {
            EventType.UPDATE -> EventType.UPDATE.buildNotificationEvent(
                (updatedFields as Any), userIds[crtUserId]
            )

            EventType.DELETE -> EventType.DELETE.buildNotificationEvent(userIds[crtUserId])
            EventType.INSERT -> EventType.INSERT.buildNotificationEvent(userIds[crtUserId])
            EventType.RESET_PASSWORD -> EventType.RESET_PASSWORD.buildNotificationEvent(userIds[crtUserId])
            else -> null
        }
    }

    @PostConstruct
    private fun init() {
        millisInterval = (3600000 / generatorCadence).toLong()
        generateAfter = Instant.now()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(EventGenerator::class.java)
    }
}
