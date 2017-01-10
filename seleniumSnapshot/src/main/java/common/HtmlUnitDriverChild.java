package common;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Created by yek on 2017-1-10.
 */
public class HtmlUnitDriverChild extends HtmlUnitDriver{
    public HtmlUnitDriverChild() {
        super();
    }

    public HtmlUnitDriverChild(boolean enableJavascript) {
        super(enableJavascript);
    }

    public HtmlUnitDriverChild(BrowserVersion version, boolean enableJavascript) {
        super(version, enableJavascript);
    }

    public HtmlUnitDriverChild(BrowserVersion version) {
        super(version);
    }

    public HtmlUnitDriverChild(Capabilities capabilities) {
        super(capabilities);
    }

    public HtmlUnitDriverChild(Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(desiredCapabilities, requiredCapabilities);
    }

    @Override
    public WebClient getWebClient() {
        return super.getWebClient();
    }
}
