package qz.bigdata.crawler.notification;

import org.apache.log4j.Logger;

/**
 * Created by Jiest on 2015/1/30.
 *  统一日志处理方案
 */
public class ResultManager implements INotify {
    //Logger logger = new  Logger.getLogger(getClass());
//    @Override
//    public void error(String url, String error) {
//        logger.error(error);
//        //send mail to administrator
//    }
//
//    @Override
//    public void success(object successInfo) {
//
//    }

    @Override
    public void error(String error) {

    }

    @Override
    public void success(String url, String content) {

    }

    @Override
    public void info(String info) {

    }

    @Override
    public void visit(String url) {

    }
}
