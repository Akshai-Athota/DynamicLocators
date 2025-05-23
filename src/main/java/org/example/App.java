package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.NoSuchElementException;


public class App {

    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\chand\\IdeaProjects\\DynamicLocators\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        int counter =0;
        TestScreenRecorder.startRecording();
        String url = "https://practicetestautomation.com/practice-test-login/";
        driver.manage().window().maximize();
        driver.get(url);
        Thread.sleep(5000);
        String html = driver.getPageSource();

        System.out.println("sending request to chat gpt");

        String locators = LocatorGenarator.genearte(html);

        System.out.println("response received writing to json file");

        LocatorWriter.writeToJson(locators, "loginPage");

        System.out.println("json file completed");

        ElementFinder locatorFinder = new ElementFinder(driver);

        WebElement userNameInput = locatorFinder.findElementParallel(LocatorUtils.getLocators("usernameInput", "loginPage"),
                30);

        if(userNameInput == null){
            userNameInput = retry(driver,"usernameInput", "loginPage",counter);
            counter ++;
        }
        userNameInput.sendKeys("student");

        WebElement passwordInput = locatorFinder.findElementParallel(LocatorUtils.getLocators("passwordInput", "loginPage"),
                30);

        if(passwordInput == null){
            passwordInput = retry(driver,"passwordInput", "loginPage",counter);
            counter ++;
        }

        passwordInput.sendKeys("Password123");

        WebElement submitBtn = locatorFinder.findElementParallel(LocatorUtils.getLocators("submitButton", "loginPage"),
                30);

        if(submitBtn == null){
            submitBtn = retry(driver,"submitButton", "loginPage",counter);
            counter ++;
        }

        submitBtn.click();

        Thread.sleep(4000);

        TestScreenRecorder.stopRecording();

        driver.quit();

    }

    public static WebElement retry(WebDriver driver,String key,String filePath,int counter) throws Exception {
        if(counter != 0) throw new NoSuchElementException("None of the locators matched");
        String html = driver.getPageSource();

        System.out.println("sending request to chat gpt");

        String locators = LocatorGenarator.genearte(html);

        System.out.println("response received writing to json file");

        LocatorWriter.writeToJson(locators, "loginPage");

        System.out.println("json file completed");

        ElementFinder locatorFinder = new ElementFinder(driver);

        WebElement element=locatorFinder.findElementParallel(LocatorUtils.getLocators(key, filePath),
                30);

        if(element == null){
            throw new NoSuchElementException("None of the locators matched");
        }
        return element;
    }
}
