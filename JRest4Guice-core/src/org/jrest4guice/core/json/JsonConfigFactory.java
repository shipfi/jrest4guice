package org.jrest4guice.core.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import net.sf.json.JsonConfig;

import org.jrest4guice.core.json.annotations.JsonExclude;

public class JsonConfigFactory {
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
		List<String> excludes = new ArrayList<String>();
		Class<?> clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(JsonExclude.class) || isAnnotationPresent4ORMRelation(f)) {
				excludes.add(f.getName());
			}
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
