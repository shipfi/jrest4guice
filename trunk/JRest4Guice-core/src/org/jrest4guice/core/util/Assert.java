package org.jrest4guice.core.util;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 断言工具类<br>
 * 用于帮助程序校验参数或变量值。如果断言不通过会抛出<code>IllegalArgumentException</code>的运行期异常。
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
public abstract class Assert {
	
	/**
	 * 断言 布尔值表达式为真
	 * @param expression 表达式
	 * @param message 失败信息
	 * @throws IllegalArgumentException
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression)
			throw new IllegalArgumentException(message);
	}
	
	/**
	 * 断言 布尔值表达式为真
	 * @param expression 表达式
	 * @throws IllegalArgumentException
	 */
	public static void isTrue(boolean expression) {
		isTrue(expression, "[断言失败] - 表达式值必须为真");
	}
	
	/**
	 * 断言 布尔值表达式为假
	 * @param expression 表达式
	 * @param message 失败信息
	 * @throws IllegalArgumentException
	 */
	public static void isFalse(boolean expression, String message) {
		if (expression)
			throw new IllegalArgumentException(message);
	}
	
	/**
	 * 断言 布尔值表达式为假
	 * @param expression 表达式
	 * @throws IllegalArgumentException
	 */
	public static void isFalse(boolean expression) {
		isTrue(expression, "[断言失败] - 表达式值必须为假");
	}
	
	/**
	 * 断言 对象为空
	 * @param object 被检查对象
	 * @param message 失败信息
	 * @throws IllegalArgumentException
	 */
	public static void isNull(Object object, String message) {
		if (object != null)
			throw new IllegalArgumentException(message);
	}
	
	/**
	 * 断言 对象为空
	 * @param object 被检查对象
	 * @throws IllegalArgumentException
	 */
	public static void isNull(Object object) {
		isNull(object, "[断言失败] - 被检查对象必须为空");
	}
	
	/**
	 * 断言 对象非空
	 * @param object 被检查对象
	 * @param message 失败信息
	 * @throws IllegalArgumentException
	 */
	public static void notNull(Object object, String message) {
		if (object == null)
			throw new IllegalArgumentException(message);
	}
	
	/**
	 * 断言 对象非空
	 * @param object 被检查对象
	 * @throws IllegalArgumentException
	 */
	public static void notNull(Object object) {
		notNull(object, "[断言失败] - 被检查对象必须非空");
	}
	
	/**
	 * 断言 字符串非空
	 * @param text 被检查的字符串
	 * @param message 失败信息
	 * @throws IllegalArgumentException
	 * @see {@link StringUtils#isNotBlank(String)}
	 */
	public static void isNotBlank(String text, String message) {
		if (!StringUtils.isNotBlank(text))
			throw new IllegalArgumentException(message);
	}
	
	/**
	 * 断言 字符串非空
	 * @param text 被检查的字符串
	 * @throws IllegalArgumentException
	 * @see {@link StringUtils#isNotBlank(String)}
	 */
	public static void isNotBlank(String text) {
		isNotBlank(text, "[断言失败] - 字符串必须非空，并不能只有空格");
	}
	
	/**
	 * 断言 数组对象非空
	 * @param array 被检查的数据对象
	 * @param message 失败信息
	 * @throws IllegalArgumentException
	 * @see {@link ArrayUtils#isEmpty(Object[])}
	 */
	public static void notEmpty(Object[] array, String message) {
		if (ArrayUtils.isEmpty(array))
			throw new IllegalArgumentException(message);
	}
	
	/**
	 * 断言 数组对象非空
	 * @param array 被检查的数据对象
	 * @throws IllegalArgumentException
	 * @see {@link ArrayUtils#isEmpty(Object[])}
	 */
	public static void notEmpty(Object[] array) {
		notEmpty(array, "[断言失败] - 数组对象不能为空,至少需要包含一个元素");
	}
	
	/**
	 * 断言 数组及其元素非空
	 * @param array 被检查的数据对象
	 * @param message 失败信息
	 * @throws IllegalArgumentException
	 * @see {@link ArrayUtils#isEmpty(Object[])}
	 */
	public static void noNullElements(Object[] array, String message) {
		if (!ArrayUtils.isEmpty(array)) {
			for (Object element : array) {
				if (element == null)
					throw new IllegalArgumentException(message);
			}
		}
	}
	
	/**
	 * 断言 数组及其元素非空
	 * @param array 被检查的数据对象
	 * @throws IllegalArgumentException
	 * @see {@link ArrayUtils#isEmpty(Object[])}
	 */
	public static void noNullElements(Object[] array) {
		noNullElements(array, "[断言失败] - 数组及其元素不能为空");
	}
	
	/**
	 * 断言 集合非空
	 * @param collection 被检查的集合对象
	 * @param message 失败信息
	 * @throws IllegalArgumentException
	 * @see {@link CollectionUtils#isEmpty(Collection)}
	 */
	@SuppressWarnings("unchecked")
	public static void notEmpty(Collection collection, String message) {
		if (CollectionUtils.isEmpty(collection))
			throw new IllegalArgumentException(message);
	}
	
	/**
	 * 断言 集合非空
	 * @param collection 被检查的集合对象
	 * @throws IllegalArgumentException
	 * @see {@link CollectionUtils#isEmpty(Collection)}
	 */
	@SuppressWarnings("unchecked")
	public static void notEmpty(Collection collection) {
		notEmpty(collection, "[断言失败] - 集合不能为空，并至少要包含一项元素");
	}
	
	/**
	 * 断言 映射非空
	 * @param map 被检查的映射对象
	 * @param message 失败信息
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static void notEmpty(Map map, String message) {
		if (map == null || map.isEmpty())
			throw new IllegalArgumentException(message);
	}
	
	/**
	 * 断言 映射非空
	 * @param map 被检查的映射对象
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static void notEmpty(Map map) {
		notEmpty(map, "[断言失败] - 映射不能为空，并至少要包含一项映射关系");
	}
	
	/**
	 * 断言 指定对象为特定类型的实例
	 * @param type 被检查类型
	 * @param obj 被检查对象
	 * @throws IllegalArgumentException
	 * @see {@link Class#isInstance(Object)}
	 */
	@SuppressWarnings("unchecked")
	public static void isInstanceOf(Class clazz, Object obj) {
		isInstanceOf(clazz, obj, "");
	}
	
	/**
	 * 断言 指定对象为特定类型的实例
	 * @param type 被检查类型
	 * @param obj 被检查对象
	 * @param message 失败信息
	 * @throws IllegalArgumentException
	 * @see {@link Class#isInstance(Object)}
	 */
	@SuppressWarnings("unchecked")
	public static void isInstanceOf(Class type, Object obj, String message) {
		notNull(type, "被检查类型不能为空");
		if (!type.isInstance(obj))
			throw new IllegalArgumentException(message + " Object:" + (obj != null ? obj.getClass().getName() : "null")
			        + " Type:" + type.getName());
	}
	
	/**
	 * 断言 superType类型是否为subType的超类或超接口
	 * @param superType 父类
	 * @param subType 子类
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static void isAssignable(Class superType, Class subType) {
		isAssignable(superType, subType, "");
	}
	
	/**
	 * 断言 superType类型是否为subType的超类或超接口
	 * @param superType 父类
	 * @param subType 子类
	 * @param message 失败信息
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static void isAssignable(Class superType, Class subType, String message) {
		notNull(superType, "父类不能为空");
		if (subType == null || !superType.isAssignableFrom(subType))
			throw new IllegalArgumentException(message);
	}
}