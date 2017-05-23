package test1;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import common.C3P0Utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by yek on 2017-3-22.
 */
public class Main1 {
    static DataSource dataSource;

    static {
        dataSource = new ComboPooledDataSource("mysqlApp");
    }

    static Connection getConnection() throws SQLException {
        if (dataSource != null)
            return dataSource.getConnection();
        else
            return null;
    }

    public static void main(String[] args) throws SQLException {
//        System.out.println(getConnection());
        Connection connection = C3P0Utils.getInstance().getConnection();

        String sql = "select * from user_table";

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String birthday = resultSet.getString(3);
            String sex = resultSet.getString(4);
            System.out.println(id +" "+name+" "+birthday+" "+sex);
        }

        connection.close();
    }
}
