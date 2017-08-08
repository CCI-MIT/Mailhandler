package org.xcolab.mailhandler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("mail.spam")
public class SpamSettings {

    private final Warning warning = new Warning();

    private final Filter filter = new Filter();

    public Warning getWarning() {
        return warning;
    }

    public Filter getFilter() {
        return filter;
    }

    public static class Warning {

        /**
         * Show warnings when a message is considered spam by the filter.
         */
        private boolean enabled;

        /**
         * Threshold for showing warnings, if active.
         */
        private float threshold = 4.0f;

        /**
         * Show spam score when sending a message with a warning.
         */
        private boolean showScore = true;

        /**
         * Show spam report when sending a message with a warning.
         */
        private boolean showReport = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public float getThreshold() {
            return threshold;
        }

        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        public boolean isShowScore() {
            return showScore;
        }

        public void setShowScore(boolean showScore) {
            this.showScore = showScore;
        }

        public boolean isShowReport() {
            return showReport;
        }

        public void setShowReport(boolean showReport) {
            this.showReport = showReport;
        }
    }

    public static class Filter {

        /**
         * Don't forward messages messages above the filter threshold.
         */
        private boolean enabled;

        /**
         * Threshold for filtering messages, if active.
         */
        private float threshold = 5.0f;

        /**
         * Phrases that will lead to the message being filtered immediately.
         */
        private List<String> phrases = new ArrayList<>();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public float getThreshold() {
            return threshold;
        }

        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        public List<String> getPhrases() {
            return phrases;
        }

        public void setPhrases(List<String> phrases) {
            this.phrases = phrases;
        }
    }
}
