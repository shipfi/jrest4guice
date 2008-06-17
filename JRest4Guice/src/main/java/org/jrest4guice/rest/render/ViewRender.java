package org.jrest4guice.rest.render;

import java.io.PrintWriter;

import org.jrest4guice.rest.JRestResult;

/**
 * 视图渲染器
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
public interface ViewRender {
	/**
	 * 返回渲染器的类型
	 * @return
	 */
	public String getRenderType();
	
	/**
	 * 渲染数据
	 * @param out	输出流
	 * @param templateUrl	模块的路径
	 * @param result	业务数据
	 * @throws Exception
	 */
	public void render(PrintWriter out,String templateUrl,JRestResult result) throws Exception;
}
