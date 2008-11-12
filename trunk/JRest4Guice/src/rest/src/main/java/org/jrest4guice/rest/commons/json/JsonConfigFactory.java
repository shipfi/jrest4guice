package org.jrest4guice.rest.commons.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import net.sf.json.JsonConfig;

import org.jrest4guice.rest.commons.json.annotations.JsonExclude;

public class JsonConfigFactory {
	private static Map<String, List<String>> excudeMap = new HashMap<String, List<String>>(0);
	
	public static JsonConfig createJsonConfig(Object bean) {
		JsonConfig jsonConfig = new JsonConfig();
		if(bean == null)
			return jsonConfig;
		if (bean instanceof List) {
			try {
				bean = ((List) bean).get(0);
			} catch (Exception e) {
			}
		}
		
		if(bean == null)
			return jsonConfig;

		filteExcludes(bean, jsonConfig);
		return jsonConfig;
	}

	public static void filteExcludes(Object bean, JsonConfig jsonConfig) {
		List<String> excludes = null;
		String name = bean.getClass().getName();
		if(excudeMap.containsKey(name)){
			excludes = excudeMap.get(name);
		}else{
			excludes = new ArrayList<String>();
			Class<?> clazz = bean.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field f : fields) {
				if (f.isAnnotationPresent(JsonExclude.class) || isAnnotationPresent4ORMRelation(f)) {
					excludes.add(f.getName());
				}
			}
			excudeMap.put(name, excludes);
		}

		Class<?> clazz = bean.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		Object invoke;
		String mName;
		for (Method m : methods) {
			mName = m.getName();
			if(mName.startsWith("get")){
				try {
					invoke = m.invoke(bean);
					if(invoke == null){
						excludes.add(mName.substring(3,4).toLowerCase()+ mName.substring(4));
					}
				} catch (Exception e) {
				}
			}
		}
		
		jsonConfig.setExcludes(excludes.toArray(new String[] {}));
	}

	private static boolean isAnnotationPresent4ORMRelation(Field field) {
		return field.isAnnotationPresent(OneToOne.class)
				|| field.isAnnotationPresent(OneToMany.class)
				|| field.isAnnotationPresent(ManyToOne.class)
				|| field.isAnnotationPresent(ManyToMany.class);
	}
}
