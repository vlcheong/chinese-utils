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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;

abstract class Loader {

    private static final StampedLock s2tLock = new StampedLock();

    private static WordPair[] simplified2Traditional = null;

    private static final StampedLock t2sLock = new StampedLock();

    private static WordPair[] traditional2Simplified = null;

    private Loader() {
    }

    static WordPair[] simplified2Traditional() {
        if (simplified2Traditional == null) {
            long stamp = s2tLock.writeLock();
            try {
                if (simplified2Traditional != null) {
                    return simplified2Traditional;
                }
                simplified2Traditional = load("s2t.dat");
                return simplified2Traditional;
            } catch (IOException e) {
                throw new UncheckedIOException(e.getMessage(), e);
            } finally {
                s2tLock.unlockWrite(stamp);
            }
        }
        return simplified2Traditional;
    }

    static WordPair[] traditional2Simplified() {
        if (traditional2Simplified == null) {
            long stamp = t2sLock.writeLock();
            try {
                if (traditional2Simplified != null) {
                    return traditional2Simplified;
                }
                traditional2Simplified = load("t2s.dat");
                return traditional2Simplified;
            } catch (IOException e) {
                throw new UncheckedIOException(e.getMessage(), e);
            } finally {
                t2sLock.unlockWrite(stamp);
            }
        }
        return traditional2Simplified;
    }

    private static WordPair[] load(String source) throws IOException {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(source);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8), 8192)) {
            List<WordPair> pairs = new ArrayList<>(3000);
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("=");
                pairs.add(WordPair.builder().from(words[0]).to(words[1]).build());
            }
            return pairs.toArray(WordPair.EMPTY);
        }
    }
}