package com.chinese.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;

import static org.junit.Assert.assertEquals;

public class SimplifiedSizeTest extends BaseTest {

    private static String text;

    private static BufferedWriter writer;

    @BeforeClass
    public static void setup() throws FileNotFoundException {
        text = load("gb_text.txt", LangCode.SIMPLIFIED.encoding());
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("gb_text.log"), LangCode.SIMPLIFIED.encoding()));
    }

    @Test
    public void testSubstring() {
        String out = ChineseUtils.fixedSizeGB(text, 11);
        assertEquals(11, ChineseTextUtils.encodingSize(out, LangCode.SIMPLIFIED.encoding()));
        write("substring: " + out);
    }

    @Test
    public void testFill() {
        String out = ChineseUtils.fixedSizeGB(text, 30);
        assertEquals(30, ChineseTextUtils.encodingSize(out, LangCode.SIMPLIFIED.encoding()));
        write("fill     : " + out);
    }

    @Test
    public void testEqual() {
        String out = ChineseUtils.fixedSizeGB(text, 24);
        assertEquals(24, ChineseTextUtils.encodingSize(out, LangCode.SIMPLIFIED.encoding()));
        write("equal    : " + out);
    }

    private void write(String line) {
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    @AfterClass
    public static void destroy() {
        if (writer != null) {
            try {
                writer.close();
            } catch (Exception ignored) {
            }
        }
    }
}