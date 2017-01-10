package annotation.service;

import annotation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yek on 2017-1-3.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public void add(){
        System.out.println("UserService add.");
        userRepository.save();
    }
}
