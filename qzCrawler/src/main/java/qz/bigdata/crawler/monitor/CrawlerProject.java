package qz.bigdata.crawler.monitor;

import qz.bigdata.crawler.store.redis.IHashSetObject;
import qz.bigdata.crawler.store.redis.IKeyValueObject;

/**
 * Created by fys on 2015/4/16.
 */
public class CrawlerProject implements IHashSetObject {
    public String name;
    public String version;

    public CrawlerProject(){

    }
    public CrawlerProject(String name, String version){
        this.name = name;
        this.version = version;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getVersion(){
        return this.version;
    }
    public void setVersion(String version){
        this.version = version;
    }
    @Override
    public String myKey() {
        return "crawler_projects";
    }

    @Override
    public String myField() {
        return this.name + "_" + this.version;
    }
    //public CrawlerStatus status; //表示Project当前状态
    //public CrawlerStatus flag;  //表示通知/控制project的命令
}
