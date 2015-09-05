package com.coderyuan.yyframework.utils;

import java.util.Random;

public class RandomKeyUtil {

    private static final String BASE = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateNumber(int len) {
        StringBuilder builder = new StringBuilder();
        int[] defaultNumbers = new int[10];
        for (int i = 0; i < defaultNumbers.length; i++) {
            defaultNumbers[i] = i;
        }
        Random random = new Random();
        int[] numbers = new int[len];
        int canBeUsed = 10;
        for (int i = 0; i < numbers.length; i++) {
            int index = random.nextInt(canBeUsed);
            numbers[i] = defaultNumbers[index];
            swap(index, canBeUsed - 1, defaultNumbers);
            canBeUsed--;
        }
        if (numbers.length > 0) {
            for (int num : numbers) {
                builder.append(num);
            }
        }
        return builder.toString();
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(BASE.length());
            sb.append(BASE.charAt(number));
        }
        return sb.toString();
    }

    private static void swap(int i, int j, int[] num) {
        int temp = num[i];
        num[i] = num[j];
        num[j] = temp;
    }
}
