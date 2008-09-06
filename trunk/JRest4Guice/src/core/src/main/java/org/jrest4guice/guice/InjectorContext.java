package org.jrest4guice.guice;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InjectorContext {
	private static final ThreadLocal<Field> field = new ThreadLocal<Field>();
	private static final ThreadLocal<Method> method = new ThreadLocal<Method>();
	private static final ThreadLocal<Object> instace = new ThreadLocal<Object>();
	
	private InjectorContext(){
	}
	
	public static void setCurrentField(Field f){
		field.set(f);
	}

	public static void clearCurrentField(){
		field.remove();
	}

	public static Field currentField(){
		return field.get();
	}

	public static void setCurrentMethod(Method m){
		method.set(m);
	}

	public static void clearCurrentMethod(){
		method.remove();
	}

	public static Method currentMethod(){
		return method.get();
	}

	public static void setCurrentInstace(Object i){
		instace.set(i);
	}

	public static void clearCurrentInstace(){
		instace.remove();
	}

	public static Object currentInstace(){
		return instace.get();
	}
}
