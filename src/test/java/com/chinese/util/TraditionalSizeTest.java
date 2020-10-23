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

public class TraditionalSizeTest extends BaseTest {

    private static String text;

    private static BufferedWriter writer;

    @BeforeClass
    public static void setup() throws FileNotFoundException {
        text = load("big5_text.txt", LangCode.TRADITIONAL.encoding());
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("big5_text.log"), LangCode.TRADITIONAL.encoding()));
    }

    @Test
    public void testSubstring() {
        String out = ChineseUtils.fixedSizeBig5(text, 13);
        assertEquals(13, ChineseTextUtils.encodingSize(out, LangCode.TRADITIONAL.encoding()));
        write("substring: " + out);
    }

    @Test
    public void testFill() {
        String out = ChineseUtils.fixedSizeBig5(text, 30);
        assertEquals(30, ChineseTextUtils.encodingSize(out, LangCode.TRADITIONAL.encoding()));
        write("fill     : " + out);
    }

    @Test
    public void testEqual() {
        String out = ChineseUtils.fixedSizeBig5(text, 24);
        assertEquals(24, ChineseTextUtils.encodingSize(out, LangCode.TRADITIONAL.encoding()));
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