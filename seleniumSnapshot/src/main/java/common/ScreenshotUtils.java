package common;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.Augmenter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by yek on 2017-1-6.
 */
public class ScreenshotUtils {
    public static void snapshot(TakesScreenshot drivername, String filename) {
        // this method will take screen shot ,require two parameters ,one is driver name, another is file name


        File scrFile = drivername.getScreenshotAs(OutputType.FILE);
        // Now you can do whatever you need to do with it, for example copy somewhere
        try {
            System.out.println("save snapshot path is:" + new File("").getAbsolutePath() + "/" + filename);
            FileUtils.copyFile(scrFile, new File(filename));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Can't save screenshot");
            e.printStackTrace();
        } finally {
            System.out.println("screen shot finished");
        }
    }

    public static void snapshot2(WebDriver drivername, String filename) {
        // this method will take screen shot ,require two parameters ,one is driver name, another is file name


        //  File scrFile = drivername.getScreenshotAs(OutputType.FILE);
        // Now you can do whatever you need to do with it, for example copy somewhere
        try {
            WebDriver augmentedDriver = new Augmenter().augment(drivername);
            File screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
            File file = new File(filename);
            System.out.println("save snapshot path is:" + new File("").getAbsolutePath() + "/" + filename);
            FileUtils.copyFile(screenshot, file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Can't save screenshot");
            e.printStackTrace();
        } finally {
            System.out.println("screen shot finished");
        }
    }

    public static byte[] takeScreenshot(WebDriver driver) throws IOException {
        WebDriver augmentedDriver = new Augmenter().augment(driver);
        return ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);
        //TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        //return takesScreenshot.getScreenshotAs(OutputType.BYTES);
    }

    public static BufferedImage createElementImage(WebDriver driver, WebElement webElement) throws IOException {
        // 获得webElement的位置和大小。
        Point location = webElement.getLocation();
        Dimension size = webElement.getSize();
        // 创建全屏截图。
        BufferedImage originalImage =
                ImageIO.read(new ByteArrayInputStream(takeScreenshot(driver)));
        // 截取webElement所在位置的子图。
        BufferedImage croppedImage = originalImage.getSubimage(
                location.getX(),
                location.getY(),
                size.getWidth(),
                size.getHeight());
        return croppedImage;
    }

    public static boolean takeElementPng(WebDriver driver, WebElement webElement, File file){
        boolean flag = false;

        try {
            ImageIO.write(createElementImage(driver, webElement), "png", file);
            flag = true;
        } catch (IOException e) {
            System.out.println("Can't save image.");
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean takeElementJpeg(WebDriver driver, WebElement webElement, File file){
        boolean flag = false;

        try {
            ImageIO.write(createElementImage(driver, webElement), "jpeg", file);
            flag = true;
        } catch (IOException e) {
            System.out.println("Can't save image.");
            e.printStackTrace();
        }
        return flag;
    }
}
