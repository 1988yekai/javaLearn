package qz.bigdata.crawler.core;

import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import qz.bigdata.crawler.browser.*;
/**
 * Created by fys on 2015/1/8.
 */
public interface IBrowserAction {
    void maximize();
    void setSize(int width, int height);
    void visit(String url);
    void goBack();
    void goForward();
    void refresh();
    void setImplicitlyWaitTimeout(long timeout, TimeUnit unit);
    void setScriptTimeout(long timeout, TimeUnit unit);
    void setPageLoadTimeout(long timeout, TimeUnit unit);
    void sleep(int milliSeconds);
    void waitEle(String locateType, String locateId, long milliSeconds);
    void acceptAlert();
    void dismissAlert();
    void runJavaScript(String jsCommand);
    void executeAction(String locateType, String locateId, UserAction action);
    void click(String locateType, String locateId);
    void input(String locateType, String locateId, String value);
    void inputSubmit(String locateType, String locateId, String value);
    WebDriver switchWindow(String keyOfTitle);
    void switchBack();
    void pressKey(String locateType, String locateId, org.openqa.selenium.Keys key);
    void press(String locateType, String locateId, String key);
    void selectByText(String locateType, String locateId, String optionText);
    void selectByIndex(String locateType, String locateId, int index);
    void selectByValue(String locateType, String locateId, String value);
    void takeScreenshot(String filename) throws IOException;
    void takeScreenshot(String locateType, String locateId, String filename)throws IOException;
}
