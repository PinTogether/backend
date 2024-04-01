package com.pintogether.backend.util;

import java.security.SecureRandom;

public final class RandomMembernameGenerator {

     static String candidate = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

     public static String generate() {
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int idx = random.nextInt(62);
            builder.append(candidate.charAt(idx));
        }
        return builder.toString();
    }

}
