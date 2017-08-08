package org.xcolab.mailhandler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mail.smtp")
public class SmtpProperties {

    /**
     * Hostname of the SMTP server.
     */
    private String host = "localhost";

    /**
     * Port of the SMTP server.
     */
    private int port = 8025;

    /**
     * User on the SMTP server, if any.
     */
    private String user;

    /**
     * Password for the user, if any.
     */
    private String pass;

    /**
     * Transport strategy for reaching the SMTP server.
     *
     * Default is plain SMTP, use TLS or SSL for more secure transport.
     */
    private String transport = "SMTP";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }
}
