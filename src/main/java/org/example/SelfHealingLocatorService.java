package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.io.FileNotFoundException;
import java.util.*;

public class SelfHealingLocatorService {
    private static SelfHealingLocatorService instance;

    private WebDriver driver;
    private ElementFinder elementFinder;
    private final Set<String> fetchedPages = new HashSet<String>();

    private SelfHealingLocatorService(){

    }

    public static SelfHealingLocatorService  getInstance(){
        if(instance == null){
            synchronized (SelfHealingLocatorService.class){
                if(instance == null){
                    instance = new SelfHealingLocatorService();
                }
            }
        }

        return instance;
    }

    public void initDriver(WebDriver driver){
        this.driver = driver;
        this.elementFinder = new ElementFinder(driver);
    }

    public WebElement findElement(String locatorKey, String pageName) throws Exception {
        long start = System.currentTimeMillis();

        List<String> elements;
        WebElement element = null;
        try {
            elements = LocatorUtils.getLocators(locatorKey, pageName);
            element = elementFinder.findElementParallel(elements, 30);
        } catch (FileNotFoundException e) {
            System.out.println("JSON file not found for page: " + pageName + ". Will fetch from ChatGPT.");
        }

        if (element != null) {
            long duration = System.currentTimeMillis() - start;
            TimingLogger.log("Found element from existing JSON for key: " + locatorKey, duration);
            return element;
        }

        if (fetchedPages.contains(pageName)) {
            throw new NoSuchElementException("Locator not found for key: " + locatorKey + " in page: " + pageName + ", and re-fetch already performed.");
        }

        System.out.println("Fetching updated locators from ChatGPT for page: " + pageName);

        long fetchStart = System.currentTimeMillis();
        String html = driver.getPageSource();
        String locators = LocatorGenarator.genearte(html);
        LocatorWriter.writeToJson(locators, pageName);
        long fetchDuration = System.currentTimeMillis() - fetchStart;
        TimingLogger.log("Time to fetch from ChatGPT and write JSON for page: " + pageName, fetchDuration);

        fetchedPages.add(pageName);

        long retryStart = System.currentTimeMillis();
        element = elementFinder.findElementParallel(LocatorUtils.getLocators(locatorKey, pageName), 30);
        long retryDuration = System.currentTimeMillis() - retryStart;

        if (element == null) {
            throw new NoSuchElementException("Locator not found even after re-fetch for key: " + locatorKey + " in page: " + pageName);
        }

        long totalDuration = System.currentTimeMillis() - start;
        TimingLogger.log("Total time for self-healing for key: " + locatorKey, totalDuration);
        TimingLogger.log("Retry finding element after healing for key: " + locatorKey, retryDuration);

        return element;
    }

}
