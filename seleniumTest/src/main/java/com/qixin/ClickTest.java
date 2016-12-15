package com.qixin;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * www.qixin.com test
 * Created by yek on 2016-8-24.
 */
public class ClickTest {

    public static void main(String[] args) {
        String proxyIpAndPort = "119.6.136.122:80";//"localhost:8080";
        DesiredCapabilities cap = new DesiredCapabilities();
        org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
        proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
        cap.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        cap.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
        System.setProperty("http.nonProxyHosts", "119.6.136.122");
        cap.setCapability(CapabilityType.PROXY, proxy);

        WebDriver driver = null;
        String baseUrl = "http://www.qixin.com/";
        ChromeDriverService service = null;
        try {



            System.setProperty("webdriver.chrome.driver", "F:\\JavaEE Lianxi\\chromedriver.exe");
//            // 创建一个 ChromeDriver 的接口，用于连接 Chrome
            service = new ChromeDriverService.Builder().usingDriverExecutable(new File("F:\\JavaEE Lianxi\\chromedriver.exe")).usingAnyFreePort().build();

            service.start();
//            // 创建一个 Chrome 的浏览器实例
//            driver = new RemoteWebDriver(service.getUrl(),
//                    DesiredCapabilities.chrome());
            driver = new ChromeDriver(service,cap);  //初始化浏览器
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

//            driver.manage().window().maximize(); // max size the chrome window

            driver.get(baseUrl + "company/20957626-2189-4ec0-aec3-47a0f9be6d49");
            WebElement closeEle = driver.findElement(By.id("close-download-panel"));
            System.out.println(closeEle.getAttribute("class"));
            try {
                if (closeEle != null)
                // Execute JavaScript
                ((JavascriptExecutor)driver).executeScript("window.isLogin = true;var a = document.getElementById('download-container');var _parentElement = a.parentNode;_parentElement.removeChild(a)");
//                ele.click();

//                closeEle.click();
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            driver.findElement(By.xpath("//ul[@id='mainTab']/li[2]/a/span")).click();
            WebElement TableEle = driver.findElement(By.xpath("//*[@id=\"lawsuits\"]/div[2]/table"));
            TableEle.findElement(By.xpath(".//tbody/tr[1]/td[4]/a")).click();
            driver.findElement(By.cssSelector("div.div-header > button.close")).click();
            driver.findElement(By.linkText("苏州工业园区人民法院")).click();
            driver.findElement(By.cssSelector("div.div-header > button.close")).click();
            driver.findElement(By.xpath("//div[@id='executionperson']/div[2]/div/div/span[3]")).click();
            driver.findElement(By.cssSelector("div.fr > a")).click();
            driver.findElement(By.cssSelector("div.div-header > button.close")).click();
            driver.findElement(By.linkText("知识产权999+")).click();
            driver.findElement(By.cssSelector("td.text-middle > a")).click();
            driver.findElement(By.cssSelector("div.div-header > button.close")).click();
            driver.findElement(By.xpath("//div[@id='trademark']/div[2]/div[2]/div/span[3]")).click();
            driver.findElement(By.linkText("一种门转轴组件")).click();
            driver.findElement(By.cssSelector("div.div-header > button.close")).click();
            driver.findElement(By.cssSelector("#patent > div.panel-body > div.pull-right > div.oni-pager > span.oni-pager-next")).click();
            driver.findElement(By.xpath("//div[@id='softwarecopyright']/div[2]/div/div/span[3]")).click();
            driver.findElement(By.xpath("(//a[contains(text(),'查看详情>>')])[6]")).click();
            driver.findElement(By.cssSelector("div.div-header > button.close")).click();
            driver.findElement(By.xpath("//ul[@id='mainTab']/li[6]/a/span")).click();

            System.out.println("关闭窗口中...");
            // 关闭 ChromeDriver 接口
            driver.quit();
            service.stop();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("错误 关闭窗口中...");
            // 关闭 ChromeDriver 接口
            if (driver != null)
                driver.quit();
            if (service != null)
                service.stop();
        }
    }
}
