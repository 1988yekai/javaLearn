package qz.bigdata.crawler.browser;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * Created by fys on 2015/1/8.
 */
public class BrowserUtil {

    private static Boolean IsElementPresent(WebDriver driver, By by)
    {
        try
        {
            driver.findElement(by);
            return true;
        }
        catch (NoSuchElementException e)
        {
            return false;
        }
    }

    private static Boolean IsAlertPresent(WebDriver driver)
    {
        try
        {
            driver.switchTo().alert();
            return true;
        }
        catch (NoAlertPresentException e)
        {
            return false;
        }
    }

    private static String CloseAlertAndGetItsText(WebDriver driver, Boolean acceptAlert)
    {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        if (acceptAlert)
        {
            alert.accept();
        }
        else
        {
            alert.dismiss();
        }
        return alertText;
    }

    public static WebElement Find(WebDriver driver, WebElement father, String locateType, String locateId)
            throws Exception
    {
        WebElement webEle = null;
        if (locateType == "xpath")
        {
            if (father != null)
            {
                webEle = father.findElement(By.xpath(locateId));
            }
            else
            {
                webEle = driver.findElement(By.xpath(locateId));
            }
        }
        else if (locateType == "id")
        {
            if (father != null)
            {
                webEle = father.findElement(By.id(locateId));
            }
            else
            {
                webEle = driver.findElement(By.id(locateId));
            }
        }
        else if (locateType == "classname")
        {
            if (father != null)
            {
                webEle = father.findElement(By.className(locateId));
            }
            else
            {
                webEle = driver.findElement(By.className(locateId));
            }
        }
        else if (locateType == "css")
        {
            if (father != null)
            {
                webEle = father.findElement(By.cssSelector(locateId));
            }
            else
            {
                webEle = driver.findElement(By.cssSelector(locateId));
            }
        }
        else if (locateType == "linktext")
        {
            if (father != null)
            {
                webEle = father.findElement(By.linkText(locateId));
            }
            else
            {
                webEle = driver.findElement(By.linkText(locateId));
            }
        }
        else if (locateType == "name")
        {
            if (father != null)
            {
                webEle = father.findElement(By.name(locateId));
            }
            else
            {
                webEle = driver.findElement(By.name(locateId));
            }
        }
        else if (locateType == "partiallinktext")
        {
            if (father != null)
            {
                webEle = father.findElement(By.partialLinkText(locateId));
            }
            else
            {
                webEle = driver.findElement(By.partialLinkText(locateId));
            }
        }
        else if (locateType == "tagname")
        {
            if (father != null)
            {
                webEle = father.findElement(By.tagName(locateId));
            }
            else
            {
                webEle = driver.findElement(By.tagName(locateId));
            }
        }
        else
        {
            throw new Exception("unknown locate type: " + locateType);
        }
        return webEle;
    }

    public static WebElement Find(WebDriver driver, String[][] locateInfos)
            throws Exception, NoSuchElementException
    {
        int length = locateInfos[0].length;
        if(length != 2)
        {
            throw new Exception("locateInfos必须为2维数组，且第二维长度必须为2。");
        }
        String locateType = "";
        String locateId = "";
        WebElement webEle = null;

        for (int i = 0; i < length - 1; i++ )
        {
            locateType = locateInfos[i][0];
            locateId = locateInfos[i][1];
            try {
                webEle = Find(driver, webEle, locateType, locateId);
            }
            catch (Exception e)
            {
                throw e;
            }
            if(webEle == null)
            {
                throw new NoSuchElementException("无法找到页面元素父路径上的节点。");
            }
        }
        return Find(driver, webEle, locateInfos[length - 1][0], locateInfos[length - 1][1]);
    }

    public static List<WebElement> Finds(WebDriver driver, WebElement father, String locateType, String locateId)
            throws Exception
    {
        List<WebElement> webEle = null;
        locateType = locateType.toLowerCase();
        if(driver.getClass() == ChromeDriver.class ){
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        }
        if (locateType == "xpath")
        {
            if (father != null)
            {
                webEle = father.findElements(By.xpath(locateId));
            }
            else
            {
                webEle = driver.findElements(By.xpath(locateId));
            }
        }
        else if (locateType == "id")
        {
            if (father != null)
            {
                webEle = father.findElements(By.id(locateId));
            }
            else
            {
                webEle = driver.findElements(By.id(locateId));
            }
        }
        else if (locateType == "classname")
        {
            if (father != null)
            {
                webEle = father.findElements(By.className(locateId));
            }
            else
            {
                webEle = driver.findElements(By.className(locateId));
            }
        }
        else if (locateType == "css")
        {
            if (father != null)
            {
                webEle = father.findElements(By.cssSelector(locateId));
            }
            else
            {
                webEle = driver.findElements(By.cssSelector(locateId));
            }
        }
        else if (locateType == "linktext")
        {
            if (father != null)
            {
                webEle = father.findElements(By.linkText(locateId));
            }
            else
            {
                webEle = driver.findElements(By.linkText(locateId));
            }
        }
        else if (locateType == "name")
        {
            if (father != null)
            {
                webEle = father.findElements(By.name(locateId));
            }
            else
            {
                webEle = driver.findElements(By.name(locateId));
            }
        }
        else if (locateType == "partiallinktext")
        {
            if (father != null)
            {
                webEle = father.findElements(By.partialLinkText(locateId));
            }
            else
            {
                webEle = driver.findElements(By.partialLinkText(locateId));
            }
        }
        else if (locateType == "tagname")
        {
            if (father != null)
            {
                webEle = father.findElements(By.tagName(locateId));
            }
            else
            {
                webEle = driver.findElements(By.tagName(locateId));
            }
        }
        else
        {
            throw new Exception("unknown locate type: " + locateType);
        }
        return webEle;
    }

    public static List<WebElement> Finds(WebDriver driver, String[][] locateInfos)
            throws Exception, NoSuchElementException
    {
        int length = locateInfos[0].length;
        if (length != 2)
        {
            throw new Exception("locateInfos必须为2维数组，且第二维长度必须为2。");
        }
        String locateType = "";
        String locateId = "";
        WebElement webEle = null;


        for (int i = 0; i < length - 1; i++)
        {
            locateType = locateInfos[i][0];
            locateId = locateInfos[i][1];
            try
            {
                webEle = Find(driver, webEle, locateType, locateId);
            }
            catch(Exception e)
            {
                throw e;
            }

            if (webEle == null)
            {
                throw new NoSuchElementException("无法找到页面元素父路径上的节点。");
            }
        }
        return Finds(driver, webEle, locateInfos[length - 1][0], locateInfos[length - 1][1]);
    }

    public static By GetBy(WebDriver driver, WebElement father, String locateType, String locateId)
            throws Exception
    {
        locateType = locateType.toLowerCase();
        if (locateType == "xpath")
        {
            return By.xpath(locateId);
        }
        else if (locateType == "id")
        {
            return By.id(locateId);
        }
        else if (locateType == "classname")
        {
            return By.className(locateId);
        }
        else if (locateType == "css")
        {
            return By.cssSelector(locateId);
        }
        else if (locateType == "linktext")
        {
            return By.linkText(locateId);
        }
        else if (locateType == "name")
        {
            return By.name(locateId);
        }
        else if (locateType == "partiallinktext")
        {
            return By.partialLinkText(locateId);
        }
        else if (locateType == "tagname")
        {
            return By.tagName(locateId);
        }
        else
        {
            throw new Exception("unknown locate type: " + locateType);
        }
    }


    // Description: Function to select an item on the actions menu
    // Parameters: menuID - ID of menu; menuItemID - ID of menu item to be selected
    // Globals: None
    // Return Values: None
    // Example: ePO.Utilities.selectActionsMenuItem("MenuID", "AddComputer");

    public static void selectActionsMenuItem(WebDriver driver, String menuID, String menuItemID)
    {
        //Create a new action builder
        Actions builder = new Actions(driver);
        //click on Group Actions menu
        WebElement actionMenu = driver.findElement(By.id(menuID));
        builder.moveToElement(actionMenu).click(actionMenu).perform();
        //Click the rename option
        List<WebElement> lRenameGroupElements = driver.findElements(By.id(menuItemID));
        for (WebElement MyItem : lRenameGroupElements)
        {
            if (MyItem.isDisplayed())
            {
                MyItem.click();
            }
        }
    }

    // Description: Function to expand/collaspe tree branch
    // Parameters: treeID - ID of tree; nodeID - ID of node to be expanded/collasped
    // Globals: None
    // Return Values: None
    // Example: ePO.Utilities.toggleTreeBranch("TreeMenuID", "node_0");
    public static void toggleTreeBranch(WebDriver driver, String treeID, String nodeID)
    {
        //Expand the main group to see all tag groups
        driver.navigate().refresh();
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("var evt={target:{OrionEventUserProperty:{get:function(key){return{id:\"" + nodeID + "\",treeId:\"" + treeID + "\"};}}}};OrionTree.toggleBranchEvent(evt);");
    }

    public static void scrollToBottom(WebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight," +
                "document.body.scrollHeight,document.documentElement.clientHeight));");
    }
}
