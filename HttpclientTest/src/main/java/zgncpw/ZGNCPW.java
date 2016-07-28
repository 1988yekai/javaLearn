package zgncpw;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2016-7-11.
 * 失败......
 */
public class ZGNCPW {
    private CookieStore cookieStore = new BasicCookieStore();
    private HttpClientContext context = null;

    private void setCookieStore(){
        BasicClientCookie cookie1 = new BasicClientCookie("D1s_auth",
                "605dcsRe0UgveJsQ9dNhqnS8bEK5ZCVXFpzOORO5tl6n-P-mH3UaZN3-S-KpWfzHE3Qzt7XB4px3rfEgqvKFR7mHtXd7AZBMsA");
//        cookie1.setVersion(0);
        cookie1.setDomain(".zgncpw.com");
        cookie1.setPath("/");
        cookie1.setExpiryDate((new GregorianCalendar(2020,7,11)).getTime());
        cookie1.setSecure(false);

        BasicClientCookie cookie2 = new BasicClientCookie("D1s_username",
                "bellye123");
//        cookie2.setVersion(0);
        cookie2.setDomain(".zgncpw.com");
        cookie2.setPath("/");
        cookie2.setExpiryDate((new GregorianCalendar(2020,7,11)).getTime());
        cookie2.setSecure(false);

        BasicClientCookie cookie3 = new BasicClientCookie("CNZZDATA1256881620",
                "475097379-1468221006-%7C1468221006");
//        cookie3.setVersion(0);
        cookie3.setDomain("www.zgncpw.com");
        cookie3.setPath("/");
        cookie3.setExpiryDate((new GregorianCalendar(2020,7,11)).getTime());
        cookie3.setSecure(false);

        cookieStore.addCookie(cookie1);
        cookieStore.addCookie(cookie2);
        cookieStore.addCookie(cookie3);

    }
    @Test
    public void test1() throws Exception{
        System.out.println("----testCookieStore");
        setCookieStore();
        String testUrl = "http://www.zgncpw.com/sell/show-52942.html";
        // 使用cookieStore方式
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultCookieStore(cookieStore).build();
        HttpGet httpGet = new HttpGet(testUrl);
        System.out.println("request line:" + httpGet.getRequestLine());
        HttpResponse httpResponse = client.execute(httpGet);
        System.out.println("cookie store:" + cookieStore.getCookies());
        printResponse(httpResponse);
    }

    private void printResponse(HttpResponse httpResponse) throws Exception{
        System.out.println(EntityUtils.toString(httpResponse.getEntity(),"utf-8"));
    }

    @Test
    public void test2() throws Exception{
        System.out.println("----test2");
        String testUrl = "http://www.zgncpw.com/sell/show-52942.html";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(testUrl);
//        httpGet.setHeader("Cookie","CNZZDATA1256881620=1616630441-1465370137-%7C1468226406; PHPSESSID=07hb23irjgkdjtkc6jb121q327; D1s_auth=972bF8TAsK5x-P-z5D2DSbYzBurh3X2SAkcSTX5lVVVHJVQv9-P--S-mDocuvLUu1a0Y0rQzjQY2Mii3DaD2ryJIrWgq1IDhXDQg; D1s_username=bellye123");
        httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpGet.setHeader("Accept-Encoding","gzip, deflate");
        httpGet.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
        HttpResponse httpResponse = client.execute(httpGet);
        printResponse(httpResponse);
    }

}
