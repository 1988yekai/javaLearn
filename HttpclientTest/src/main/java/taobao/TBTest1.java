package taobao;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016-6-21.
 */
public class TBTest1 {
    @Test
    public void test1(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("https://item.taobao.com/item.htm?spm=a219e.1191392.1111.5.nGzOWQ&id=527867784496&scm=1029.newlist-0.1.50108542&ppath=&sku=&ug=#detail");

        try {
            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity,"utf-8");
            OutputStream outputStream = new FileOutputStream("d:/taobao.html");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(html.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                System.out.println("写入结束，成功退出！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void test2() throws Exception{
        String url = "https://detailskip.taobao.com/service/getData/1/p2/item/detail/sib.htm?itemId=527867784496&modules=qrcode,viewer,price,contract,duty,xmpPromotion,dynStock,delivery,upp,sellerDetail,activity,fqg,zjys,coupon&callback=onSibRequestSuccess";
        HttpClientBuilder builder = HttpClients.custom();
        builder.setUserAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
        CloseableHttpClient httpClient = builder.build();

        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Referer","https://item.taobao.com/item.htm?spm=a219e.1191392.1111.5.nGzOWQ&id=527867784496&scm=1029.newlist-0.1" +
                ".50108542&ppath=&sku=&ug=");
        CloseableHttpResponse response = null;
        response = httpClient.execute(httpGet);
        final HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity,"utf-8");
            EntityUtils.consume(entity);
        }
        System.out.println(result);

        httpClient.close();
    }
}
