package test1.po;

import java.util.Date;

/**
 * Created by yek on 2016-7-20.
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

    public UserTable(String name, String sex, Date birthday) {
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
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
                ", birthday=" + birthday.toLocaleString() +
                ", sex='" + sex + '\'' +
                '}';
    }
}
