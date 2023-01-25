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

import java.net.URL;

import io.bluzy.kafkapipelinedemo.commons.kafka.model.EventV1;
import org.springframework.integration.core.GenericSelector;
import org.springframework.stereotype.Component;

@Component
public class EventsFilter implements GenericSelector<org.springframework.messaging.Message<EventV1>> {
    @Override
    public boolean accept(org.springframework.messaging.Message<EventV1> message) {
        try {
            new URL("");
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
