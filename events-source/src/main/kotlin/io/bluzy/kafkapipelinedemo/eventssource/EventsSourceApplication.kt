package io.bluzy.kafkapipelinedemo.eventssource

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventsSourceApplication

fun main(args: Array<String>) {
	runApplication<EventsSourceApplication>(*args)
}
