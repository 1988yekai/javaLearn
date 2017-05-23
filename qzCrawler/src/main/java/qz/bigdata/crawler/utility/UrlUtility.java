package qz.bigdata.crawler.utility;

/**
 * Created by fys on 2015/1/15.
 */

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtility {

    private static String m_urlPatternString = "(?i)(?s)<\\s*?a.*?href=\"(.*?)\".*?>";
    private static Pattern m_urlPattern = Pattern.compile(m_urlPatternString);

    private static Logger m_debug = Logger.getLogger(UrlUtility.class);

    public static void ExtractURL(String baseUrl, String content) {
        Matcher matcher = m_urlPattern.matcher(content);
        while (matcher.find()) {
            String anchor = matcher.group();

            String url = StrUtility.GetSubString(anchor, "href=\"", "\"");
            if ((url = UrlUtility.Refine(baseUrl, url)) != null) {
                // Queue.Add(url);
            }
        }
    }

    //
    public static String Encode(String url) {
        String res = "";
        for (char c : url.toCharArray()) {
            if (!":/.?&#=".contains("" + c)) {
                try {
                    res += URLEncoder.encode("" + c, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    m_debug.debug("This JVM has no UTF-8 charset. It's strange");
                }
            } else {
                res += c;
            }
        }

        return res;
    }

    public static String Normalizer(String url) {
        url = url.replaceAll("&amp;", "&");
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }

    //
    public static String Refine(String baseUrl, String relative) {
        if (baseUrl == null || relative == null) {
            return null;
        }

        final Url base = Parse(baseUrl), url = Parse(relative);
        if (base == null || url == null) {
            return null;
        }

        if (url.scheme == null) {
            url.scheme = base.scheme;
            if (url.host == null) {
                url.host = base.host;
            }
        }

        if (url.path.startsWith("../")) {
            String prefix = "";
            int idx = base.path.lastIndexOf('/');
            if ((idx = base.path.lastIndexOf('/', idx - 1)) > 0)
                prefix = base.path.substring(0, idx);
            url.path = prefix + url.path.substring(3);
        }

        return Normalizer(url.ToUrl());
    }


    private static Url Parse(String link) {
        int idx, endIndex;
        final Url url = new Url();

        if ((idx = link.indexOf("#")) >= 0) { // ignore fragment
            if (idx == 0)
                return null;
            else
                link = link.substring(0, idx - 1);
        }

        // if( (idx = link.indexOf("?")) > 0 ) { //ignore query information
        // link = link.substring(0, idx);
        // }

        if ((idx = link.indexOf(":")) > 0) {
            url.scheme = link.substring(0, idx).trim();
            if (IsLegalScheme(url.scheme)) {
                link = link.substring(idx + 1);
            } else {
                return null;
            }
        }

        if (link.startsWith("//")) {
            if ((endIndex = link.indexOf('/', 2)) > 0) {
                url.host = link.substring(2, endIndex).trim();
                link = link.substring(endIndex + 1);
            } else {
                url.host = link.substring(2).trim();
                link = null;
            }
        }

        if (link != null)
            url.path = link.trim();
        else
            url.path = "";

        return url;
    }

    private static boolean IsLegalScheme(String scheme) {
        if (scheme.equals("http") || scheme.equals("https")
                || scheme.equals("ftp"))
            return true;
        else
            return false;
    }

    private static class Url {
        public Url() {
        }

        public String ToUrl() {
            String prefix = null;
            if (path.startsWith("/"))
                prefix = scheme + "://" + host;
            else
                prefix = scheme + "://" + host + "/";

            return prefix + path;
        }

        public String scheme;
        public String host;
        public String path;
    }

    /*
    // 外部调用接口 用来检测一个url是否为合法的url
    public static boolean validateAndAddUrl(String url,
                                            HashMap<DataCategory, List<String>> contentMap, IFrontier urlsQueue) {

        String domain = TemplateParser.GetDomainName(url);
        String baseUrl = TemplateParser.GetBaseUrlMap().get(domain);
        String fullUrl = null;
        LinkedList<String> list = (LinkedList<String>) contentMap
                .get(DataCategory.Topic_Url);

        if (list == null) {
            return false;
        }

        for (String href : list) {
            if (href != null) {
                if (!href.contains("href")) {
                    if (url.contains("\"")) {
                        continue;
                    }

                    fullUrl = href;
                } else {
                    // <a href="/post-284-281674-1.shtml"
                    fullUrl = baseUrl
                            + StrUtility.GetSubString(href, "href=\"", "\"");
                }

                // if(fullUrl.contains("&amp;")){
                // html中的转义符逆转换
                fullUrl = fullUrl.replaceAll("&amp;", "&");
                fullUrl = fullUrl.replace("\"", "");
                // }
                if(!fullUrl.contains("http://")){
                    fullUrl ="http://"+fullUrl;
                }

                if(fullUrl.contains("hexun") && fullUrl.contains("money")){
                    fullUrl.replaceFirst("money", "mon_ey");
                }

                urlsQueue.add(fullUrl);
            }
        }

        return true;
    }
    */

    /*
    public static String FormTopicID(String url) {
        String str = null;
        if (url.contains("tianya")) {
            Pattern pattern = null;
            if(url.contains("stocks")){
                pattern = Pattern.compile("-stocks+-\\d+");
            }else if (url.contains("develop")){
                pattern = Pattern.compile("-develop+-\\d+");
            }

            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return "ty" + url.substring(matcher.start(), matcher.end());
            }
        } else if (url.contains("scol")) {
            Pattern pattern = Pattern.compile("d-\\d+");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return "so" + url.substring(matcher.start() + 1, matcher.end());
            }
        } else if (url.contains("autohome")) {
            // http://club.autohome.com.cn/fbbs/thread-a-100025-23315459-1.html
            Pattern pattern = Pattern.compile("d-.-\\d+-\\d+");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return "ah" + url.substring(matcher.start() + 3, matcher.end());
            } else {
                m_debug.warn("ah topicId not formed ");
                return null;
            }
        }

        else if (url.contains("xcar")) {
            // http://club.autohome.com.cn/fbbs/thread-a-100025-23315459-1.html
            Pattern pattern = Pattern.compile("tid=\\d+");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return "xc" + url.substring(matcher.start() + 4, matcher.end());
            } else {
                m_debug.warn("xcar topicId not formed ");
                return null;
            }
        } else if (url.contains("junzhuan")) {
            // http://club.autohome.com.cn/fbbs/thread-a-100025-23315459-1.html
            Pattern pattern = Pattern.compile("d-\\d+");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return "jz" + url.substring(matcher.start() + 2, matcher.end());
            } else {
                m_debug.warn("jz topicId not formed ");
                return null;
            }
        } else if (url.contains("tieba")) {
            // p/2339862284
            Pattern pattern = Pattern.compile("p/\\d+");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return "tb" + url.substring(matcher.start() + 2, matcher.end());
            } else {
                m_debug.warn("tieba topicId not formed ");
                return null;
            }
        }else if (url.contains("sina")) {
            //thread-3300516-
            Pattern pattern = Pattern.compile("d-\\d+");
            Pattern pattern2 = Pattern.compile("tid=\\d+");
            Matcher matcher = pattern.matcher(url);
            Matcher matcher2 = pattern2.matcher(url);
            if (matcher.find()) {
                return "sn" + url.substring(matcher.start() + 2, matcher.end());
            } else if(matcher2.find()){
                //http://club.finance.sina.com.cn/viewthread.php?tid=2826790&extra=page%3D1&page=2
                return "sn" + url.substring(matcher2.start() + 4, matcher2.end());
            }else{
                m_debug.warn("sn topicId not formed ");
                return null;
            }
        }else if (url.contains("hexun")) {
            // http://fbbs.hexun.com/futures/post_70_4149636_3_d.html
            Pattern pattern = Pattern.compile("_\\d+_\\d+_");
            //Pattern pattern2 = Pattern.compile("tid=\\d+");
            Matcher matcher = pattern.matcher(url);
            //Matcher matcher2 = pattern2.matcher(url);
            if (matcher.find()) {
                return "hx" + url.substring(matcher.start() + 1, matcher.end()-1);
            }else{
                m_debug.warn("hx topicId not formed ");
                return null;
            }
        }else if ( !url.contains("hexun")  && url.contains("education")) {
            //  /352415061.html
            Pattern pattern = Pattern.compile("/\\d+");
            //Pattern pattern2 = Pattern.compile("tid=\\d+");
            Matcher matcher = pattern.matcher(url);
            //Matcher matcher2 = pattern2.matcher(url);
            if (matcher.find()) {
                return "ne" + url.substring(matcher.start() + 1, matcher.end());
            }else{
                m_debug.warn("ne topicId not formed ");
                return null;
            }
        }else if (url.contains("netbig")) {
            Pattern pattern = Pattern.compile("d-\\d+");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return "nb" + url.substring(matcher.start() + 1, matcher.end());
            }
        }else if (url.contains("newsmth")) {
            //<a href="/nForum/article/Algorithm/58629">
            Pattern pattern = Pattern.compile("/\\d+");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return "nm" + url.substring(matcher.start() + 1, matcher.end());
            }
        }

        return null;
    }
    */

    public static String formTopicIDWithPage(String topicID,int page){

        if(page > 0){
            return topicID+"-"+page;
        }

        return null;
    }

    public static String DecryptUrl(String originalUrl){

        if(originalUrl.contains("hexun")){
            return originalUrl.replaceAll("mon_ey", "money");
        }
        return originalUrl;
    }
}

