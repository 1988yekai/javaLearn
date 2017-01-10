package annotationGeneric;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yek on 2017-1-6.
 */
public class BaseService <T>{
    @Autowired
    private BaseDao<T> dao;

    public void add() {
        System.out.println("BaseService add.");
        System.out.println(dao);
    }
}
