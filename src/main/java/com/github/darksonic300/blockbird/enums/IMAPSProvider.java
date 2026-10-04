package com.github.darksonic300.blockbird.enums;

public enum IMAPSProvider {
    GMAIL("imap.gmail.com", 993),
    OUTLOOK("imap.office365.com", 993),
    YAHOO("imap.mail.yahoo.com", 993),
    CUSTOM("", 993);

    /**
     * Does not automatically add the "smtp" prefix
     */
    public final String host;
    /**
     * If <= 0 - defaults to 993
     */
    public final int port;

    IMAPSProvider(String host, int port) {
        this.host = host;
        this.port = port <= 0 ? 993 : port;
    }
}
