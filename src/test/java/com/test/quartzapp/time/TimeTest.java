package com.test.quartzapp.time;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

public class TimeTest {

    @Test
    void test() {
        LocalTime now = LocalTime.now();
        System.out.println("now: " + now);
    }
}
