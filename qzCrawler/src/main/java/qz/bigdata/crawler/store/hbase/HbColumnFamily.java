package qz.bigdata.crawler.store.hbase;

import java.lang.Object;import java.lang.String;import java.util.HashMap;

/**
 * Created by fys on 2015/5/7.
 */
public class HbColumnFamily {
    public String family;
    public HashMap<String, Object> columns = new HashMap<String, Object>();
}
