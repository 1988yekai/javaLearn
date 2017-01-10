package annotation.controller;

import annotation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by yek on 2017-1-3.
 */
@Controller
public class UserController {
    @Autowired
    private UserService service;
    public void execute() {
        System.out.println("UserController execute.");
        service.add();
    }
}
