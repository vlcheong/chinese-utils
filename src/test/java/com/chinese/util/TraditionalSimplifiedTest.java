package com.chinese.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TraditionalSimplifiedTest extends BaseTest {

    @Test
    public void test() {
        setupConversion();
        assertEquals(simplified, ChineseUtils.toGB(traditional));
    }
}