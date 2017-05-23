package qz.bigdata.crawler.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Dynamic {
	
	
	private String convert(String kind,String param) {
		String fnname;
		String oldChar = param.substring(0, 1);
		String newChar = oldChar.toUpperCase();
		fnname = kind+param.replaceFirst(oldChar, newChar);
		return fnname;
	}

	
	private Method findMethod(String kind,String param){
		Method method = null;
		if(kind.equals("get")){
			try {
				method = this.getClass().getMethod
						(convert(kind,param));
			} catch (SecurityException e1) {
				
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				
			}
		}else{
			Field field = null;
			try {
				field = this.getClass().getDeclaredField(param);
				method = this.getClass().getMethod
						(convert(kind,param),field.getType());
			} catch (SecurityException e1) {
				
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				
			}
			catch (NoSuchMethodException e) {
				
			}
		}
		return method;
	}

	public <T> boolean setFunction(String param,T value){		
		Method method = findMethod("set",param);
		
		if(method != null){
			Class type = method.getParameterTypes()[0];
			try {
				if(type.equals(int.class)){
					int realvalue = new Integer((String)value);
					method.invoke(this, realvalue);
				}
				
				else if(type.equals(long.class)){
					long realvalue = new Long((String)value);
					method.invoke(this, realvalue);
				}
				
				else if(type.equals(float.class)){
					float realvalue = new Float((String)value);
					method.invoke(this, realvalue);
				}
				
				else if(type.equals(boolean.class)){
					boolean realvalue = new Boolean((String)value);
					method.invoke(this, realvalue);
				}
				
				else{
					method.invoke(this, value);
				}
				
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				
				e.printStackTrace();
			}
			return true;
		}else{
			return false;
		}
		
	}


	public <T> T getFunction(String param){
		Method method = findMethod("get",param);;
		
		if(method != null){
			try {
				return (T)method.invoke(this);
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
