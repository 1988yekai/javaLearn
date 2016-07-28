package hibernate5need_java8;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.Session;
import org.hibernate.query.Query;
import hibernate5need_java8.po.UserTable;

/**
 * Created by yek on 2016-6-16.
 */
public class Main1 {
    public static void main(String[] args) {
        System.out.println("test...");

        //1. 配置类型安全的准服务注册类，这是当前应用的单例对象，不作修改，所以声明为final
        //在configure("cfg/hibernate.cfg.xml")方法中，如果不指定资源路径，默认在类路径下寻找名为hibernate.cfg.xml的文件
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        //2. 根据服务注册类创建一个元数据资源集，同时构建元数据并生成应用一般唯一的的session工厂
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        /* ***上面是配置准备，下面开始我们的数据库操作***** */
        Session session = sessionFactory.openSession();//从会话工厂获取一个session

        //3. 开启事务
        Transaction transaction = session.beginTransaction();

        //4. 执行保存操作
        //add
//       UserTable user1 = new UserTable("Java12345", "male", new Date());
//        session.save(user1);
        //select
        UserTable user = new UserTable();
        String hql = "select u from UserTable u";
        Query query = session.createQuery(hql);
        System.out.println(query.list());


        //5. 提交事务
        transaction.commit();

        //6. 关闭 Session
        session.close();

        //7. 关闭 SessionFactory 对象
        sessionFactory.close();
    }
}
