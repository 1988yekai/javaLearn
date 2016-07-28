package lcApple;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * Created by Administrator on 2016-6-16.
 */
public class LCAppleTest {
    public static void main(String[] args) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet("https://aldh5.tmall.com/recommend2.htm?notNeedBackup=true&appId=2016022913,201603010,2016031463,2016030115,201603071,2016031461,2016031462,2016031464,2016031465,2016031466,2016031467&callback=jsonp_10413748");
        HttpGet httpGet = new HttpGet("http://www.lcapple.com/wangye/hangqing/hq_chandi.jsp?getCurPage=1&curBigPage=1&query_string=null");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
//        System.out.println(EntityUtils.toString(httpEntity,"utf-8"));
        String html = EntityUtils.toString(httpEntity,"utf-8");
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode tagNode = htmlCleaner.clean(html);
        Object[] objects = tagNode.evaluateXPath("//table");
        if (objects.length > 0)
            System.out.println(((TagNode)objects[0]).getText().toString().trim().replaceAll("&nbsp;","").replaceAll("\\s",""));
        httpClient.close();

    }
}
