package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

public class ElementFinder {

    private WebDriver driver;

    public ElementFinder(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement findElementParallel(List<String> locatorStrings, long timeoutInSeconds) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(locatorStrings.size());
        CompletionService<WebElement> completionService = new ExecutorCompletionService<>(executor);

        for (String locatorStr : locatorStrings) {
            completionService.submit(() -> {
                By locator = parseLocator(locatorStr);
                try {
                    FluentWait<WebDriver> wait = new FluentWait<>(driver)
                            .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                            .pollingEvery(Duration.ofMillis(500))
                            .ignoring(NoSuchElementException.class)
                            .ignoring(StaleElementReferenceException.class);

                    return wait.until(d -> d.findElement(locator));
                } catch (Exception ignored) {
                    return null;
                }
            });
        }

        WebElement foundElement = null;
        long endTime = System.currentTimeMillis() + timeoutInSeconds * 1000;
        while (System.currentTimeMillis() < endTime) {
            Future<WebElement> future = completionService.poll(1, TimeUnit.SECONDS);
            if (future != null) {
                WebElement result = future.get();
                if (result != null) {
                    foundElement = result;
                    break;
                }
            }
        }

        executor.shutdownNow();

        return foundElement;
    }

    private By parseLocator(String locatorStr) {
        if (locatorStr.startsWith("xpath=")) {
            return By.xpath(locatorStr.substring(6));
        } else if (locatorStr.startsWith("css=")) {
            return By.cssSelector(locatorStr.substring(4));
        } else if (locatorStr.startsWith("id=")) {
            return By.id(locatorStr.substring(3));
        } else if (locatorStr.startsWith("name=")) {
            return By.name(locatorStr.substring(5));
        } else if (locatorStr.startsWith("className=")) {
            return By.className(locatorStr.substring(10));
        } else if (locatorStr.startsWith("tagName=")) {
            return By.tagName(locatorStr.substring(8));
        } else {
            throw new IllegalArgumentException("Unsupported locator format: " + locatorStr);
        }
    }
}
