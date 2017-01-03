package com.yek.mian;

import com.yek.beans.UserBean;
import com.yek.mapper.UserBeanMapper;
import com.yek.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * Created by yek on 2016-12-30.
 */
public class Test1 {
    public static void main(String[] args) {
        SqlSessionFactory factory = MybatisUtil.getFactory();
        SqlSession sqlSession = factory.openSession();
        UserBeanMapper mapper = sqlSession.getMapper(UserBeanMapper.class);
        UserBean bean = mapper.selectByPrimaryKey(1);
        System.out.println(bean);
    }
}
