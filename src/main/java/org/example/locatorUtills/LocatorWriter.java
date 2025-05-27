package org.example.locatorUtills;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;

public class LocatorWriter {
    static String pagesPath = "src\\main\\resources\\Pages\\";

    public static void writeToJson(String locators,String filePath){
        filePath = pagesPath+filePath+".json";

        try(FileWriter write = new FileWriter(filePath)){
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            Object json = gson.fromJson(locators, Object.class);
            gson.toJson(json,write);
            System.out.println("json saved to filepath :"+ filePath);
        }
        catch (Exception e){
            System.out.println("unable to open the file with given path :"+filePath);
        }

    }
}
