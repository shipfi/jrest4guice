package org.jrest4guice.rest.render;

import java.io.PrintWriter;

import org.jrest4guice.rest.JRestResult;
import org.jrest4guice.rest.annotations.PageFlow;

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
	 * @param annotation	页面流程定义的注解
	 * @param result	业务数据
	 * @param cache	是否要缓存
	 * @throws Exception
	 */
	public void render(PrintWriter out,PageFlow annotation,JRestResult result,boolean cache) throws Exception;
}
