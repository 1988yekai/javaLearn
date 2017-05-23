package qz.bigdata.crawler.utility;

/**
 * Created by fys on 2015/1/15.
 */
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;


public class MyHttpClient {

    private static Logger logger = Logger.getLogger(MyHttpClient.class);
    //private static HttpClient instance = null;
    private HttpClient instance;
    //private static R
    private void initHttpClient(){

        SchemeRegistry schemeRegistry = new SchemeRegistry();

        schemeRegistry.register(
                new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

        PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        // Increase max total connection to 200
        cm.setMaxTotal(50);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20+1);
        // Increase max connections for localhost:80 to 50
        //HttpHost localhost = new HttpHost("locahost", 80);
        //cm.setMaxPerRoute(new HttpRoute(localhost), 50);

        instance = new DefaultHttpClient(cm);
        instance.getParams().setParameter("http.socket.timeout", new Integer(1000*20));
        instance.getParams().setParameter("http.connection.timeout", new Integer(1000*20));


        //this is used for qinghua fbbs especially not for common
        //Referer:http://www.newsmth.net/nForum/
        instance.getParams().setParameter("Referer", "http://www.newsmth.net/nForum/board/Algorithm?ajax");



        //instance.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        //instance.getParams().setParameter("http.protocol.expect-continue", false);

        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount,HttpContext contex
            ) {
                // retry a max of 5 times
                //logger.warn("exception :"+exception.getMessage()+"   execution times = "+executionCount);

                if(executionCount >= 5){
                    if(exception instanceof SocketTimeoutException){
                        logger.warn(Thread.currentThread().getName()+"repeat times >= 5");
                        return true;
                    }
                    return false;
                }
                if(exception instanceof NoHttpResponseException){
                    ThreadSleep(10);
                    return true;
                } else if (exception instanceof ClientProtocolException){
                    ThreadSleep(10);
                    return true;
                }else if(exception instanceof SocketTimeoutException){
                    ThreadSleep(60,30);
                    return true;
                }

                return false;
            }
        };

        ((AbstractHttpClient) instance).setHttpRequestRetryHandler(retryHandler);
    }

    public static HttpClient getInstance(){

        //if(instance == null){

        MyHttpClient client = new MyHttpClient();
        client.initHttpClient();

        //}

        return client.instance;
    }

    public static void ThreadSleep(int sec){
        try {
            Thread.sleep(1000*(int)(Math.random()*sec+5));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void ThreadSleep(int sec,int basetime){
        try {
            Thread.sleep(1000*(int)(Math.random()*sec+basetime));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
