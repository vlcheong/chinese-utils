package com.chinese.util;

import java.nio.charset.Charset;

public enum LangCode {
    SIMPLIFIED(Charset.forName("GB18030")),
    TRADITIONAL(Charset.forName("Big5"));

    private final Charset encoding;

    private LangCode(Charset encoding) {
        this.encoding = encoding;
    }

    public Charset encoding() {
        return encoding;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "[" +
                    "name=" + name() +
                    ", encoding=" + encoding +
                "]";
    }
}