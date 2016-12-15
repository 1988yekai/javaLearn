package com.yek.service;

import com.yek.beans.UserTable;
import com.yek.dao.UserDao;

/**
 * service
 * Created by yek on 2016-8-29.
 */
public class UserService {
    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserTable findUserById(int id) throws Exception {
        return userDao.findUserById(id);
    }
}
