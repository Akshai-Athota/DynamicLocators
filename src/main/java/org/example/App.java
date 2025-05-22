package org.example;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws InterruptedException, IOException {
        String url ="https://practicetestautomation.com/practice-test-login/";
        String html = HtmlExtractor.extract(url);
        String locators = LocatorGenarator.genearte(html);
        System.out.println(locators);
    }
}
