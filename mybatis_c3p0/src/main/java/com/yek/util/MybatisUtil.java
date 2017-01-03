package com.yek.util;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yek on 2016-12-21.
 */
public class MybatisUtil {
    static {
        String recourse = "mybatisConfig.xml";
        InputStream inputStream = MybatisUtil.class.getClassLoader().getResourceAsStream(recourse);
        factory = new SqlSessionFactoryBuilder().build(inputStream);
        if (inputStream !=null)
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    private static SqlSessionFactory factory;

    public static SqlSessionFactory getFactory() {
        return factory;
    }
}
