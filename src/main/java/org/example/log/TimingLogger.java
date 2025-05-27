package org.example.log;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class TimingLogger {
    private static final String LOG_FILE = "src\\main\\resources\\timing_logs.txt";

    public static void log(String message, long durationMillis) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write("[" + LocalDateTime.now() + "] " + message + ": " + durationMillis + " ms\n");
        } catch (IOException e) {
            System.out.println("Failed to write timing log: " + e.getMessage());
        }
    }

    public static void log(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(message);
        } catch (IOException e) {
            System.out.println("Failed to write timing log: " + e.getMessage());
        }
    }
}