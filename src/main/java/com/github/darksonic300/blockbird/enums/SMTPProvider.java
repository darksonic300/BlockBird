package com.github.darksonic300.blockbird.enums;

import joptsimple.internal.Strings;

public enum SMTPProvider {
    GMAIL("smtp.gmail.com", 587),
    OUTLOOK("smtp.office365.com", 587),
    YAHOO("smtp.mail.yahoo.com", 587),
    CUSTOM(Strings.EMPTY, 0);

    /**
     * Does not automatically add the "smtp" prefix
     */
    public final String host;
    /**
     * If null or 0 - defaults to 587
     */
    public final int port;

    SMTPProvider(String host, Integer port) {
        this.host = host;
        this.port = port == null || port == 0 ? 587 : port;
    }
}
