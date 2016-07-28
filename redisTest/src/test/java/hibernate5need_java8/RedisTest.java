package hibernate5need_java8;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016-7-19.
 */
public class RedisTest {
    // 连接实例
    @Test
    public void test1(){
        Jedis jedis = new Jedis("127.0.0.1",6379);
        System.out.println("Connect to server successfully!");
        System.out.println("Server is running: " + jedis.ping());
    }

    //设置并读取字符串实例
    @Test
    public void test2(){
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connect to server successfully!");
//        System.out.println("Server is running: " + jedis.ping());
        //设置 redis 字符串数据
        jedis.set("badWords", "直接了当");
        // 获取存储的数据并输出
        System.out.println("Stored string in redis:: "+ jedis.get("badWords"));
    }
    //Redis Java List(列表) 实例
    @Test
    public void test3(){
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connect to server successfully!");
        // //存储数据到列表中
        jedis.lpush("name","张三");
        jedis.lpush("name","李四");
        jedis.lpush("name","王五");
        //获取数据输出
        List<String> names = jedis.lrange("name",0,10);
        for (String name: names)
            System.out.println("name:" + name);
        // close
        jedis.close();
    }

    //Redis Java Keys 实例
    @Test
    public void test4(){
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connect to server successfully!");
        //get keys
        Set<String> keys = jedis.keys("*");
        for (String key : keys)
            System.out.println("key:" + key);

        // remove
//        Set<String> keys = jedis.keys("*");
        for (String key : keys)
            jedis.del(key);

        keys = jedis.keys("*");
        System.out.println("key is empty:"+keys.isEmpty());


        jedis.close();
    }


}
