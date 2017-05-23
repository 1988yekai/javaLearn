package qz.bigdata.crawler.utility;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.File;
import java.util.List;

/**
 * Created by fys on 2015/1/15.
 */
public class ConfigurationParser {

    private static final Logger logger = Logger.getLogger(ConfigurationParser.class);

    private static ConfigurationParser parser = null;
    private static SAXReader reader = null;
    private static Document document = null;
    private static Element root = null;

    private ConfigurationParser(){

        reader = new SAXReader();

    }


    //每次获得Instance时  总是把覆盖前面的instance
    public static ConfigurationParser GetInstance(String filePath)
    {

        if(parser == null)
        {
            parser = new ConfigurationParser();
        }

        try {
            //get the document we want to read
            document = reader.read(new File(filePath));

            //1.获取文档的根节点.
            root = document.getRootElement();

        } catch (DocumentException e) {

            logger.error("failed to read the file:"+filePath);
            //e.printStackTrace();
        }

        return parser;
    }

    public String readString(String name)
    {

        //2.取得某节点的单个子节点.
        Element memberElm=root.element(name);// "member"是节点名
        //3.取得节点的文字
        String text=memberElm.getText();

//        4.取得某节点下名为"member"的所有字节点并进行遍历.
//        List nodes = rootElm.elements("member");
//
//        for (Iterator it = nodes.iterator(); it.hasNext();) {
//           Element elm = (Element) it.next();
//           // do something
//        }

        return text;
    }

    public double readDouble(String name)
    {
        //2.取得某节点的单个子节点.
        Element memberElm=root.element(name);// "member"是节点名
        //3.取得节点的文字
        String text=memberElm.getText();
        //should be checked weather text is the string of a double
        double dig = Double.parseDouble(text);

        return dig;
    }

    public int readInt(String name)
    {

        //2.取得某节点的单个子节点.
        Element memberElm=root.element(name);// "member"是节点名
        //3.取得节点的文字
        String text=memberElm.getText();
        //should be checked weather text is the string of a int
        int dig = Integer.parseInt(text);

        return dig;
    }

    public boolean readBoolean(String name){

        //2.取得某节点的单个子节点.
        Element memberElm=root.element(name);// "member"是节点名
        //3.取得节点的文字
        String text=memberElm.getText();

        if (text.equalsIgnoreCase("yes")) {

            return true;

        } else if (text.equalsIgnoreCase("no")) {

            return false;

        } else {

            logger.error(name + "'s value in not a boolean variable");
        }

        //默认不使用hdfs
        return false;
    }

    public boolean readSeedUrls(List<String> urllist) {


        return false;
    }
}

