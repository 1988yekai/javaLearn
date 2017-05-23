package qz.bigdata.crawler.core;

import java.net.URL;

/**
 * Created by fys on 2015/1/12.
 */
public class UrlInfo {
    public String time;
    public String area;
    public String countType;
    public String source;
    public String title;
    public URL url;
    public URL parentUrl;
    public Object data;
    public String handlerName = null;

    public UrlInfo() {
    }

    public UrlInfo(URL url) {
        this.url = url;
        this.parentUrl = null;
        this.data = null;
        this.handlerName = null;
    }

    public UrlInfo(URL url, String handlerName) {
        this.url = url;
        this.parentUrl = null;
        this.data = null;
        this.handlerName = handlerName;
    }

    public UrlInfo(URL url, URL parentUrl, String handlerName) {
        this.url = url;
        this.parentUrl = parentUrl;
        this.data = null;
        this.handlerName = handlerName;
    }

    public UrlInfo(URL url, URL parentUrl, Object data, String handlerName) {
        this.url = url;
        this.parentUrl = parentUrl;
        this.data = data;
        this.handlerName = handlerName;
    }

    public UrlInfo(URL url, String source, String countType, String area, String title, String time, Object data, String handlerName) {
        this.url = url;
        this.title = title;
        this.source = source;
        this.countType = countType;
        this.area = area;
        this.data = data;
        this.time = time;
        this.handlerName = handlerName;
    }

    public UrlInfo(URL url, String source, String countType, String area, String title, String time, Object data, String handlerName, URL parentUrl) {
        this.url = url;
        this.title = title;
        this.source = source;
        this.countType = countType;
        this.area = area;
        this.data = data;
        this.time = time;
        this.handlerName = handlerName;
        this.parentUrl = parentUrl;
    }
}
