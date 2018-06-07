package org.xcolab.mailhandler.service

import org.apache.commons.lang3.StringUtils
import org.codemonkey.simplejavamail.Mailer
import org.codemonkey.simplejavamail.TransportStrategy
import org.codemonkey.simplejavamail.email.Email
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.xcolab.mailhandler.config.SmtpProperties
import org.xcolab.mailhandler.pojo.ParsedEmail
import java.io.IOException

@Service
@EnableConfigurationProperties(SmtpProperties::class)
class EmailService
@Autowired constructor(smtpProperties: SmtpProperties) {

    companion object {
        private val log = LoggerFactory.getLogger(EmailService::class.java)
    }

    private val smtpHost: String = smtpProperties.host
    private val smtpPort: Int = smtpProperties.port
    private val smtpUsername: String? = smtpProperties.user
    private val smtpPassword: String? = smtpProperties.pass
    private val smtpConnection: String = smtpProperties.transport

    fun forwardEmail(originalRecipient: String, mappedRecipients: List<String>,
                     parsedEmail: ParsedEmail) {
        val emailBuilder = Email.Builder()
                .from(null, parsedEmail.from.address)
                .subject("[$originalRecipient] " + parsedEmail.subject)

        if (!StringUtils.isEmpty(parsedEmail.html)) {
            emailBuilder.textHTML(parsedEmail.html)
        } else {
            emailBuilder.textHTML(parsedEmail.text)
        }

        for (mappedRecipient in mappedRecipients) {
            emailBuilder.to(null, mappedRecipient)
        }

        for (attachment in parsedEmail.attachments) {
            try {
                emailBuilder.addAttachment(attachment.originalFilename,
                        attachment.bytes, attachment.contentType)
            } catch (e: IOException) {
                log.error("Could not forward attachment {} for email {}",
                        attachment.originalFilename, parsedEmail.subject)
            }

        }
        log.debug("Sending email {} from {} to {}", parsedEmail.subject, parsedEmail.from.address,
                mappedRecipients)
        sendEmail(emailBuilder.build())
    }

    private fun sendEmail(email: Email) {
        val mailer = when (smtpConnection) {
            "TLS" -> Mailer(smtpHost, smtpPort, smtpUsername, smtpPassword,
                    TransportStrategy.SMTP_TLS)
            "SSL" -> Mailer(smtpHost, smtpPort, smtpUsername, smtpPassword,
                    TransportStrategy.SMTP_SSL)
            else -> Mailer(smtpHost, smtpPort, smtpUsername, smtpPassword)
        }
        mailer.sendMail(email)
    }
}
