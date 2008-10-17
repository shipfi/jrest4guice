package org.jrest4guice.rest.commons.json;

import java.lang.reflect.Field;
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
		JsonConfig jsConfig = new JsonConfig();
		if(bean == null)
			return jsConfig;
		if (bean instanceof List) {
			try {
				bean = ((List) bean).get(0);
			} catch (Exception e) {
			}
		}
		
		if(bean == null)
			return jsConfig;

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
		jsConfig.setExcludes(excludes.toArray(new String[] {}));
		return jsConfig;
	}

	private static boolean isAnnotationPresent4ORMRelation(Field field) {
		return field.isAnnotationPresent(OneToOne.class)
				|| field.isAnnotationPresent(OneToMany.class)
				|| field.isAnnotationPresent(ManyToOne.class)
				|| field.isAnnotationPresent(ManyToMany.class);
	}
}
