package com.yek.dao;

import com.yek.beans.UserTable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * dao
 * Created by yek on 2016-8-29.
 */
public class UserDao {
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public UserTable findUserById(int id)throws Exception{
        UserTable userTable = null;
        Connection connection = dataSource.getConnection();
        String sql = "select * from user_table where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            userTable = new UserTable();
            userTable.setId(resultSet.getInt(1));
            userTable.setName(resultSet.getString(2));
            userTable.setBirthday(resultSet.getDate(3));
            userTable.setSex(resultSet.getString(4));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return userTable;
    }
}
