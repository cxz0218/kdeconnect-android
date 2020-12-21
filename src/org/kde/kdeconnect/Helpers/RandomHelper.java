package org.kde.kdeconnect.Helpers;


import java.security.SecureRandom;

/**
 * Randomize helper classe for security settings.
 */
public class RandomHelper {
    public static final SecureRandom secureRandom = new SecureRandom();

    private static final char[] symbols = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "1234567890").toCharArray();

    /**
     * Create a string randomly.
     *
     * @param length
     * @return
     */
    public static String randomString(int length) {
        char[] buffer = new char[length];
        for (int idx = 0; idx < length; ++idx) {
            buffer[idx] = symbols[secureRandom.nextInt(symbols.length)];
        }
        return new String(buffer);
    }

}
