package qz.bigdata.crawler.store.redis;

/**
 * Created by fys on 2015/4/20.
 */
public class RedisDistributedObjectManager {
    private String redisIP;
    private String redisPort;
    private String redisPassword;
    private JsonPooledRedisClient client;

    public RedisDistributedObjectManager(String ip, int port, String password){
        this.redisIP = ip;
        this.redisPort = Integer.toString(port);
        this.redisPassword = password;
        client = JsonPooledRedisClient.getJsonPooledRedisClient(ip, port, password);
    }

    public void set(Object data){

        if( data instanceof IKeyValueObject){
            String key = ((IKeyValueObject)data).getKey();
            this.client.set(key, data);
        }
        else if(data instanceof  IHashSetObject){
            String key = ((IHashSetObject)data).myKey();
            String field = ((IHashSetObject)data).myField();
            client.hset(key, field, data);
        }
        else{
            throw new IllegalArgumentException("Argument data should be instance of interface IKeyValueObject or IHashSetObject.");
        }
    }
    public Object get(Object data){
        if( data instanceof IKeyValueObject){
            String key = ((IKeyValueObject)data).getKey();
            return this.client.get(key, data.getClass());
        }
        else if(data instanceof  IHashSetObject){
            String key = ((IHashSetObject)data).myKey();
            String field = ((IHashSetObject)data).myField();
            return this.client.hget(key, field, data.getClass());
        }
        else{
            throw new IllegalArgumentException("Argument data should be instance of interface IKeyValueObject or IHashSetObject.");
        }
    }
    public Object get(String key, Class dataClass){
        return this.client.get(key, dataClass);
    }

    public Object get(String key, String field, Class dataClass){
        return this.client.hget(key, field, dataClass);
    }
}
