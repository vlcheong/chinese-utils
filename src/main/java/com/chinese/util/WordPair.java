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

import java.io.Serializable;
import java.util.Objects;

final class WordPair implements Serializable {

    private static final long serialVersionUID = 1L;

    final static WordPair[] EMPTY = new WordPair[]{};

    private final String from;

    private final int fromCodePoint;

    private final String to;

    private final int toCodePoint;

    private WordPair(Builder builder) {
        this.from = builder.from;
        this.fromCodePoint = ChineseTextUtils.charToCodePoint(builder.from.toCharArray()[0]);
        this.to = builder.to;
        this.toCodePoint = ChineseTextUtils.charToCodePoint(builder.to.toCharArray()[0]);
    }

    static Builder builder() {
        return new Builder();
    }

    static final class Builder {
        private String from;

        private String to;

        private Builder() {
        }

        Builder from(String from) {
            this.from = from;
            return this;
        }

        Builder to(String to) {
            this.to = to;
            return this;
        }

        WordPair build() {
            return new WordPair(this);
        }
    }

    String from() {
        return from;
    }

    int fromCodePoint() {
        return fromCodePoint;
    }

    String to() {
        return to;
    }

    int toCodePoint() {
        return toCodePoint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromCodePoint);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        WordPair other = (WordPair) obj;
        return (fromCodePoint == other.fromCodePoint);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
            "[" +
                "from=" + from +
                ", fromCodePoint=" + fromCodePoint +
                ", to=" + to +
                ", toCodePoint=" + toCodePoint +
            "]";
    }
}