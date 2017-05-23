package qz.bigdata.crawler.utility;

/**
 * Created by fys on 2015/1/15.
 */

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HttpUtility {

    static{

    }


    Logger logger = Logger.getLogger(HttpUtility.class);
    //αװ�õ�agent
    private static String[] m_agent = {"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2)",
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)", };

    private static Logger m_debug = Logger.getLogger(HttpUtility.class);
    private  HttpClient client = null;
    private HttpContext context= null;
    public HttpUtility(HttpClient aClient){

        super();
        client = MyHttpClient.getInstance();

        /*context = new BasicHttpContext();
        //client =  aClient;
SchemeRegistry schemeRegistry = new SchemeRegistry();

		schemeRegistry.register(
		         new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

		PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
		// Increase max total connection to 200
		cm.setMaxTotal(50);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(20+1);
        client = new DefaultHttpClient(cm);
        client.getParams().setParameter("http.socket.timeout", new Integer(1000*50));
        client.getParams().setParameter("http.connection.timeout", new Integer(1000*50));
        */
    }

    public void closeClient(){

        //client.getConnectionManager().closeExpiredConnections();
        //client.getConnectionManager().closeIdleConnections(30, TimeUnit.SECONDS);
    }

    //get entity from this specific url
    public  HttpEntity GetEntity(String url) {

        HttpGet getMethod = new HttpGet(UrlUtility.Encode(url));
        //getMethod
        getMethod.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);

        //αװagent
        java.util.Random r = new java.util.Random();
        getMethod.setHeader("User-Agent", m_agent[r.nextInt(m_agent.length)]);

        HttpResponse response = null;
        try {

            response = client.execute(getMethod,context);

        } catch (Exception e) {

            m_debug.warn(Thread.currentThread().getName()+" can't get response from " + url);
            m_debug.warn("reason is : " +Thread.currentThread().getName()+ e.getMessage());

            return null;

        }finally{

        }

        int statusCode = response.getStatusLine().getStatusCode();

        if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
                || (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
                || (statusCode == HttpStatus.SC_SEE_OTHER)
                || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {  // ת��ץȡ������
            return GetEntity(response.getLastHeader("Location").getValue());
        }
        /*else if( statusCode == HttpStatus.SC_NOT_FOUND ) { //�Ҳ�����ҳ
            m_debug.warn(url + " : page was no found");
            response = null;
        }
        */
        if (response != null)
            return response.getEntity();
        else
            return null;
    }

    public  String GetContent(HttpEntity entity) {
        if( entity != null ) {
            byte[] bytes;
            try {
                entity.getContent();
                bytes = EntityUtils.toByteArray(entity);

            } catch (IOException e) {

                m_debug.warn(Thread.currentThread().getName()+"can't get bytes from entity. Reason are: " + e.getMessage());
                return null;
            }catch(Exception e){

                m_debug.warn(Thread.currentThread().getName()+"can't get bytes from entity. Reason are: " + e.getMessage());
                return null;
            }

            String charSet = EntityUtils.getContentCharSet(entity); //�õ���ҳ�����ʽ
            if( charSet != null ) {  //��ҳ�����и�֪����
                try {
                    return new String(bytes, charSet);
                } catch (UnsupportedEncodingException e) {
                    m_debug.debug("unsupported charset " + charSet);
                    return null;
                }
            }
            else {
                return GetContent(bytes);
            }
        }

        return null;
    }

    public  String GetContent(byte[] bytes) {

        CharsetDetector detector = new CharsetDetector();
        detector.setText(bytes);
        CharsetMatch match = detector.detect();

        try {

            return match.getString();

        } catch (Exception e) {
            m_debug.debug("can't get content. Reason are: " + e.getMessage());
            return null;
        }
    }

}