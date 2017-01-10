package annotationGeneric;

/**
 * Created by yek on 2017-1-6.
 */
public class BaseDao<T> {
    public void save(T entity){
        System.out.println( "BaseDao save:" + entity);
    }
}
