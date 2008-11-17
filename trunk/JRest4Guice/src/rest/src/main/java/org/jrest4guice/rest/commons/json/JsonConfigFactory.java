package org.jrest4guice.rest.commons.json;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.jrest4guice.rest.commons.json.annotations.JsonExclude;

public class JsonConfigFactory {
	public static JsonConfig createJsonConfig(Object bean) {
		JsonConfig jsonConfig = new JsonConfig();
		if(bean == null)
			return jsonConfig;
		if (bean instanceof List) {
			try {
				bean = ((List<?>) bean).get(0);
			} catch (Exception e) {
			}
		}
		
		if(bean == null)
			return jsonConfig;

		filteExcludes(bean, jsonConfig);
		return jsonConfig;
	}

	public static void filteExcludes(Object bean, JsonConfig jsonConfig) {
		jsonConfig.setJsonPropertyFilter(new PropertyFilter(){
			@Override
			public boolean apply(Object source, String name, Object value) {
				if(value == null)
					return true;
				try {
					Field field = source.getClass().getDeclaredField(name);
					if(field != null && field.isAnnotationPresent(JsonExclude.class))
						return true;
					if(ignoreLazyField(field))
						return true;
				} catch (Exception e) {
				}
				return false;
			}
		});
	}

	private static boolean ignoreLazyField(Field field) {
		return (field.isAnnotationPresent(OneToOne.class) && ((OneToOne)field.getAnnotation(OneToOne.class)).fetch()== FetchType.LAZY)
				|| (field.isAnnotationPresent(OneToMany.class) && ((OneToMany)field.getAnnotation(OneToMany.class)).fetch()== FetchType.LAZY)
				|| (field.isAnnotationPresent(ManyToOne.class) && ((ManyToOne)field.getAnnotation(ManyToOne.class)).fetch()== FetchType.LAZY)
				|| (field.isAnnotationPresent(ManyToMany.class) && ((ManyToMany)field.getAnnotation(ManyToMany.class)).fetch()== FetchType.LAZY);
	}
}
