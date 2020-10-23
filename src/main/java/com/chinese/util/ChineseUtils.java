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

public abstract class ChineseUtils {

    private ChineseUtils() {
    }

    /**
     * Right fill with spaces or substring the GB18030 encoded Simplified Chinese text according to the required size.
     *
     * @param text The GB18030 encoded Simplified Chinese text.
     * @param requiredSize The required size.
     * @return The GB18030 encoded string with the required length.
     */
    public static String fixedSizeGB(String text, int requiredSize) {
        return ChineseTextUtils.fixedSize(text, requiredSize, LangCode.SIMPLIFIED.encoding());
    }

    /**
     * Right fill with spaces or substring the Big5 encoded Traditional Chinese text according to the required size.
     *
     * @param text The Big5 encoded Traditional Chinese text.
     * @param requiredSize The required length.
     * @return The Big5 encoded string with the required length.
     */
    public static String fixedSizeBig5(String text, int requiredSize) {
        return ChineseTextUtils.fixedSize(text, requiredSize, LangCode.TRADITIONAL.encoding());
    }

    /**
     * Convert any Traditional Chinese character to the correspondence Simplified Chinese character.
     *
     * @param text The input text.
     * @return The converted string.
     */
    public static String toGB(String text) {
        return replace(text, LangCode.TRADITIONAL);
    }

    /**
     * Convert any Simplified Chinese character to the correspondence Traditional Chinese character.
     *
     * @param text The input text.
     * @return The converted string.
     */
    public static String toBig5(String text) {
        return replace(text, LangCode.SIMPLIFIED);
    }

    private static int lookup(LangCode from, int codePoint) {
        WordPair[] wordPairs = null;
        if (from == LangCode.SIMPLIFIED) {
            wordPairs = Loader.simplified2Traditional();
        } else if (from == LangCode.TRADITIONAL) {
            wordPairs = Loader.traditional2Simplified();
        }
        if (wordPairs != null) {
            for (int i = 0, length = wordPairs.length; i < length; i++) {
                if (wordPairs[i].fromCodePoint() == codePoint) {
                    return wordPairs[i].toCodePoint();
                }
            }
        }
        return codePoint;
    }

    private static String replace(String text, LangCode from) {
        if (text != null && !text.isEmpty()) {
            char[] chars = text.toCharArray();
            int length = chars.length;
            StringBuilder str = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                int codePoint = Character.codePointAt(chars, i);
                if (isCJKUnifiedIdeographs(Character.UnicodeBlock.of(codePoint))) {
                    str.appendCodePoint(lookup(from, codePoint));
                } else {
                    str.appendCodePoint(codePoint);
                }
            }
            return str.toString();
        }
        return "";
    }

    private static boolean isCJKUnifiedIdeographs(Character.UnicodeBlock block) {
        return Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(block) ||
               Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A.equals(block) ||
               Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B.equals(block) ||
               Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C.equals(block) ||
               Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D.equals(block);
    }
}