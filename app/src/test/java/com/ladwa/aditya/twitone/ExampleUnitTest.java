package com.ladwa.aditya.twitone;

import com.ladwa.aditya.twitone.util.Utility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void checkDate() throws Exception {
        long timeDifference = Utility.getTimeDifference("2016-07-13 14:57:16");
        System.out.println(timeDifference);
    }
}