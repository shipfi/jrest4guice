package org.jrest4guice.persistence.ibatis;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils.Null;
import org.jrest4guice.persistence.ibatis.annotations.Cachemodel;
import org.jrest4guice.persistence.ibatis.annotations.Delete;
import org.jrest4guice.persistence.ibatis.annotations.Insert;
import org.jrest4guice.persistence.ibatis.annotations.ParameterMap;
import org.jrest4guice.persistence.ibatis.annotations.Procedure;
import org.jrest4guice.persistence.ibatis.annotations.Property;
import org.jrest4guice.persistence.ibatis.annotations.Result;
import org.jrest4guice.persistence.ibatis.annotations.ResultMap;
import org.jrest4guice.persistence.ibatis.annotations.Select;
import org.jrest4guice.persistence.ibatis.annotations.Statement;
import org.jrest4guice.persistence.ibatis.annotations.Update;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * 负责从类中提取SqlMap的配置信息，以xml字符串的形式返回
 * 
 * @author cnoss
 * 
 */
public class SqlMapClientXmlHelper {
	private static Set<String> parameterMapIds = new HashSet<String>(0);
	private static Set<String> resultMapIds = new HashSet<String>(0);
	private static Set<String> cacheModelIds = new HashSet<String>(0);

	public static SqlMapping generateXmlConfig(Class<?> clazz) {

		SqlMapping mapping = new SqlMapping();

		// 处理cacheModel
		StringBuffer cacheModelSb = new StringBuffer();
		processCacheModel(clazz, cacheModelSb);

		// 处理参数映射字典
		StringBuffer parameterMapSb = new StringBuffer();
		processPrameterMap(clazz, parameterMapSb);

		// 处理结果映射字典
		StringBuffer resultMapSb = new StringBuffer();
		processResultMap(clazz, resultMapSb);

		// 处理Sql语句
		StringBuffer statementSb = new StringBuffer();
		processStatement(clazz, statementSb);

		mapping.setParameterMap(parameterMapSb.toString());
		mapping.setResultMap(resultMapSb.toString());
		mapping.setStatement(statementSb.toString());
		return mapping;
	}

	private static void processStatement(Class<?> clazz,
			StringBuffer statementSb) {
		Method[] methods = clazz.getDeclaredMethods();
		Statement statement;
		Procedure procedure;
		Select select;
		Update update;
		Insert insert;
		Delete delete;
		for (Method method : methods) {
			if (method.isAnnotationPresent(Statement.class)) {
				statement = method.getAnnotation(Statement.class);
				generateMethodSqlMapping(statementSb, "statement", method,
						statement.id(), statement.parameterMap(), statement
								.parameterClass(), statement.resltMap(),
						statement.resltClass(), statement.sql(), statement
								.cacheModel(), statement.xmlResultName());
			} else if (method.isAnnotationPresent(Procedure.class)) {
				procedure = method.getAnnotation(Procedure.class);
				generateMethodSqlMapping(statementSb, "procedure", method,
						procedure.id(), procedure.parameterMap(), procedure
								.parameterClass(), procedure.resltMap(),
						procedure.resltClass(), procedure.sql(), null,
						procedure.xmlResultName());
			} else if (method.isAnnotationPresent(Select.class)) {
				select = method.getAnnotation(Select.class);
				generateMethodSqlMapping(statementSb, "select", method, select
						.id(), select.parameterMap(), select.parameterClass(),
						select.resltMap(), select.resltClass(), select.sql(),
						select.cacheModel(), null);
			} else if (method.isAnnotationPresent(Update.class)) {
				update = method.getAnnotation(Update.class);
				generateMethodSqlMapping(statementSb, "update", method, update
						.id(), null, update.parameterClass(), null, Null.class,
						update.sql(), null, null);
			} else if (method.isAnnotationPresent(Insert.class)) {
				insert = method.getAnnotation(Insert.class);
				generateMethodSqlMapping(statementSb, "insert", method, insert
						.id(), null, insert.parameterClass(), null, Null.class,
						insert.sql(), null, null);
			} else if (method.isAnnotationPresent(Delete.class)) {
				delete = method.getAnnotation(Delete.class);
				generateMethodSqlMapping(statementSb, "delete", method, delete
						.id(), null, delete.parameterClass(), null, Null.class,
						delete.sql(), null, null);
			}
		}
	}

	private static void processResultMap(Class<?> clazz,
			StringBuffer resultMapSb) {
		if (clazz.isAnnotationPresent(ResultMap.class)) {
			ResultMap annotation = clazz.getAnnotation(ResultMap.class);
			String id = annotation.id();

			// 检查当前resultMap是否已经在其它dao中声明并解析过了
			if (resultMapIds.contains(id))
				return;
			resultMapIds.add(id);

			Class<?> resultClass = annotation.resultClass();
			Result[] results = annotation.result();

			resultMapSb.append("  <resultMap id=\"" + id + "\" class=\""
					+ resultClass.getName() + "\">");
			for (Result result : results) {
				resultMapSb.append("\n    <result property=\""
						+ result.property() + "\"");
				resultMapSb.append("  column=\"" + result.column() + "\"");
				if (!result.columnIndex().trim().equals(""))
					resultMapSb.append("  columnIndex=\""
							+ result.columnIndex() + "\"");
				if (!result.javaType().trim().equals(""))
					resultMapSb.append("  javaType=\"" + result.javaType()
							+ "\"");
				if (!result.jdbcType().trim().equals(""))
					resultMapSb.append("  jdbcType=\"" + result.jdbcType()
							+ "\"");
				if (!result.nullValue().trim().equals(""))
					resultMapSb.append("  nullValue=\"" + result.nullValue()
							+ "\"");
				if (!result.select().trim().equals(""))
					resultMapSb.append("  select=\"" + result.select() + "\"");
				resultMapSb.append("/>");
			}
			resultMapSb.append("\n  </resultMap>");
		}
	}

	private static void processCacheModel(Class<?> clazz,
			StringBuffer cacheModelSb) {
		if (clazz.isAnnotationPresent(Cachemodel.class)) {
			Cachemodel annotation = clazz.getAnnotation(Cachemodel.class);
			String id = annotation.id();
			// 检查当前parameterMap是否已经在其它dao中声明并解析过了
			if (cacheModelIds.contains(id))
				return;
			cacheModelIds.add(id);

			String[] flushOnExecute = annotation.flushOnExecute();

			cacheModelSb.append("  <cacheModel id=\"" + id
					+ "\" imlementation=\"" + annotation.imlementation()
					+ "\">");

			cacheModelSb.append("\n    <flushInterval hours=\""
					+ annotation.flushInterval() + "\"/>");
			for (String statement : flushOnExecute) {
				cacheModelSb.append("\n    <flushOnExecute statement=\""
						+ statement + "\"/>");
			}
			
			Property[] properties = annotation.property();
			for(Property property :properties){
				cacheModelSb.append("\n    <property"+
						" name=\""+ property.name() + "\""+
						" value=\""+ property.value() + "\""+
						"/>");
			}
			
			cacheModelSb.append("\n  </cacheModel>");
		}
	}

	private static void processPrameterMap(Class<?> clazz,
			StringBuffer parameterMapSb) {
		if (clazz.isAnnotationPresent(ParameterMap.class)) {
			ParameterMap annotation = clazz.getAnnotation(ParameterMap.class);
			String id = annotation.id();
			// 检查当前parameterMap是否已经在其它dao中声明并解析过了
			if (parameterMapIds.contains(id))
				return;
			parameterMapIds.add(id);

			Class<?> parameterClass = annotation.parameterClass();
			String[] parameters = annotation.parameters();

			parameterMapSb.append("  <parameterMap id=\"" + id + "\" class=\""
					+ parameterClass.getName() + "\">");
			for (String parameter : parameters) {
				parameterMapSb.append("\n    <parameter property=\""
						+ parameter + "\"/>");
			}
			parameterMapSb.append("\n  </parameterMap>");
		}
	}

	private static void generateMethodSqlMapping(StringBuffer sb,
			String prefix, Method method, String id, String parameterMap,
			Class<?> parameterClazz, String resultMap, Class<?> resultClazz,
			String sql, String cacheModel, String xmlResultName) {
		sb.append("\n");

		String param = "", result = "";
		Class<?>[] parameterTypes;
		Class<?> returnType;

		if (id.trim().equals("")) {
			id = method.getName();
		}

		if (parameterMap != null && !parameterMap.trim().equals("")) {
			param = " parameterMap=\"" + parameterMap + "\"";
		} else if (parameterClazz == Null.class) {
			parameterTypes = method.getParameterTypes();
			if (parameterTypes != null && parameterTypes.length > 0) {
				parameterClazz = parameterTypes[0];
			}
			if (parameterClazz != Null.class) {
				param = " parameterClass=\"" + parameterClazz.getName() + "\"";
			} else
				param = "";
		}

		if (resultMap != null && !resultMap.trim().equals("")) {
			param = " resultMap=\"" + resultMap + "\"";
		} else if (resultClazz == Null.class) {
			returnType = method.getReturnType();
			if (!returnType.getName().equals("void")) {
				resultClazz = returnType;
				final Type genericType = method.getGenericReturnType();
				if (genericType instanceof ParameterizedTypeImpl) {
					ParameterizedTypeImpl pgType = (ParameterizedTypeImpl) genericType;
					Type[] actualTypeArguments = pgType
							.getActualTypeArguments();
					resultClazz = (Class<?>) actualTypeArguments[0];
				}
			}
			if (resultClazz != Null.class) {
				result = " resultClass=\"" + resultClazz.getName() + "\"";
			} else
				result = "";
		}

		sb.append("  <" + prefix + " id=\"" + id + "\"" + param + result);
		if (cacheModel != null && !cacheModel.trim().equals(""))
			sb.append(" cacheModel=\"" + cacheModel + "\"");
		if (xmlResultName != null && !xmlResultName.trim().equals(""))
			sb.append(" xmlResultName=\"" + xmlResultName + "\"");
		sb.append(">\n");
		sb.append("    " + sql.trim());
		sb.append("\n  </" + prefix + ">");
	}
}
