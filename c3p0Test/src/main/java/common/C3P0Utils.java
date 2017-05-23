package common;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;

/**
 * Created by yek on 2017-3-22.
 */
public final class C3P0Utils {
    private static C3P0Utils instance;
    private static ComboPooledDataSource dataSource;

//    static {
//        dataSource = new ComboPooledDataSource("mysqlApp");
//    }
    private C3P0Utils() {
        dataSource = new ComboPooledDataSource("mysqlApp");
    }
    //获得ConnectionManager对象实例
    public static final C3P0Utils getInstance(){
        try{
            if(instance==null){
                instance=new C3P0Utils();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return instance;
    }
    //获得数据库连接
    public synchronized final Connection getConnection(){
        Connection conn=null;
        try{
            conn=dataSource.getConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }
}
