package qz.bigdata.crawler.configuration;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;

/**
 * Created by Administrator on 14-4-10.
 */
public class GlobalOptionParser {

    public SAXReader reader = null;
    public Document document = null;
    public Element root = null;

    public GlobalOptionParser() {
    }

    /**
     * 初始化reader,document,root
     * @param filePath
     */
    public void getInstance(String filePath) {

        if(reader == null){
            reader = new SAXReader();
        }

        try {
            document = reader.read(new File(filePath));
            root = document.getRootElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Global initGlobalOption(String filePath){
        if(reader == null){
            reader = new SAXReader();
        }

        try {
            document = reader.read(GlobalOptionParser.class.getClassLoader().getResourceAsStream(filePath));
            root = document.getRootElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Global go = Global.getInstance();
        Element browserSettings = root.element("browserSettings");
        Element mainThread = root.element("mainThread");
        Element redisSettings = root.element("redisSettings");
        Element storageSettings = root.element("storageSettings");
        Element hdfsSettings = root.element("hdfsSettings");
        Element cloudcodeSettings = root.element("cloudcode");
        //browserSettings
        Iterator<?> sleepTimeIts = browserSettings.elementIterator();
        while(sleepTimeIts.hasNext()){
            Element  el  = (Element)sleepTimeIts.next();
            String name  = el.getQualifiedName();
            String value  = el.getTextTrim();
            go.setFunction(name,value);
        }

        //mainThread
        Iterator<?> threadNumbersIts = mainThread.elementIterator();
        while(threadNumbersIts.hasNext()){
            Element  el  = (Element)threadNumbersIts.next();
            String name  = el.getQualifiedName();
            String value  = el.getTextTrim();
            go.setFunction(name,value);
        }

        //redisSettings
        Iterator<?> redisIts = redisSettings.elementIterator();
        while(redisIts.hasNext()){
            Element el = (Element)redisIts.next();
            String name  = el.getQualifiedName();
            String value = el.getTextTrim();
            go.setFunction(name,value);
        }

        //storageSettings
        Iterator<?> storageIts = storageSettings.elementIterator();
        while (storageIts.hasNext()) {
            Element el = (Element) storageIts.next();
            String name = el.getQualifiedName();
            String value = el.getTextTrim();
            go.setFunction(name, value);

        }
        //hdfsSettings
        Iterator<?> hdfs = hdfsSettings.elementIterator();
        while (hdfs.hasNext()) {
            Element el = (Element) hdfs.next();
            String name = el.getQualifiedName();
            String value = el.getTextTrim();
            go.setFunction(name, value);
        }
        //cloudcodeSettings
        if(cloudcodeSettings != null){
            Iterator<?> cloudcodeIterator = cloudcodeSettings.elementIterator();
            while (cloudcodeIterator.hasNext()) {
                Element el = (Element) cloudcodeIterator.next();
                String name = el.getQualifiedName();
                String value = el.getTextTrim();
                go.setFunction(name, value);
            }
        }
            return go;
    }
}
