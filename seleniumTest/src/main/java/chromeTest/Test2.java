package chromeTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by yek on 2017-3-29.
 */
public class Test2 {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new ChromeThread());

        Thread thread2 =new Thread(new ChromeThread());
        Thread thread3 =new Thread(new ChromeThread());

        thread1.start();
        thread2.start();
        thread3.start();

        System.out.println(thread1.getName());
        System.out.println(thread2.getName());
        System.out.println(thread3.getName());
    }
}

class ChromeThread implements Runnable{
    public void run() {
        String driverPath = "tools/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driverPath);
        WebDriver driver = new ChromeDriver();
        try {
            driver.get("http://www.baidu.com");
            Thread.sleep(3000);
            String title = driver.getTitle();
            driver.quit();
            System.out.println(title);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (WebDriverException e){
//            System.exit(0);
        }
    }
}
