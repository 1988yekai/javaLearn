package qz.bigdata.crawler.core;

import java.net.URL;
/**
 * Created by fys on 2015/1/11.
 */
public interface IBrowserController {
    void setStartUrl(URL url) throws InterruptedException;
}
