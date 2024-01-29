package com.pintogether.backend.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

public final class RandomNicknameGenerator {

    public static String generateNickname() {

        int asciiFirst = 65;
        int asciiLast = 90;
        Integer[] exceptions = { 34, 39, 96 };

        List<Integer> exceptionsList = Arrays.asList(exceptions);
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int charIndex;
            do {
                charIndex = random.nextInt(asciiLast - asciiFirst + 1) + asciiFirst;
            } while (exceptionsList.contains(charIndex));

            builder.append((char) charIndex);
        }

        return builder.toString();

    }
}
