package com.chinese.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimplifiedTraditionalTest extends BaseTest {

    @Test
    public void test() {
        setupConversion();
        assertEquals(traditional, ChineseUtils.toBig5(simplified));
    }
}