import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import po.UserTable;

import java.util.Date;

/**
 * Created by yek on 2016-7-21.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("test...");

        //1. 创建一个 SessionFactory 对象
        SessionFactory sessionFactory = null;

        //1). 创建 Configuration 对象: 对应 hibernate 的基本配置信息和 对象关系映射信息
        Configuration configuration = new Configuration().configure();

        //4.0 之前这样创建
//		sessionFactory = configuration.buildSessionFactory();

        //2). 创建一个 ServiceRegistry 对象: hibernate 4.x 新添加的对象
        //hibernate 的任何配置和服务都需要在该对象中注册后才能有效.
        ServiceRegistry serviceRegistry =
                new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
                        .build();
//        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();


        //3).
//        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
/*
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        //2. 根据服务注册类创建一个元数据资源集，同时构建元数据并生成应用一般唯一的的session工厂
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

* */
        //2. 创建一个 Session 对象
        Session session = sessionFactory.openSession();

        //3. 开启事务
        Transaction transaction = session.beginTransaction();

        //4. 执行保存操作
        UserTable user = new UserTable("小蓝", new Date(),"female");
        session.save(user);
        System.out.println("保存成功！");

        //5. 提交事务
        transaction.commit();

        //6. 关闭 Session
        session.close();

        //7. 关闭 SessionFactory 对象
        sessionFactory.close();
    }
}
