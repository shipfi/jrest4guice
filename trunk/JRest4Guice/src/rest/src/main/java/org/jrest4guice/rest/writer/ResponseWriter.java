package org.jrest4guice.rest.writer;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.Map;

import org.jrest4guice.rest.exception.Need2RedirectException;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public interface ResponseWriter {

	/**
	 * 向客户端写回服务端的输出结果
	 * @param out TODO
	 * @param result
	 * @param response
	 */
	public void writeResult(Method method,ByteArrayOutputStream out, Object result, String charset ,Map options) throws Need2RedirectException;
	
	/**
	 * 返回当前Writer所对应的Mime类型
	 * @return
	 */
	public String getMimeType();
}