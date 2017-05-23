package qz.bigdata.crawler.store.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import qz.bigdata.crawler.utility.TransformUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
* Created by fys on 2015/1/27.
*/
public class HBaseOperation {
    private static Configuration conf = null;

    /**
     * 初始化配置
     */
    static {
        Configuration HBASE_CONFIG = new Configuration();
        //与hbase/conf/hbase-site.xml中hbase.zookeeper.quorum配置的值相同
        HBASE_CONFIG.set("hbase.zookeeper.quorum", "slaver4,slaver2,slaver1");  //yangd 修改 由于集群发生变更。
//        HBASE_CONFIG.set("hbase.zookeeper.quorum", "192.168.13.154,192.168.13.152,192.168.13.151");
        //与hbase/conf/hbase-site.xml中hbase.zookeeper.property.clientPort配置的值相同
        HBASE_CONFIG.set("hbase.zookeeper.property.clientPort", "2181");
        conf = HBaseConfiguration.create(HBASE_CONFIG);
    }

    /**
     * 创建一张表
     */
    public static void creatTable(String tableName,String[] familys,int verCount) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        int version = 3;
        if (verCount > 1){
            version = verCount;
        }

        if (admin.tableExists(tableName)) {
            System.out.println("table already exists!");
        } else {
            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            for(int i=0; i<familys.length; i++){
                tableDesc.addFamily(new HColumnDescriptor(familys[i]).setMaxVersions(version));
            }
            admin.createTable(tableDesc);
            System.out.println("create table " + tableName + " ok.");
        }
    }
    public static void creatTable(String tableName,String[] familys) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        if (admin.tableExists(tableName)) {
            System.out.println("table already exists!");
        } else {
            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            for(int i=0; i<familys.length; i++){
                tableDesc.addFamily(new HColumnDescriptor(familys[i]));
            }
            admin.createTable(tableDesc);
            System.out.println("create table " + tableName + " ok.");
        }
    }

    /**
     * 删除表
     */
    public static void deleteTable(String tableName) throws Exception {
        try {
            HBaseAdmin admin = new HBaseAdmin(conf);
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
            System.out.println("delete table " + tableName + " ok.");
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入一行记录
     */
    public static void addRecord (String tableName, String rowKey, List<HbColumnFamily> families)
            throws Exception{
        HTable table = null;
        try {
            table = new HTable(conf, tableName);
            Put put = new Put(Bytes.toBytes(rowKey));
            for(HbColumnFamily f : families){
                if(f.columns != null && f.columns.entrySet() != null) {
                    Iterator<Map.Entry<String, Object>> iter = f.columns.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        put.add(Bytes.toBytes(f.family),
                                Bytes.toBytes(entry.getKey()),
                                TransformUtility.ObjectToByte(entry.getValue()));
                    }
                }
            }
            table.put(put);
            System.out.println("insert recored " + rowKey + " to table " + tableName +" ok.");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if(table != null)
                table.close();
        }
    }

    public static void addRecord (String tableName, String rowKey, String[][] famCloumns, Object[] values)
            throws Exception{
        HTable table = null;
        if(famCloumns[0].length != famCloumns[1].length || famCloumns[0].length != values.length){
            throw new IllegalArgumentException("length of family, column, and value must be equal.");
        }
        try {
            table = new HTable(conf, tableName);
            Put put = new Put(Bytes.toBytes(rowKey));
            int len = values.length;
            for(int i = 0; i < len; i++){
                String family = famCloumns[0][i];
                String column = famCloumns[1][i];
                put.add(Bytes.toBytes(family),
                        Bytes.toBytes(column),
                        TransformUtility.ObjectToByte(values[i]));
            }
            table.put(put);
            System.out.println("insert recored " + rowKey + " to table " + tableName +" ok.");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if(table != null)
                table.close();
        }
    }

    /**
     * 删除一行记录
     */
    public static void delRecord (String tableName, String rowKey) throws IOException{
        HTable table = new HTable(conf, tableName);
        List list = new ArrayList();
        Delete del = new Delete(rowKey.getBytes());
        list.add(del);
        table.delete(list);
        System.out.println("del recored " + rowKey + " ok.");
    }

    /**
     * 查找一行记录
     */
    public static void getOneRecord (String tableName, String rowKey) throws IOException{
        HTable table = new HTable(conf, tableName);
        Get get = new Get(rowKey.getBytes());
        Result rs = table.get(get);
        for(KeyValue kv : rs.raw()){
            System.out.print(new String(kv.getRow()) + " " );
            System.out.print(new String(kv.getFamily()) + ":" );
            System.out.print(new String(kv.getQualifier()) + " " );
            System.out.print(kv.getTimestamp() + " " );
            System.out.println(new String(kv.getValue()));
        }
    }

    /**
     * 显示所有数据
     */
    public static void getAllRecord (String tableName) {
        try{
            HTable table = new HTable(conf, tableName);
            Scan s = new Scan();
            ResultScanner ss = table.getScanner(s);
            for(Result r:ss){
                for(KeyValue kv : r.raw()){
                    System.out.print(new String(kv.getRow()) + " ");
                    System.out.print(new String(kv.getFamily()) + ":");
                    System.out.print(new String(kv.getQualifier()) + " ");
                    System.out.print(kv.getTimestamp() + " ");
                    System.out.println(new String(kv.getValue()));
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


}
