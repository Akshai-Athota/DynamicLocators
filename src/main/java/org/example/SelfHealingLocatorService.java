package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

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

    public WebElement findElement(String locatorKey,String pageName) throws Exception {
        WebElement element = elementFinder.findElementParallel(LocatorUtils.getLocators(locatorKey, pageName), 30);

        if (element != null) return element;

        if (fetchedPages.contains(pageName)) {
            throw new NoSuchElementException("Locator not found for key: " + locatorKey + " in page: " + pageName + ", and re-fetch already performed.");
        }

        System.out.println("Fetching updated locators from ChatGPT for page: " + pageName);

        String html = driver.getPageSource();
        String locators = LocatorGenarator.genearte(html);
        LocatorWriter.writeToJson(locators, pageName);

        fetchedPages.add(pageName);

        element = elementFinder.findElementParallel(LocatorUtils.getLocators(locatorKey, pageName), 30);
        if (element == null) {
            throw new NoSuchElementException("Locator not found even after re-fetch for key: " + locatorKey + " in page: " + pageName);
        }

        return element;
    }
}
