package org.example.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocatorLogger {

    public static void logLocatorUpdate(String locatorKey, String pageName) {
        try (FileWriter log = new FileWriter("src/main/resources/locator-update-log/"+pageName+".txt", true)) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            log.write("[" + timestamp + "] Updated locators for key: " + locatorKey + ", page: " + pageName + "\n");

            File oldFile = new File("src/main/resources/Pages/" + pageName + "_old.json");
            File newFile = new File("src/main/resources/Pages/" + pageName + ".json");

            if (oldFile.exists()) {
                String oldContent = Files.readString(oldFile.toPath());
                log.write("Old: " + oldContent + "\n");
            }
            if (newFile.exists()) {
                String newContent = Files.readString(newFile.toPath());
                log.write("New: " + newContent + "\n");
            }
            log.write("------------------------------------------\n");
        } catch (IOException e) {
            System.out.println("Failed to write log: " + e.getMessage());
        }
    }
}
