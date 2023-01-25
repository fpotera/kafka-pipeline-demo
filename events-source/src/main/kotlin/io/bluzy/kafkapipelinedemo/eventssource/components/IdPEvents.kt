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

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.shaded.gson.JsonArray
import com.nimbusds.jose.shaded.gson.JsonObject
import io.bluzy.kafkapipelinedemo.commons.events.model.Messages
import org.apache.commons.lang3.tuple.MutablePair
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ConcurrentHashMap


@Component
class IdPEvents {
    private val mapper = ObjectMapper()

    @Autowired
    private val genComp: EventGenerator? = null
    fun getNotifications(clientId: String): String? {
        LOG.info("getNotifications called")
        LOG.info("getNotifications clientId: $clientId")
        if (!genComp!!.hasNext()) {
            val nullMsgs = Messages.builder().messages(ArrayList()).build()
            return convertToJsonString(nullMsgs)
        }
        val evt = genComp.next()
        val evtType = evt!!.left.left
        val evtId = evt.left.right
        var clientCounts = generationCounts[clientId]
        if (Objects.isNull(clientCounts)) {
            clientCounts = ConcurrentHashMap()
            clientCounts[evtType] = MutablePair(1, 0)
            generationCounts[clientId] = clientCounts
        } else {
            val counters = clientCounts?.get(evtType)
            if (counters != null) {
                if (Objects.isNull(counters)) {
                    clientCounts?.set(evtType, MutablePair(1, 0))
                } else {
                    counters.left++
                }
            }
        }
        var clientIds = generationIds[clientId]
        if (Objects.isNull(clientIds)) {
            clientIds = ConcurrentHashMap()
            clientIds[evtType] = ArrayList(listOf(evtId))
            generationIds[clientId] = clientIds
        } else {
            val ids = clientIds?.get(evtType)
            if (Objects.isNull(ids)) {
                clientIds?.set(evtType, ArrayList(listOf(evtId)))
            } else {
                ids?.add(evtId)
            }
        }
        return convertToJsonString(evt.getRight())
    }

    fun deleteNotification(clientId: String, receiptHandle: String): String? {
        LOG.info("deleteNotification called")
        LOG.info("deleteNotification clientId: $clientId")
        LOG.info("deleteNotification requestHandle: $receiptHandle")
        var eventName: String? = null
        val clientIds: Map<String, MutableList<String>> = generationIds[clientId]!!
        if (Objects.nonNull(clientIds)) {
            for ((key, value) in clientIds) {
                if (value.remove(receiptHandle)) {
                    eventName = key
                    break
                }
            }
        }
        if (Objects.nonNull(eventName)) {
            val clientCounts: Map<String?, MutablePair<Int, Int>> = generationCounts[clientId]!!
            if (Objects.nonNull(clientCounts)) {
                val counters = clientCounts[eventName]!!
                if (Objects.isNull(counters)) {
                    eventName = null
                } else {
                    counters.right++
                }
            }
            if (Objects.nonNull(eventName)) {
                return null
            }
        }
        return """{
  "code": "AccessDenied",
  "description": "",
  "message": "Illegal receipt handle."
}"""
    }

    fun reportStatus(results: JsonObject) {
        val clients = JsonArray()
        generationCounts.forEach { (clientId: String?, event: Map<String?, MutablePair<Int, Int>>) ->
            val events = JsonArray()
            event.forEach { (evtName: String?, counters: MutablePair<Int, Int>) ->
                val obj = JsonObject()
                obj.addProperty("eventName", evtName)
                obj.addProperty("generated", counters.left)
                obj.addProperty("deleted", counters.right)
                events.add(obj)
            }
            val obj = JsonObject()
            obj.add(clientId, events)
            clients.add(obj)
        }
        results.add("generationCounters", clients)
    }

    private fun convertToJsonString(obj: Any): String? {
        var str: String? = null
        try {
            str = mapper.writeValueAsString(obj)
        } catch (e: JsonProcessingException) {
            LOG.error("error at json conversion", e)
        }
        return str
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(IdPEvents::class.java)
        private val generationCounts: MutableMap<String, MutableMap<String?, MutablePair<Int, Int>>> =
            ConcurrentHashMap()
        private val generationIds: MutableMap<String, MutableMap<String, MutableList<String>>> = ConcurrentHashMap()
    }
}
