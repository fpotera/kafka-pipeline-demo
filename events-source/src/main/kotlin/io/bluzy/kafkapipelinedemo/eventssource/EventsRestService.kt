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
package io.bluzy.kafkapipelinedemo.eventssource

import io.bluzy.kafkapipelinedemo.commons.idp.events.Body
import io.bluzy.kafkapipelinedemo.commons.idp.events.Message
import io.bluzy.kafkapipelinedemo.commons.idp.events.Messages
import io.bluzy.kafkapipelinedemo.commons.idp.events.type.UPDATE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class EventsRestService {
    @GetMapping("/events")
    fun getEvents(): Messages {
        return Messages.builder().message(Message.builder().body(Body.builder().message(UPDATE.builder().build()).build()).build()).build()
    }
}
