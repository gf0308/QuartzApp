package com.test.quartzapp.time;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class TimeTest {

    @Test
    void test() {
        LocalDate now = LocalDate.now();
        LocalDateTime now1 = LocalDateTime.now();
        LocalTime now2 = LocalTime.now();

        System.out.println(now);  //        2023-03-13
        System.out.println(now1); //        2023-03-13T11:18:43.654067
        System.out.println(now2); //        11:18:43.654067
        System.out.println("Date: " + new Date());
    }
}
