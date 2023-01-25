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

import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class ReceiptHandleNotifier implements Consumer<MessageHeaders> {

    @Override
    public void accept(MessageHeaders headers) {
        WebClient.create()
                .method(HttpMethod.DELETE)
                .uri("/")
                .bodyValue("")
                .exchange()
                .block();
    }
}
