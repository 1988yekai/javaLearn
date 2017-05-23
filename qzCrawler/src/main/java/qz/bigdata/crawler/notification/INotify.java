package qz.bigdata.crawler.notification;

/**
 * Created by Jiest on 2015/1/30.
 *  统一日志处理方案
 */
public interface INotify {
    void error(String error);
    void success(String url, String content);
    void info(String info);
    void visit(String url);
}
