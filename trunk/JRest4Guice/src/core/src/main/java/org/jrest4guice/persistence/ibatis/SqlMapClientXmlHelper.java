package org.jrest4guice.persistence.ibatis;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.apache.commons.lang.ObjectUtils.Null;
import org.jrest4guice.persistence.ibatis.annotations.Delete;
import org.jrest4guice.persistence.ibatis.annotations.Insert;
import org.jrest4guice.persistence.ibatis.annotations.Select;
import org.jrest4guice.persistence.ibatis.annotations.Update;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class SqlMapClientXmlHelper {
	public static String generateXmlConfig(Class clazz) {
		StringBuffer sb = new StringBuffer();
		Method[] methods = clazz.getDeclaredMethods();
		Select select;
		Update update;
		Insert insert;
		Delete delete;
		for (Method method : methods) {
			if (method.isAnnotationPresent(Select.class)) {
				select = method.getAnnotation(Select.class);
				generateMethodSqlMapping(sb, "select", method, select.id(),
						select.parameterClass(), select.resltClass(), select
								.sql());
			} else if (method.isAnnotationPresent(Update.class)) {
				update = method.getAnnotation(Update.class);
				generateMethodSqlMapping(sb, "update", method, update.id(),
						update.parameterClass(), Null.class, update
								.sql());
			} else if (method.isAnnotationPresent(Insert.class)) {
				insert = method.getAnnotation(Insert.class);
				generateMethodSqlMapping(sb, "insert", method, insert.id(),
						insert.parameterClass(), Null.class, insert
								.sql());
			} else if (method.isAnnotationPresent(Delete.class)) {
				delete = method.getAnnotation(Delete.class);
				generateMethodSqlMapping(sb, "delete", method, delete.id(),
						delete.parameterClass(), Null.class, delete
								.sql());
			}
		}

		return sb.toString();
	}

	private static void generateMethodSqlMapping(StringBuffer sb,
			String prefix, Method method, String id, Class parameterClazz,
			Class resultClazz, String sql) {
		sb.append("\n");

		String str, paramClass, resultClass;
		Class[] parameterTypes;
		Class returnType;

		if (parameterClazz == Null.class) {
			parameterTypes = method.getParameterTypes();
			if (parameterTypes != null && parameterTypes.length > 0) {
				parameterClazz = parameterTypes[0];
			}
		}

		if (parameterClazz != Null.class) {
			paramClass = " parameterClass=\"" + parameterClazz.getName() + "\"";
		} else
			paramClass = "";

		if (resultClazz == Null.class) {
			returnType = method.getReturnType();
			if (!returnType.getName().equals("void")) {
				resultClazz = returnType;
				final Type genericType = method.getGenericReturnType();
		    	if(genericType instanceof ParameterizedTypeImpl){
		    		ParameterizedTypeImpl pgType = (ParameterizedTypeImpl)genericType;
		    		Type[] actualTypeArguments = pgType.getActualTypeArguments();
		    		resultClazz = (Class<?>)actualTypeArguments[0];
		    	}
			}
		}

		if (resultClazz != Null.class) {
			final Type genericSuperclass = resultClazz.getGenericSuperclass();
			resultClass = " resultClass=\"" + resultClazz.getName() + "\"";
		} else
			resultClass = "";

		sb.append("<" + prefix + " id=\"" + id + "\"" + paramClass
				+ resultClass + ">\n");
		sb.append(sql.trim());
		sb.append("\n</" + prefix + ">");
	}
}
