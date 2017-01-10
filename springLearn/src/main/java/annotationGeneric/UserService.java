package annotationGeneric;

import org.springframework.stereotype.Service;

/**
 * Created by yek on 2017-1-6.
 */
//若注解没有指定 bean 的 id, 则类名第一个字母小写即为 bean 的 id
@Service(value = "userService1")
public class UserService extends BaseService<User>{

}
