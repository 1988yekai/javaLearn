package dao;

import bean.UserBean;

/**
 * Created by yek on 2016-8-16.
 */
public interface UserMapper {
    public UserBean getById(int id);
    public int count();
}
