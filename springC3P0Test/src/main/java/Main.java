import bean.UserBean;
import dao.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yek on 2016-8-16.
 */
public class Main {
    @Test
    public void test1(){

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");//读取bean.xml中的内容
        UserBean user = ctx.getBean("user",UserBean.class);//创建bean的引用对象
        System.out.println(user);
    }
    @Test
    public void mybatisTest2(){
        //根据 spring-config.xml 初始化 spring
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
        //得到mybatis的SqlSessionFactory
        SqlSessionFactory obj = (SqlSessionFactory) ctx.getBean(
                "sqlSessionFactory", DefaultSqlSessionFactory.class);
        //创建mybatis的SqlSession
        SqlSession ses = obj.openSession();
        UserMapper userMapper = ses.getMapper(UserMapper.class);
        UserBean userBean = userMapper.getById(2);
        System.out.println(userBean);

        int countNum = userMapper.count();
        System.out.println(countNum);

        ses.close();
    }
}
