package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.NoSuchElementException;


public class App {

    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\chand\\IdeaProjects\\DynamicLocators\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        String url = "https://practicetestautomation.com/practice-test-login/";
        driver.manage().window().maximize();
        driver.get(url);
        Thread.sleep(5000);

        SelfHealingLocatorService locatorFinder = SelfHealingLocatorService.getInstance();
        locatorFinder.initDriver(driver);

        WebElement userNameInput = locatorFinder.findElement("usernameInput", "loginPage");
        userNameInput.sendKeys("student");

        WebElement passwordInput = locatorFinder.findElement("passwordInput", "loginPage");
        passwordInput.sendKeys("Password123");

        WebElement submitBtn = locatorFinder.findElement("submitButton","loginPage");
        submitBtn.click();

        driver.quit();

    }


}
