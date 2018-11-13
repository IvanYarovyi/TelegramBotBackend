package com.eone.bot.args;

public enum OPTION {
    TOKEN("token"),
    CERTIFICATE("certificate"),
    WEB_HOOK("web_hook"),
    SERVER_PORT("port");

    private final String val;

    OPTION(final String val){
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
