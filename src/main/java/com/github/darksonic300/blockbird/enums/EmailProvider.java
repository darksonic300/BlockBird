package com.github.darksonic300.blockbird.enums;

import joptsimple.internal.Strings;

public enum EmailProvider {
    GMAIL("smtp.gmail.com", 587),
    OUTLOOK("smtp.office365.com", 587),
    YAHOO("smtp.mail.yahoo.com", 587),
    CUSTOM(Strings.EMPTY, 0);

    public final String host;
    public final int port;

    EmailProvider(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
