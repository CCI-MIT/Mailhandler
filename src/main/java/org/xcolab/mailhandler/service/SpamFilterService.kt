package org.xcolab.mailhandler.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.xcolab.mailhandler.config.SpamSettings
import org.xcolab.mailhandler.pojo.ParsedEmail
import org.xcolab.mailhandler.pojo.SpamReport

@Service
@EnableConfigurationProperties(SpamSettings::class)
class SpamFilterService
@Autowired constructor(spamSettings: SpamSettings) {

    val filterThreshold = spamSettings.filter.threshold
    val warningThreshold = spamSettings.warning.threshold
    val isFilterEnabled = spamSettings.filter.isEnabled
    val isShowReportOnWarning = spamSettings.warning.isShowReport
    val isShowScoreOnWarning = spamSettings.warning.isShowScore
    val isWarningEnabled = spamSettings.warning.isEnabled
    val filterPhrases: List<String> = spamSettings.filter.phrases

    fun shouldFilterMessage(parsedEmail: ParsedEmail, spamReport: SpamReport): Boolean {
        return (isFilterEnabled && spamReport.score > filterThreshold)
                || filterPhrases.any { parsedEmail.html.contains(it) }
                || filterPhrases.any { parsedEmail.text.contains(it) }
                || filterPhrases.any { parsedEmail.subject.contains(it) }
    }

    fun formatEmailWithSpamReport(parsedEmail: ParsedEmail, spamReport: SpamReport): ParsedEmail {
        return parsedEmail.copy(
                subject = formatSubjectWithSpamReport(parsedEmail.subject, spamReport),
                html = formatBodyWithSpamReport(parsedEmail.html, true, spamReport),
                text = formatBodyWithSpamReport(parsedEmail.text, false, spamReport)
        )
    }

    private fun formatSubjectWithSpamReport(subject: String, spamReport: SpamReport): String {
        if (shouldShowSpamWarning(spamReport)) {
            return getWarning(spamReport) + subject;
        }
        return subject
    }

    private fun shouldShowSpamReport(spamReport: SpamReport): Boolean {
        return isShowReportOnWarning && shouldShowSpamWarning(spamReport)
    }

    private fun getWarning(spamReport: SpamReport): String {
        val s = "[Potential Spam] "
        if (isShowScoreOnWarning) {
            return s + "[SpamScore: ${spamReport.score}] "
        }
        return s
    }

    private fun formatBodyWithSpamReport(body: String, asHtml: Boolean,
                                         spamReport: SpamReport): String {
        if (shouldShowSpamReport(spamReport)) {
            return body + formatSpamReport(spamReport, asHtml)
        }
        return body
    }

    private fun shouldShowSpamWarning(spamReport: SpamReport): Boolean {
        return isWarningEnabled && spamReport.score > warningThreshold
    }

    private fun formatSpamReport(spamReport: SpamReport, asHtml: Boolean): String {
        val sb = StringBuilder()
        if (asHtml) {
            sb.append("<br/><br/>=======<br/><br/>")
        } else {
            sb.append("\n\n=======\n\n")
        }
        sb.append(spamReport)
        return sb.toString()
    }
}
