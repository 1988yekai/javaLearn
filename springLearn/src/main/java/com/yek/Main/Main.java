package com.yek.main;

import com.yek.beans.UserTable;
import com.yek.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yek on 2016-8-29.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext_dao.xml");
        UserService service = (UserService) context.getBean("service");
        UserTable user = service.findUserById(2);
        System.out.println(user);
    }
}
