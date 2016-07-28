package po;
import java.util.Date;

/**
 * Created by yek on 2016-7-20.
 */
//当前的类是一个持久化类，是Category这个类。他映射了一个表category。所对应的 数据库是users
//这句：@Table(name = "category", catalog = "users")  可以省略
//@Entity
//@Table(name = "User_Table", catalog = "test")
public class UserTable {


    private int id;
    private String name;
    private Date birthday;
    private String sex;

    public UserTable() {
    }

    public UserTable(String name, Date birthday, String sex) {
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
    }

    public UserTable(int id, String name, Date birthday, String sex) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
    }
    // 主键 ：@Id    主键生成方式：strategy = "increment"
    //映射表中id这个字段，不能为空，并且是唯一的
//    @GenericGenerator(name = "generator", strategy = "increment")
//    @Id
//    @GeneratedValue(generator = "generator")
//    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    @Column(name = "birthday" )
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

//    @Column(name = "sex")
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
                ", birthday=" + birthday +
                ", sex='" + sex + '\'' +
                '}';
    }
}
