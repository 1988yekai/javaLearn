package annotationGeneric;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yek on 2017-1-6.
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_annotation.xml");
        UserService service = (UserService) context.getBean("userService1");
        service.add();
    }
}
