package org.security.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author newlife
 */
public final class Utils {

    private Utils() {
    }

    public static String randomPassword(final int maxLength) {

        final SecureRandom random = new SecureRandom();
        final int startChar = (int) '!';
        final int endChar = (int) '~';
        final int length = 6 + random.nextInt(maxLength + 1);

        return random.ints(length, startChar, endChar + 1)
                .mapToObj((i) -> (char) i)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public static boolean checkThenAgainstNow(String then) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime expiration = LocalDateTime.
                parse(then, DateTimeFormatter.ofPattern("dd-MMM-yyyy-HH:mm"));
        return now.isBefore(expiration);
    }

    public static boolean pingSMTPHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }
}
