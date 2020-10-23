package com.chinese.util;

/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public abstract class ChineseTextUtils {

    private final static String UNICODE_PREFIX = "\\u";

    private final static char NULL = '\u0000';

    private final static int[] EMPTY_INT = new int[]{};

    private ChineseTextUtils() {
    }

    public static int charToCodePoint(char c) {
        return Character.codePointAt(new char[] {c}, 0);
    }

    public static int[] stringToCodePoints(String text) {
        if (text != null && !text.isEmpty()) {
            char[] chars = text.toCharArray();
            int length = chars.length;
            if (length == 1) {
                return new int[]{Character.codePointAt(chars, 0)};
            } else {
                int[] codePoints = new int[length];
                char high = NULL, low = NULL;
                int count = 0;
                for (int i = 0; i < length; i++) {
                    if (Character.isSurrogate(chars[i])) {
                        if (Character.isHighSurrogate(chars[i])) {
                            high = chars[i];
                        } else if (Character.isLowSurrogate(chars[i])) {
                            low = chars[i];
                        }
                        if (high != NULL && low != NULL) {
                            codePoints[count] = Character.toCodePoint(high, low);
                            high = NULL;
                            low = NULL;
                            count += 1;
                        }
                    } else {
                        codePoints[count] = Character.codePointAt(chars, i);
                        count += 1;
                    }
                }
                if (count != length) {
                    int[] points = new int[count];
                    System.arraycopy(codePoints, 0, points, 0, count);
                    return points;
                }
                return codePoints;
            }
        }
        return EMPTY_INT;
    }

    public static String fixedSize(String str, int requiredSize, Charset charset) {
        if (str == null || requiredSize <= 0) return "";
        byte[] strBytes = str.getBytes(charset);
        int strByteLength = strBytes.length;
        if (strByteLength == requiredSize) {
            return str;
        } else if (strByteLength < requiredSize) {
            byte[] bytes = new byte[requiredSize];
            System.arraycopy(strBytes, 0, bytes, 0, strByteLength);
            Arrays.fill(bytes, strByteLength, requiredSize, (byte) 0x20);
            return new String(bytes, charset);
        }
        char[] chars = str.toCharArray();
        byte[] bytes = new byte[requiredSize];
        int j = 0, remain = requiredSize;
        for (int i = 0; i < chars.length; i++) {
            byte[] cbytes = charToCompactedBytes(chars[i], charset);
            int blength = cbytes.length;
            if ((remain - blength) >= 0) {
                System.arraycopy(cbytes, 0, bytes, j, blength);
                j += blength;
                remain -= blength;
            } else {
                break;
            }
        }
        if (j < requiredSize) {
            Arrays.fill(bytes, j, requiredSize, (byte) 0x20);
        }
        return new String(bytes, charset);
    }

    public static byte[] charToBytes(char c, Charset charset) {
        return charset.encode(CharBuffer.wrap(new char[] {c})).array();
    }

    public static byte[] charToCompactedBytes(char c, Charset charset) {
        byte[] bytes = charToBytes(c, charset);
        int length = bytes.length;
        byte[] newBytes = new byte[length];
        int j = 0;
        for (int i = 0; i < length; i++) {
            if (bytes[i] != 0x00) {
                newBytes[j] = bytes[i];
                j += 1;
            } else {
                break;
            }
        }
        byte[] compacted = new byte[j];
        System.arraycopy(newBytes, 0, compacted, 0, compacted.length);
        return compacted;
    }

    public static String unicodeToString(String text) {
        if (text != null && !text.isEmpty()) {
            String[] characters = text.split("(?=\\" + UNICODE_PREFIX + ')');
            StringBuilder result = new StringBuilder();
            char high = NULL, low = NULL;
            for (int i = 0; i < characters.length; i++) {
                int codePoint = Integer.parseInt(characters[i].substring(2), 16);
                char ch = (char) codePoint;
                if (Character.isSurrogate(ch)) {
                    // If the specified code point is a BMP (Basic Multilingual Plane or Plane 0) value,
                    // the resulting char array has the same value as codePoint.
                    if (Character.isHighSurrogate(ch)) {
                        high = ch;
                    } else if (Character.isLowSurrogate(ch)) {
                        low = ch;
                    }
                    if (high != NULL && low != NULL) {
                        result.appendCodePoint(Character.toCodePoint(high, low));
                        high = NULL;
                        low = NULL;
                    }
                } else {
                    result.appendCodePoint(codePoint);
                }
            }
            return result.toString();
        }
        return "";
    }

    public static String stringToUnicode(String text) {
        if (text != null && !text.isEmpty()) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                result.append(convertCodePointToUnicode(Character.codePointAt(text, i)));
                if (Character.isHighSurrogate(text.charAt(i))) {
                    i++;
                }
            }
            return result.toString();
        }
        return "";
    }

    public static int encodingSize(String text, Charset charset) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return text.getBytes(charset).length;
    }

    private static String convertCodePointToUnicode(int codePoint) {
        String codePointHex = Integer.toHexString(codePoint);
        codePointHex = codePointHex.startsWith("0") ? codePointHex.substring(1) : codePointHex;
        StringBuilder result = new StringBuilder(6);
        result.append(UNICODE_PREFIX);
        int length = codePointHex.length();
        if (length < 4) {
            switch (length) {
                case 0:
                    result.append("0000");
                    break;
                case 1:
                    result.append("000");
                    break;
                case 2:
                    result.append("00");
                    break;
                case 3:
                    result.append('0');
                    break;
            }
        }
        return result.append(codePointHex).toString();
    }
}