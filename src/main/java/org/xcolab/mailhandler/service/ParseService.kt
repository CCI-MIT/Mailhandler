package org.xcolab.mailhandler.service

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.xcolab.mailhandler.config.MailProperties
import org.xcolab.mailhandler.pojo.MatchedMapping
import org.xcolab.mailhandler.pojo.ParsedEmail
import org.xcolab.mailhandler.web.ParseController
import java.util.*
import javax.mail.internet.InternetAddress
import javax.servlet.http.HttpServletRequest

@Service
@EnableConfigurationProperties(MailProperties::class)
class ParseService(val mailProperties: MailProperties) {

    companion object {
        private val log = LoggerFactory.getLogger(ParseController::class.java)
    }

    fun parseMappings(parsedEmail: ParsedEmail): ArrayList<MatchedMapping> {
        val matchedMappings = ArrayList<MatchedMapping>()
        for (domain in mailProperties.domains) {
            domain.mappings
                    .filter {
                        parsedEmail.to.contains(InternetAddress("${it.username}@${domain.domain}"))
                    }
                    .mapTo(matchedMappings) { MatchedMapping(domain, it) }
        }

        return matchedMappings
    }

    fun parseEmail(request: HttpServletRequest, to: String, from: String,
                   subject: String, html: String, text: String): ParsedEmail {
        val toAddresses = InternetAddress.parseHeader(to, false).toList()
        val fromAddress = InternetAddress(from)

        log.debug("Parsed email from {} to {}", fromAddress, toAddresses)

        val attachments = getAttachments(request)
        return ParsedEmail(attachments, toAddresses, fromAddress, subject, html, text)

    }

    private fun getAttachments(request: HttpServletRequest): Collection<MultipartFile> {
        val fileMap: Map<String, MultipartFile>
        if (request is MultipartHttpServletRequest) {
            fileMap = request.fileMap
        } else {
            fileMap = emptyMap<String, MultipartFile>()
        }
        return fileMap.values
    }
}
