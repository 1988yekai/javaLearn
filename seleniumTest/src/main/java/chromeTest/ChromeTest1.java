package chromeTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by yek on 2017-3-29.
 */
public class ChromeTest1 {
    public static void main(String[] args) {
        String driverPath = "tools/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driverPath);
        WebDriver driver = new ChromeDriver();
        try {
            driver.get("http://www.baidu.com");
            Thread.sleep(3000);
            String title = driver.getTitle();
            driver.quit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (WebDriverException e){
            System.out.println("driver error!");
        }
    }
}
