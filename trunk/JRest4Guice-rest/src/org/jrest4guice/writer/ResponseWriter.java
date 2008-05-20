package org.jrest4guice.writer;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public interface ResponseWriter {

	/**
	 * 向客户端写回服务端的输出结果
	 * @param response
	 * @param result
	 */
	public abstract void writeResult(Object result, String charset);
	
	/**
	 * 返回当前Writer所对应的Mime类型
	 * @return
	 */
	public String getMimeType();
}