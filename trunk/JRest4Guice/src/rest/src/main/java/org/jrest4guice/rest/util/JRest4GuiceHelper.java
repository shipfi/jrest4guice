package org.jrest4guice.rest.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.jndi.JndiGuiceModuleProvider;
import org.jrest4guice.rest.JRest4GuiceModuleProvider;
import org.jrest4guice.rest.converter.DatePatternConverter;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class JRest4GuiceHelper {
	private static long maxBodyPayloadSize = 1024*1024;
	/**
	 * 打开JRest的支持
	 * 
	 * @param scanPaths
	 *            需要动态扫瞄的资源路径
	 * @return
	 */
	public static GuiceContext useJRest(String... scanPaths) {
		//注册缺省的日期转换器（用于BeanUtilsBean）
		new DatePatternConverter().addDefaultDateConverter();
		//打开JRest的支持
		return GuiceContext.getInstance().addModuleProvider(
				new JRest4GuiceModuleProvider(scanPaths)).addModuleProvider(
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
	
	/**
	 * 设置客户端向服务端发送的http body的最大有效负载字节数
	 * @param size
	 */
	public static void setMaxBodyPayloadSize(long size){
		maxBodyPayloadSize = size;
	}
	
	public static long getMaxBodyPayloadSize(){
		return maxBodyPayloadSize;
	}
}