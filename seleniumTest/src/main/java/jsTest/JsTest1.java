package jsTest;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by yek on 2017-2-15.
 */
public class JsTest1 {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "tools\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("http://172.16.1.91/mysite/test1.php");

        String js = //$.ajax({url:'http://172.16.1.91/mysite/test2.php',success:function(result){$('#div1').html(result);};alert($("#div1").html());
                "htmlobj=$.ajax({url:\"http://172.16.1.91/mysite/test2.php\",async:false});\n" +
                        "$(\"#div1\").html(htmlobj.responseText);";
        System.out.println(js);
        ((JavascriptExecutor) driver).executeScript(js);
        driver.quit();
    }
}
