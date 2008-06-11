package org.jrest4guice.rest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.jndi.JndiGuiceModuleProvider;
import org.jrest4guice.rest.converter.DateConverter;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
public class JRest4GuiceHelper {
	/**
	 * 打开JRest的支持
	 * 
	 * @param scanPaths
	 *            需要动态扫瞄的资源路径
	 * @return
	 */
	public static GuiceContext useJRest(String... scanPaths) {
		new DateConverter().addDefaultDateConverter();
		
		return GuiceContext.getInstance().addModuleProvider(
				new JRestGuiceModuleProvider(scanPaths)).addModuleProvider(
				new JndiGuiceModuleProvider(scanPaths));
	}

	/**
	 * 注册BeanUtils的对应的转换器
	 * 
	 * @param converter
	 * @param clazz
	 */
	public static void addBeanConvert(Converter converter, Class<?> clazz) {
		BeanUtilsBean.getInstance().getConvertUtils()
				.register(converter, clazz);
	}
}