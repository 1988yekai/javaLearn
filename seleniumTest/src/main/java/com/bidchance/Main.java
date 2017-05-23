package com.bidchance;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Created by yek on 2017-2-21.
 */
public class Main {
    public static void main(String[] args) {
        WebDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_45,false);
        System.out.println(HttpClientBuilder.class.getProtectionDomain().getCodeSource().getLocation());
//        driver.get("http://www.baidu.com");
        driver.get("http://172.16.1.91/3.html");
        WebElement dd = driver.findElement(By.id("infohtmlcon"));
        String article = dd.getText();
        article = article.replaceAll("[\n]","__");
        System.out.println(article);
        article = article.replaceAll("(__)+","\n").trim();

        article = article.replaceAll("\\n \\n \\n","\n").replaceAll("\\n \\n","\n").trim();
        System.out.println(article);
    }
}
