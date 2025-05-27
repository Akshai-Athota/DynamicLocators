package org.example.locatorUtills;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BackUpLocators {

    public static void backupOldLocatorFile(String pageName) {
        try {
            File original = new File("src/main/resources/Pages/" + pageName + ".json");
            if (original.exists()) {
                File backup = new File("src/main/resources/Pages/" + pageName + "_old.json");
                Files.copy(original.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Backup created for: " + pageName);
            }
        } catch (IOException e) {
            System.out.println("Failed to backup file: " + e.getMessage());
        }
    }
}
