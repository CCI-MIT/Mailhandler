package org.xcolab.mailhandler

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

import org.xcolab.mailhandler.config.MailProperties

@SpringBootApplication
@EnableConfigurationProperties(MailProperties::class)
open class MailHandlerApplication

fun main(args: Array<String>) {
    SpringApplication.run(MailHandlerApplication::class.java, *args)
}
