package javascriptTest;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

/**
 * Created by Administrator on 2017/4/10.
 */
public class TestMain1 {
    public static void main(String[] args) throws Exception{
        String language ="javascript";
        ScriptEngineManager manager = new ScriptEngineManager();
        System.out.println("Available factories: ");
        for (ScriptEngineFactory factory : manager.getEngineFactories())
            System.out.println(factory.getEngineName());
        final ScriptEngine engine = manager.getEngineByName(language);
        if (engine == null)
        {
            System.err.println("No engine for " + language);
            System.exit(1);
        }
        engine.eval("var state=\"0,国内_1,外省_2,其他_3,三明市_4,莆田市_5,南平市_6,宁德市_7,龙岩市_8,漳州市_9,厦门市_10,泉州市_11,福州市\".split(\"_\");\n" +
                "var type=\"0,省外_1,省内_2,国际_3,国内\".split(\"_\");\n" +
                "var industry=\"0,信息化_1,工程监理_3,建筑智能化_4,土石方工程_5,消防设施工程_6,产权交易_7,土地矿产交易_8,房屋建筑_9,工程勘察_10,水利水电_11,公路工程_12,土木建筑_13,水利_14,市政公用工程_15,招标代理机构_16,农林牧渔_17,电力_18,体育场地设施_19,能源化工_20,其它_21,出版印刷_22,环保_23,交通运输_24,桥梁_25,网络通讯计算机_26,房地产建筑_27,机械电子电器_28,医疗卫生_29,港口与航道_30,建筑装饰装修\".split(\"_\");\n" +
                "var money=\"0,国内政府资金_1,国内商业融资_2,世界银行贷款_3,亚洲开发银行贷款_4,外国政府贷款_5,国际开发协会_6,自筹资金_7,其它\".split(\"_\");\n" +
                "var zb_type=\"0,公开招标_1,邀请招标_2,代建制\".split(\"_\");\n" +
                "\n" +
                "\n" +
                "\n" +
                "function getState(value,list){\n" +
                "\tvar child;\n" +
                "\tif(value!=null&&!isNaN(value)){\n" +
                "\t\tfor(var i=0;i<list.length;i++){\n" +
                "\t\t\tchild=list[i].split(\",\");\n" +
                "\t\t\tif(parseInt(child[0])==parseInt(value)){\n" +
                "\t\t\t\treturn child[1];\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\treturn \"\";\n" +
                "}" +
                "var temp1 = getState('12',industry)");
        System.out.println();
        System.out.println(engine.get("temp1"));
        Invocable invoke = (Invocable)engine;
        String result = (String) invoke.invokeFunction("getState","\'12\'","industry");
        System.out.println("==> "+result);
    }
}
