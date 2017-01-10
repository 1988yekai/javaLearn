import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import common.HtmlUnitDriverChild;
import common.ScreenshotUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yek on 2017-1-6.
 */
public class Main {

    public static void main(String[] args) {
// =============================       chrome     ========================
//        System.setProperty("webdriver.chrome.driver", "F:\\project\\sc_egov_crawler_update\\tools\\chromedriver.exe");
//
//        WebDriver driver = new ChromeDriver();
//        WebElement imgElement = driver.findElement(By.xpath("//div[@id='lg']/img"));

//        ScreenshotUtils.snapshot2(driver,"baidu.png");
//        ScreenshotUtils.takeElementPng(driver,imgElement,new File("img.png"));
//        ScreenshotUtils.takeElementJpeg(driver,imgElement,new File("img.jpg"));
// =============================       chrome     ========================

        WebDriver driver = new HtmlUnitDriverChild(BrowserVersion.FIREFOX_45, false);
        WebClient client = ((HtmlUnitDriverChild) driver).getWebClient();
        client.getOptions().setCssEnabled(false);
        try {
            HtmlPage page = client.getPage("http://tieba.baidu.com/p/4860906335");
//            System.out.println(driver.getPageSource());
//        WebElement imgElement = driver.findElement(By.xpath("//div[@id='lg']/img"));
            HtmlImage valiCodeImg = (HtmlImage) page.getByXPath("//img").get(0);
            valiCodeImg.saveAs(new File("123.jpg"));

            ImageReader imageReader = valiCodeImg.getImageReader();
            BufferedImage bufferedImage = imageReader.read(0);
            ImageIO.write(bufferedImage, "jpeg", new File("11Img.jpg"));

            JFrame frame = new JFrame();
            JLabel l = new JLabel();
            l.setIcon(new ImageIcon(bufferedImage));
            frame.getContentPane().add(l);
            frame.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
            frame.setTitle("验证码");
            frame.setVisible(true);

            String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");

            System.out.println("输入的验证码为："+valicodeStr);
            frame.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can not save.");
        }

        driver.quit();
        System.exit(0);
    }
}
