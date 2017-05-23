package qz.bigdata.crawler.browser;

import com.gargoylesoftware.htmlunit.WebClient;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by fys on 2015/1/8.
 */
public class Browser {
    private WebDriver webdriver;
    private WebElement currentWebElement;
    private String mainWindowHandle = "";
    public String previousUrl;
    public String url;
    private int pageLoadTimeoutSeconds = 30;
    private int scriptTimeoutSeconds = 10;
    private int implicitlyWaitTimeoutSeconds = 10;

//    public Browser()
//    {
//        //System.setProperty("webdriver.chrome.driver","C:\\Users\\fys\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");
//        //this.webdriver = new FirefoxDriver();
//        System.setProperty("webdriver.chrome.driver", "C:\\Users\\fys\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
//        this.webdriver = new ChromeDriver();
//        //this.webdriver = new HtmlUnitDriver();
//        this.webdriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
//        this.webdriver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
//        this.webdriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
//
//    }
//    public Browser(WebDriver driver)
//    {
//        if(driver != null)
//        {
//            this.webdriver = driver;
//        }
//        else
//        {
//            this.webdriver = new HtmlUnitDriver();
//        }
//        this.webdriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
//    }
    public Browser(BrowserType bt, String driverPath)
    {
        this.InitWebDriver(bt, driverPath);
        this.webdriver.manage().timeouts().pageLoadTimeout(pageLoadTimeoutSeconds, TimeUnit.SECONDS);
        this.webdriver.manage().timeouts().setScriptTimeout(scriptTimeoutSeconds, TimeUnit.SECONDS);
        this.webdriver.manage().timeouts().implicitlyWait(implicitlyWaitTimeoutSeconds, TimeUnit.SECONDS);
    }

    public void InitWebDriver(BrowserType bt, String driverPath)
    {
        if(BrowserType.Htmlunit == bt)
        {
            this.webdriver = new HtmlUnitDriver();
        }
        else if(BrowserType.Chrome == bt)
        {
            System.setProperty("webdriver.chrome.driver", driverPath);//"C:\\Users\\fys\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
            this.webdriver = new ChromeDriver();
        }
        else if(BrowserType.Firefox == bt)
        {
            System.setProperty("webdriver.firefox.bin", driverPath);//"C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
            WebDriver webDriver = new FirefoxDriver();
        }
        else if(BrowserType.Ie == bt)
        {
            System.setProperty("webdriver.ie.driver",driverPath);//"C:\\Users\\yren9\\workspace\\selenium\\IEDriverServer.exe");
            DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
            ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
            this.webdriver = new InternetExplorerDriver(ieCapabilities);
        }
        else if (BrowserType.PhantomJS == bt) {
                System.setProperty("phantomjs.binary.path", driverPath);
                DesiredCapabilities dcap = new DesiredCapabilities();
//                String[] phantomArgs =new String[]{
//                        "--load-images=false",
//                        "--webdriver-loglevel=NONE"
//                };
                String[] phantomArgs =new String[]{
                        "--load-images=false",

                };
            //selenium 3 有 PhantomJSDriver
                //dcap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,phantomArgs);//不加载图片和log
//                PhantomJSDriver driver = new PhantomJSDriver(dcap);
                //this.webdriver = new PhantomJSDriver(dcap);
        }
        else if(BrowserType.None == bt)
        {
            //todo:抛出异常？
            this.webdriver = null;
        }
    }

    public void setPageLoadTimeoutSeconds(int waitSeconds){
        this.pageLoadTimeoutSeconds = waitSeconds;
        this.webdriver.manage().timeouts().pageLoadTimeout(this.pageLoadTimeoutSeconds, TimeUnit.SECONDS);
    }
    public void setScriptTimeoutSeconds(int waitSeconds){
        this.scriptTimeoutSeconds = waitSeconds;
        this.webdriver.manage().timeouts().pageLoadTimeout(this.scriptTimeoutSeconds, TimeUnit.SECONDS);
    }
    public void setImplicitlyWaitTimeoutSeconds(int waitSeconds){
        this.implicitlyWaitTimeoutSeconds = waitSeconds;
        this.webdriver.manage().timeouts().pageLoadTimeout(this.implicitlyWaitTimeoutSeconds, TimeUnit.SECONDS);
    }

    public WebDriver getWebDriver()
    {
        return this.webdriver;
    }
    public void setWebDriver(WebDriver driver)
    {
        this.webdriver = driver;
    }
    public void maximize(){
        this.webdriver.manage().window().maximize();
    }
    public void scrollToBottom(){
        BrowserUtil.scrollToBottom(this.webdriver);
    }
    public void setSize(int width, int height){
        Dimension dimension = new Dimension(width, height);
        this.webdriver.manage().window().setSize(dimension);
    }
    //methods of IWebPageReader
    public String getEleText(String locateType, String locateId)
    {
        WebElement ele = this.find(locateType, locateId);
        if(ele != null) {
            return ele.getText();
        }
        return null;
    }
    public String getEleText(String locateType, String locateId,String defaultValue)
    {
        WebElement ele = this.find(locateType, locateId);
        if(ele != null) {
            return ele.getText();
        }
        return defaultValue;
    }
    public List<String> getEleTexts(String locateType, String locateId)
    {
        List<WebElement> eles = this.finds(locateType, locateId);
        if(eles !=null) {
            List<String> eleTexts = new ArrayList<String>();
            for (WebElement ele : eles) {
                eleTexts.add(ele.getText());
            }
            return eleTexts;
        }
        return null;
    }
    public String getEleAttribute(String locateType, String locateId, String attributeName)
    {
        WebElement ele = this.find(locateType, locateId);
        if(ele != null) {
            return ele.getAttribute(attributeName);
        }
        return null;
    }
    public List<String> getEleAttributes(String locateType, String locateId, String attributeName)
    {
        List<WebElement> eles = this.finds(locateType, locateId);
        if(eles !=null) {
            List<String> eleTexts = new ArrayList<String>();
            for (WebElement ele : eles) {
                eleTexts.add(ele.getAttribute(attributeName));
            }
            return eleTexts;
        }
        return null;
    }
    public String getUrl()
    {
        return webdriver.getCurrentUrl();
    }
    public String getTitle()
    {
        return webdriver.getTitle();
    }
    public String getPage()
    {
        return webdriver.getPageSource();
    }
    public List<String> getWindowTitles()
    {
        this.mainWindowHandle = webdriver.getWindowHandle();
        WebDriver popup = null;
        List<String> windowTitles = new ArrayList<String>();
        for (String win : webdriver.getWindowHandles())
        {
            popup = webdriver.switchTo().window(win);
            windowTitles.add(popup.getTitle());
        }
        webdriver.switchTo().window(this.mainWindowHandle);
        return windowTitles;
    }
    public void takeScreenshot(String filename)
            throws IOException
    {
        File screenShotFile = ((TakesScreenshot)this.webdriver).getScreenshotAs(OutputType.FILE);
        //ss.SaveAsFile(this.outputFolder + "\\" + filename + "_" + DateTime.Now.ToLongTimeString().Replace(':','_') + ".png", System.Drawing.Imaging.ImageFormat.Png);
        FileUtils.copyFile(screenShotFile, new File(filename + ".png"));
    }
    public void takeScreenshot(String locateType, String locateId, String filename)
            throws IOException
    {
        WebElement ele = this.find(locateType, locateId);
        if(ele != null) {
            Point point = ele.getLocation();
            Dimension size = ele.getSize();

        /*
        Screenshot ss = ((ITakesScreenshot)this.webdriver).GetScreenshot();
        byte[] bytes = ss.AsByteArray;
        System.Drawing.Bitmap image = new System.Drawing.Bitmap(new System.IO.MemoryStream(bytes));

        Bitmap part = ImageHandler.Crop(image, point.X, point.Y, size.Width, size.Height);
        part.Save(this.outputFolder + "\\" + filename + "_" + DateTime.Now.ToLongTimeString().Replace(':', '_') + ".png", System.Drawing.Imaging.ImageFormat.Png);
        */
        }
    }

    //methods of IWebPageAction
    public void visit(String url)
    {
        if (!url.startsWith("http"))
        {
            url = "http://" + url;
        }
        String temp = url;
        webdriver.navigate().to(url);
        this.previousUrl = temp;
        this.url = url;
        System.out.println((new Date()).toLocaleString() + " visit " + url);
    }
    public void goBack()
    {
        webdriver.navigate().back();
    }
    public void goForward()
    {
        webdriver.navigate().forward();
    }
    public void refresh()
    {
        webdriver.navigate().refresh();
    }
    public void setImplicitlyWaitTimeout(long timeout, TimeUnit unit)
    {
        webdriver.manage().timeouts().implicitlyWait(timeout, unit);
    }
    public void setScriptTimeout(long timeout, TimeUnit unit)
    {
        webdriver.manage().timeouts().setScriptTimeout(timeout, unit);
    }
    public void setPageLoadTimeout(long timeout, TimeUnit unit)
    {
        webdriver.manage().timeouts().pageLoadTimeout(timeout, unit);
    }

    //使用htmlunit提供的WebClient来获得页面的返回信息
    public static int getStatusCode(String url) throws IOException {
        WebClient webClient = new WebClient();
        int code = webClient.getPage(url).getWebResponse().getStatusCode();
        webClient.close();
        return code;
    }

    public void sleep(int milliSeconds)
    {
        try {
            Thread.currentThread().sleep(milliSeconds);
        }
        catch(Exception e)
        {
        }
    }
    public void waitEle(String locateType, String locateId, long timeOutInSeconds)
    {
        this.webdriver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(this.webdriver, timeOutInSeconds);
        //todo: wait.until(ExpectedConditions.elementExists(BrowserUtil.GetBy(this.webdriver, null, locateType, locateId)));
    }
    public void acceptAlert()
    {
        Alert alert = webdriver.switchTo().alert();
        if(alert != null)
            alert.accept();
    }
    public void dismissAlert()
    {
        Alert alert = webdriver.switchTo().alert();
        if (alert != null)
            alert.dismiss();
    }
    public void runJavaScript(String jsCommand)
    {
        ((JavascriptExecutor)webdriver).executeScript(jsCommand);
    }
    public void executeAction(String locateType, String locateId, UserAction action)
    {
        WebElement ele = this.find(locateType, locateId);
        Actions ac = new Actions(webdriver);
        switch(action)
        {
            case ClickAndHold:
                ac.clickAndHold(ele);
                break;
            case ContextClick:
                ac.contextClick(ele);
                break;
            case DoubleClick:
                ac.doubleClick(ele);
                break;
            default: //UserAction.Click
                ac.click(ele);
                break;
        }
    }
    public void click(String locateType, String locateId)
    {
        WebElement ele = this.find(locateType, locateId);
        if(ele!=null) {
            ele.click();
            this.currentWebElement = ele; //needed?
        }
    }
    public void input(String locateType, String locateId, String value)
    {
        WebElement ele = this.find(locateType, locateId);
        if(ele != null) {
            ele.clear();
            ele.sendKeys(value);
            this.currentWebElement = ele;
        }
    }
    public void inputSubmit(String locateType, String locateId, String value)
    {
        WebElement ele = this.find(locateType, locateId);
        if(ele != null) {
            ele.clear();
            ele.sendKeys(value);
            ele.sendKeys(Keys.RETURN);
            this.currentWebElement = ele;
        }
    }
    public WebDriver switchWindow(String keyOfTitle)
    {
        this.mainWindowHandle = webdriver.getWindowHandle();
        WebDriver popup = null;
        Boolean found = false;
        for(String win : webdriver.getWindowHandles())
        {
            popup = webdriver.switchTo().window(win);
            if (popup.getTitle().contains(keyOfTitle))// i Equals(window, StringComparison.InvariantCultureIgnoreCase))
            {
                found = true;
                break;
            }
        }
        if (found)
        {
            webdriver = popup;
            return popup;
        }
        else
        {
            webdriver.switchTo().window(this.mainWindowHandle);
            return webdriver;
        }
    }
    public void switchBack()
    {
        this.webdriver = webdriver.switchTo().window(this.mainWindowHandle);
    }
    //key必须为OpenQA.Selenium.Keys中定义的静态字符串常量，代表用户按下的按键。
    public void pressKey(String locateType, String locateId, org.openqa.selenium.Keys key)
    {
        WebElement ele = this.find(locateType, locateId);
        if(ele!=null) {
            //String pk = this.getKey(key);
            this.currentWebElement = ele;
            this.currentWebElement.sendKeys(key);
        }
    }
    //key为用户要输入的字符串，而不是用户按下的按键
    public void press(String locateType, String locateId, String key)
    {
        WebElement ele = this.find(locateType, locateId);
        if(ele!=null){
            this.currentWebElement = ele;
            this.currentWebElement.sendKeys(key);
        }
    }
    public void selectByText(String locateType, String locateId, String optionText)
    {
        WebElement findEle = this.find(locateType, locateId);
        if(findEle!=null) {
            this.currentWebElement = findEle;
            if (this.currentWebElement.getTagName() == "select") {
                Select select = new Select(this.currentWebElement);
                select.selectByVisibleText(optionText);
            } else if (this.currentWebElement.getTagName() == "ul") {
                List<WebElement> eles = this.currentWebElement.findElements(By.tagName("li"));
                for (WebElement ele : eles) {
                    if (ele.getText() == optionText) {
                        ele.click();
                        return;
                    }
                }
            }
        }
    }
    public void selectByIndex(String locateType, String locateId, int index) {
        WebElement findEle = this.find(locateType, locateId);
        if (findEle != null) {
            this.currentWebElement = findEle;
            if (this.currentWebElement.getTagName() == "select") {
                Select select = new Select(this.currentWebElement);
                select.selectByIndex(index);
            } else if (this.currentWebElement.getTagName() == "ul") {
                List<WebElement> eles = this.currentWebElement.findElements(By.tagName("li"));
                int i = 0;
                for (WebElement ele : eles) {
                    if (++i == index) {
                        ele.click();
                        return;
                    }
                }
            }
        }
    }
    public void selectByValue(String locateType, String locateId, String value)
    {
        WebElement findEle = this.find(locateType, locateId);
        if(findEle!=null) {
            this.currentWebElement = findEle;
            if (this.currentWebElement.getTagName() == "select") {
                Select select = new Select(this.currentWebElement);
                select.selectByValue(value);
            } else if (this.currentWebElement.getTagName() == "ul") {
                List<WebElement> eles = this.currentWebElement.findElements(By.tagName("li"));
                for (WebElement ele : eles) {
                    if (ele.getAttribute("data") == value) {
                        ele.click();
                        return;
                    }
                }
            }
        }
    }

    //methods of WebPage
    public WebElement find(String locateType, String locateId)
    {
        try {
            this.currentWebElement = BrowserUtil.Find(this.webdriver, null, locateType, locateId);
        }
        catch (Exception e)
        {
            return null;
        }
        return this.currentWebElement;
    }
   public List<WebElement> finds(String locateType, String locateId)
    {
        try {
            return BrowserUtil.Finds(this.webdriver, null, locateType, locateId);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public WebElement findByPrevious(String locateType, String locateId)
    {
        try {
            return BrowserUtil.Find(this.webdriver, this.currentWebElement, locateType, locateId);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public List<WebElement> findsByPrevious(String locateType, String locateId)
    {
        try {
            return BrowserUtil.Finds(this.webdriver, this.currentWebElement, locateType, locateId);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public void close()
    {
        if(webdriver != null)
        {
            webdriver.close();
            webdriver = null;
        }
    }
    public void quit()
    {
        if(webdriver != null)
        {
            webdriver.quit();
            webdriver = null;
        }
    }

}
