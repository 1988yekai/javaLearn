package com.yek.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * bean
 * Created by yek on 2016-8-29.
 */
public class UserTable {
    private int id;
    private String name;
    private Date birthday;
    private String sex;

    public UserTable() {
    }

    public UserTable(int id, String name, Date birthday, String sex) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserTable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday='" + new SimpleDateFormat("yyyy-MM-dd").format(birthday) + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
