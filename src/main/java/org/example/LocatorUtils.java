package org.example;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LocatorUtils {
    static String pagesPath = "src\\main\\resources\\Pages\\";
    public static List<String> getLocators(String key,String filePath) throws FileNotFoundException {
        filePath = pagesPath+filePath+".json";
        Gson gson = new Gson();
        Reader reader = new FileReader(filePath);
        Type type = new TypeToken<Map<String,List<String>>>(){}.getType();
        Map<String,List<String>> locators = gson.fromJson(reader,type);
        return locators.getOrDefault(key, Collections.emptyList());
    }
}
