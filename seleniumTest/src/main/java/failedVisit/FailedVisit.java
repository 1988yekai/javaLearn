package failedVisit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Created by yek on 2017-1-11.
 */
public class FailedVisit {
    public static void main(String[] args) {
        /*System.setProperty("webdriver.chrome.driver", "F:\\JavaEE Lianxi\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        String url = "http://222.209.207.22/";
        driver.get(url);
        System.out.println(driver.getTitle());// 无法访问 url(http://222.209.207.22/)
        System.out.println("==============================================================");
//        System.out.println(driver.getPageSource());

        driver.close();*/

        WebDriver driver = new HtmlUnitDriver();
        String url = "http://www.baidu.com/";
        try {
            driver.get(url);
            System.out.println(driver.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("over");
        driver.close();


    }
}
