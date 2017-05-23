package qz.bigdata.crawler.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 后台Console输入命令 
 * start、stop、suspend、status、resume 等
 * 通过反射目标类方法
 * 实现启动爬虫、停止爬虫、暂停爬虫、
 * 爬虫当前状态、重启爬虫等操作
 * @author yangd
 *
 */
public class CommandShell {
	
	public static Logger logger = Logger.getLogger("CommandShell");
	/**
	 * 命令行输入控制 爬虫运行。
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("请输入操作命令:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));   	  
    	//把ExampleEntry 这个类载入内存   
		Class exampleEntryClass = null;
		try {
			exampleEntryClass = Class.forName("us.codecraft.webmagic.selenium.examples.ExampleEntry");
		} catch (ClassNotFoundException e1) {
			logger.error("", e1);
		}
		//创建这个类的实例   
		Object obj = null;
		try {
			obj = exampleEntryClass.newInstance();
		} catch (InstantiationException e1) {
			logger.error("", e1);
		} catch (IllegalAccessException e1) {
			logger.error("", e1);
		}
        String cmds = null;     
        String[] cmdArray = args;		//启动程序时的 参数args数组
        try {        	
			while(true){ 
				//方法名称
				String mathodName = "";
				//方法传递的参数
				String[] mathodParam = null;
				if(cmdArray == null || cmdArray.length == 0 ){		
					cmds = br.readLine();
					if("".equals(cmds.trim())){		//判断console没有输入任何内容时继续循环
						continue;
					}
					//得到方法名称 String
					mathodName = getCmdInputMathodName(cmds.trim());
					//得到方法参数变量 String[]
					List<String> mathodParamList = getCmdInputMathodParam(cmds.trim());
					if(mathodParamList.size() > 0){
						mathodParam = new String[mathodParamList.size()];
						mathodParam = mathodParamList.toArray(mathodParam);	
					}
				}else{ 												//表示启动程序时已输入命令和参数
					//得到方法名称 String
					mathodName = getCmdInputMathodName(cmdArray);
					//得到方法参数变量 String[]
					List<String> mathodParamList = getCmdInputMathodParam(cmdArray);
					if(mathodParamList.size() > 0){
						mathodParam = new String[mathodParamList.size()];
						mathodParam = mathodParamList.toArray(mathodParam);				
					}
					cmdArray = null;
				}
				//判断方法 和参数
				if(mathodName != null && !"".equals(mathodName)){
					if(mathodParam == null || mathodParam.length ==0){		//没有参数
						//通过class来取得这个方法对象  			    	
						java.lang.reflect.Method method = null;
						try {
							method = exampleEntryClass.getMethod(mathodName);
						} catch (NoSuchMethodException e) {
							System.out.println("输入的不是规范的操作命令！请再次输入：");
							continue;
						}   
						try{
							//invoke 来执行方法对象 
							method.invoke(obj);
						}catch(Exception ex){
							System.out.println("命令执行异常！" + ex.getMessage());
							logger.error("",ex);
						}
					}else{
						List<Class> arrayList = new ArrayList<Class>();
						for(int i=0; i<mathodParam.length; i++){
							arrayList.add(String.class);
						}
						//通过class来取得这个方法对象  			    	
						java.lang.reflect.Method method = null;
						try {
							Class[] classArray = new Class[arrayList.size()];
							classArray = arrayList.toArray(classArray);
							method = exampleEntryClass.getMethod(mathodName, classArray);
						} catch (NoSuchMethodException e) {
							System.out.println("输入的不是规范的操作命令！请再次输入：");
							continue;
						}   
						try{
							//invoke 来执行方法对象 
							method.invoke(obj,mathodParam);
						}catch(Exception ex){
							System.out.println("命令执行异常！" + ex.getMessage());
							logger.error("",ex);
						}
					}
			    }else{
			    	continue;
			    }
			}
		} catch (SecurityException e) {
			logger.error("", e);
		} catch (IllegalArgumentException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}  
       
	}
	/**
	 * 获取cmdInput中的方法名称
	 * @param cmdInput 系统输入内容
	 * @return 方法名称
	 */
	private static String getCmdInputMathodName(String cmdInput){
		if(cmdInput == null || "".equals(cmdInput)){
			return "";
		}else{
			String[] temp = cmdInput.split(" ");
			return temp[0].toLowerCase();
		}
	}
	/**
	 * 获取cmdInput中的方法参数
	 * @param cmdInput 系统输入内容
	 * @return 方法参数List<String>对象
	 */
	private static List<String> getCmdInputMathodParam(String cmdInput){
		if(cmdInput == null || "".equals(cmdInput)){
			return null;
		}else {
			if(cmdInput.indexOf(" \"") != -1 && cmdInput.indexOf(" ") != -1){		//录入字符串中带有双引号
				int index = cmdInput.indexOf(" ");
				List<String> paramList = new ArrayList<String>();
				
				for(int i=index+1; i < cmdInput.length(); i++){
					char paramChar = cmdInput.charAt(i);
					if(i==cmdInput.length()-1){ //字符串最后一位
						paramList.add(cmdInput.substring(index+1).trim());
					}else if(paramChar == ' ' && cmdInput.charAt(i-1) == '"' &&  cmdInput.charAt(i-2) != '\\'){   //判断参数的结尾 
						paramList.add(cmdInput.substring(index+1, i).trim());
						index = i;
					}else if(paramChar == ' ' && cmdInput.charAt(i+1) != ' '){
						String temp = cmdInput.substring(index+1, i).trim();
						if("".equals(temp) || temp.charAt(0) == '"'){	// 
							continue;
						}
						paramList.add(temp);
						index = i;
					}
				}
				return paramList;
			}else{												//录入字符串中不带双引号
				String[] temp = cmdInput.split(" ");
				return getCmdInputMathodParam(temp);
			}
		}
	}
	
	/**
	 * 获取cmdInput中的方法名称
	 * @param cmdInput 系统输入内容
	 * @return 方法名称
	 */
	private static String getCmdInputMathodName(String[] cmdInput){
		if(cmdInput == null || cmdInput.length == 0){
			return "";
		}else{
			return cmdInput[0].toLowerCase();
		}
	}
	/**
	 * 获取cmdInput中的方法参数
	 * @param cmdInput  系统输入内容
	 * @return 方法参数List<String>对象
	 */
	private static List<String> getCmdInputMathodParam(String[] cmdInput){
		List<String> paramArray = new ArrayList<String>();
		if(cmdInput == null || cmdInput.length < 2){
			return paramArray;
		}else{
			for(int i=1; i<cmdInput.length; i++){
				paramArray.add(cmdInput[i]);
			}
			return paramArray;
		}
	}
}
