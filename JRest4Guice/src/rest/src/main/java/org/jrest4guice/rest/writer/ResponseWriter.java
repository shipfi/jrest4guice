package org.jrest4guice.rest.writer;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public interface ResponseWriter {

	/**
	 * 向客户端写回服务端的输出结果
	 * @param response
	 * @param result
	 */
	public abstract void writeResult(Method method,Object result, Map options, String charset);
	
	/**
	 * 返回当前Writer所对应的Mime类型
	 * @return
	 */
	public String getMimeType();
}