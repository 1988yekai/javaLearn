import bean.Car;
import bean.HelloWorld;
import bean.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by yek on 2016-8-18.
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        /*HelloWorld helloWorld = (HelloWorld) applicationContext.getBean("helloWorld");
        HelloWorld hello1 = (HelloWorld) applicationContext.getBean("hello1");
        hello1.sayHello();
        helloWorld.sayHello();
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);*/
//        List<Person> persons = (List<Person>) applicationContext.getBean("persons");
//        System.out.println(persons);

//        不能实例化
//        Car car = (Car) applicationContext.getBean("car");
        Car car2 = (Car) applicationContext.getBean("car2");
        System.out.println(car2);
    }
}
