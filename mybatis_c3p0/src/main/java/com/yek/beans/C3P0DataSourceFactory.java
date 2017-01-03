package com.yek.beans;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * Created by yek on 2016-12-30.
 */
public class C3P0DataSourceFactory extends UnpooledDataSourceFactory {
    public C3P0DataSourceFactory() {
        this.dataSource = new ComboPooledDataSource();
    }
}
