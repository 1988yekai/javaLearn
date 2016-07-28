package cookiesGet;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016-7-11.
 */
public class CookiesGet {

    @Test
    public void zgncpwLogin() throws Exception{
        WebDriver driver;
        String baseUrl;
        ChromeDriverService service = null;
        System.setProperty("webdriver.chrome.driver", "F:\\JavaEE Lianxi\\chromedriver.exe");
        // 创建一个 ChromeDriver 的接口，用于连接 Chrome
        service = new ChromeDriverService.Builder().usingDriverExecutable(new File("F:\\JavaEE Lianxi\\chromedriver.exe")).usingAnyFreePort().build();

        service.start();
        // 创建一个 Chrome 的浏览器实例
        driver = new RemoteWebDriver(service.getUrl(),
                DesiredCapabilities.chrome());

        baseUrl = "http://www.zgncpw.com/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        driver.get(baseUrl + "/member/login.php");
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("bellye123");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("z745839");
        driver.findElement(By.name("submit")).click();

        Thread.sleep(3000);

        File file = new File("D:\\zgncpwCookie.txt");
        file.delete();
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        for (Cookie ck : driver.manage().getCookies()) {
            bw.write(ck.getName() + ";" + ck.getValue() + ";"
                    + ck.getDomain() + ";" + ck.getPath() + ";"
                    + ck.getExpiry() + ";" + ck.isSecure());
            bw.newLine();
        }
        bw.flush();
        bw.close();
        fw.close();


        System.out.println("关闭窗口中...");
        driver.quit();
        // 关闭 ChromeDriver 接口
        service.stop();
    }


}
