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

import java.util.ArrayList;
import java.util.function.Supplier;

import io.bluzy.kafkapipelinedemo.commons.events.model.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static io.bluzy.kafkapipelinedemo.commons.logging.Markers.SECURITY;
import static java.util.function.Predicate.isEqual;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static reactor.core.publisher.Mono.error;

@Slf4j
@RequiredArgsConstructor
public class EventsSupplier implements Supplier<Messages> {

    @Override
    public Messages get() {

        Messages messages = Messages.builder().messages(new ArrayList<>()).build();
        String eventsUrl = "";

        try {
            log.debug("Calling GET on EventsURL: {}...", eventsUrl);
            messages = WebClient.create(eventsUrl)
                    .get()
                    .uri("/" + eventsUrl)
                    .retrieve()
                    .onStatus(isEqual(NO_CONTENT), c -> error(new NoContentException()))
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            Mono.error(new RuntimeException("Client error while calling GET on " + eventsUrl + " code: " + clientResponse.statusCode().value())
                            ))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            Mono.error(new RuntimeException("Server error while calling GET on " + eventsUrl + " code: " + clientResponse.statusCode().value())
                            ))
                    .bodyToMono(Messages.class)
                    .block();
        } catch (NoContentException ignored) {
            log.info(SECURITY, "204: GET on EventsUrl successful, but no new events");
        } catch (RuntimeException e) {
            log.error(SECURITY, "GET on EventsUrl failed. Client problem: " + e.getMessage());
        } catch (Exception e) {
            log.error(SECURITY, "ERROR: GET on EventsUrl failed with message: {} and error code: {}", e.getMessage(), INTERNAL_SERVER_ERROR);
            throw e;
        }

        log.info(SECURITY, "200: GET on EventsUrl. Incoming Messages from idp: " + messages);
        return messages;
    }

    static class NoContentException extends RuntimeException {}
}
