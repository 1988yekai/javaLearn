package qz.bigdata.crawler.core;

import qz.bigdata.crawler.browser.*;
/**
 * Created by fys on 2015/1/26.
 */
public interface IExtract {
    void init();
    void extract(Browser browser);
}
