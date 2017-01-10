package annotation.repository;

import org.springframework.stereotype.Repository;

/**
 * Created by yek on 2017-1-3.
 */
@Repository(value = "userRepository")
public class UserRepositoryImpl implements UserRepository{
    public void save() {
        System.out.println("UserRepository save.");
    }
}
