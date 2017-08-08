package org.xcolab.mailhandler.pojo

import org.springframework.web.multipart.MultipartFile
import javax.mail.internet.InternetAddress

data class ParsedEmail(
        val attachments: Collection<MultipartFile>,
        val to: List<InternetAddress>,
        val from: InternetAddress,
        val subject: String,
        val html: String,
        val text: String)
