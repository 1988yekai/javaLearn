package annotation;

import annotation.controller.UserController;
import annotation.repository.UserRepository;
import annotation.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yek on 2017-1-3.
 */
public class MainTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans_annotation.xml");
        TestObject testObject = (TestObject) context.getBean("testObject");
        System.out.println(testObject);
        UserController userController = (UserController) context.getBean("userController");
        userController.execute();
//        UserService userService = (UserService) context.getBean("userService");
//        userService.add();
//
//        UserRepository userRepository = (UserRepository) context.getBean("userRepository");
//        System.out.println(userRepository);
//        userRepository.save();

    }
}
