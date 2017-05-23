package qz.bigdata.crawler.monitor;

import qz.bigdata.crawler.store.redis.IHashSetObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by fys on 2015/4/16.
 */
public class CrawlerNode implements IHashSetObject{
    public CrawlerProject project;
//    public String project;
//    public String pv; //project version
    public String module;
    public String version; //module version
    public String uuid;
    public String ip;
    public String startTime;
    public String endTime;
    public CrawlerStatus status;

    public CrawlerNode(){

    }
    public CrawlerNode(CrawlerProject project, String module, String version, String ip){
        this.project = project;
        this.module = module;
        this.version = version;
        this.uuid = UUID.randomUUID().toString();
        this.ip = ip;
        this.startTime = new Date().toString();
        this.endTime = this.startTime;
        this.status = CrawlerStatus.Running;
    }
    @Override
    public String myKey() {
        return this.project.myKey() + "_" + this.project.myField();
    }

    @Override
    public String myField() {
        return this.uuid;
    }
}
