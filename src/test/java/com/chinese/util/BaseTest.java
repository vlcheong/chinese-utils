package com.chinese.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

public abstract class BaseTest {

    String simplified;

    String traditional;

    static String load(String source, Charset charset) {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(source);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset), 8192)) {
            String line = null;
            StringBuilder str = new StringBuilder(100);
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            return str.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    void setupConversion() {
        simplified = load("simplified.txt", LangCode.SIMPLIFIED.encoding());
        traditional = load("traditional.txt", LangCode.TRADITIONAL.encoding());
    }
}