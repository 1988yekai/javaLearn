package com.yek.beans;

public class UserBean {
    private Integer id;

    private String uname;

    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname == null ? null : uname.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                ", age=" + age +
                '}';
    }
}