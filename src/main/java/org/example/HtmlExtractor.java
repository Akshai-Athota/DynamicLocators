package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class HtmlExtractor {
    public static String extract(String url) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\chand\\IdeaProjects\\DynamicLocators\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        WebDriver driver = new ChromeDriver(options);
        driver.get(url);
        Thread.sleep(1000);
        String html = driver.getPageSource();
        driver.quit();
        return html;
    }
}
