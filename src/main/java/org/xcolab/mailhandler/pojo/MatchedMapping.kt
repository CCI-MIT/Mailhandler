package org.xcolab.mailhandler.pojo

import org.xcolab.mailhandler.config.MailProperties.Domain
import org.xcolab.mailhandler.config.MailProperties.Mapping

data class MatchedMapping(val domain: Domain, val mapping: Mapping) {

    fun getEmailAddress(): String {
        return mapping.username + "@" + domain.domain
    }
}
