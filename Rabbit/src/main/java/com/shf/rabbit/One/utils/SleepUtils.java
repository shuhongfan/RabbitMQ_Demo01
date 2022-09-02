package com.shf.rabbit.One.utils;

import lombok.SneakyThrows;

public class SleepUtils {
    @SneakyThrows
    public static void sleep(int second) {
        Thread.sleep(1000 * second);
    }
}
