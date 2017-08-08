package org.xcolab.mailhandler.web

import org.codemonkey.simplejavamail.MailException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.xcolab.mailhandler.pojo.SpamReport
import org.xcolab.mailhandler.service.EmailService
import org.xcolab.mailhandler.service.ParseService
import org.xcolab.mailhandler.service.SpamFilterService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class ParseController @Autowired constructor(
        private val parseService: ParseService,
        private val emailService: EmailService,
        private val spamFilterService: SpamFilterService) {

    companion object {
        private val log = LoggerFactory.getLogger(ParseController::class.java)
    }

    @RequestMapping
    fun handle(request: HttpServletRequest, response: HttpServletResponse,
               @RequestParam to: String, @RequestParam from: String, @RequestParam subject: String,
               @RequestParam(defaultValue = "") html: String,
               @RequestParam(defaultValue = "") text: String,
               @RequestParam(value = "spam_score") spamAssassinScore: Float,
               @RequestParam(value = "spam_report") spamAssassinReport: String): ResponseEntity<String> {

        if (StringUtils.isEmpty(html) && StringUtils.isEmpty(text)) {
            log.error("Email has neither text nor html parameter.")
            return ResponseEntity.badRequest().body("Need to specify at least one of html or text")
        }

        val parsedEmail = parseService.parseEmail(request, to, from, subject, html, text)
        val spamReport = SpamReport(spamAssassinScore, spamAssassinReport)

        if (spamFilterService.shouldFilterMessage(parsedEmail, spamReport)) {
            return ResponseEntity.ok("Message processed successfully; marked as spam.")
        }

        val matchedMappings = parseService.parseMappings(parsedEmail)
        if (matchedMappings.isEmpty()) {
            if (!to.contains("no-reply@")) {
                log.warn("No mappings found for to = {}.", to)
            }
            return ResponseEntity.notFound().build<String>()
        }

        try {
            for (matchedMapping in matchedMappings) {
                emailService.forwardEmail(matchedMapping.getEmailAddress(),
                        matchedMapping.mapping.recipients,
                        spamFilterService.formatEmailWithSpamReport(parsedEmail, spamReport))
            }
        } catch (e: MailException) {
            log.error("Exception while sending mail:", e)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body<String>(e.message)
        }

        return ResponseEntity.ok("Email sent.")
    }
}
