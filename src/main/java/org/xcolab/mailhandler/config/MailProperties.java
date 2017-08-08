package org.xcolab.mailhandler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("mail")
public class MailProperties {

    /**
     * A list of domains with their respective mappings.
     *
     * One mailhandler instance can handle several domains. All emails received at the user name
     * for a certain domain will be forwarded to all recipients. For example:
     *
     * mail:
     *  domains:
     *    - domain: example.com
     *      mappings:
     *      - admin:
     *          - admin1@example.com
     *          - admin2@example.com
     *
     */
    private List<Domain> domains;

    public List<Domain> getDomains() {
        return domains;
    }

    public void setDomains(List<Domain> domains) {
        this.domains = domains;
    }

    public static class Domain {

        private String domain;
        private List<Mapping> mappings;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public List<Mapping> getMappings() {
            return mappings;
        }

        public void setMappings(List<Mapping> mappings) {
            this.mappings = mappings;
        }
    }

    public static class Mapping {

        /**
         * Username for this mapping (in relation to the parent domain).
         */
        private String username;

        /**
         * Recipients for emails addressed to this username.
         */
        private List<String> recipients;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<String> getRecipients() {
            return recipients;
        }

        public void setRecipients(List<String> recipients) {
            this.recipients = recipients;
        }
    }

}
