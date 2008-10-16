package org.jrest4guice.rest.writer.renderer;


import java.io.OutputStream;

import org.jrest4guice.rest.ServiceResult;
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
	 * 返回渲染器的类型的缩略名
	 * @return
	 */
	public String getRenderTypeShortName();

	/**
	 * 渲染数据
	 * @param out	输出流
	 * @param annotation	页面流程定义的注解
	 * @param result	业务数据
	 * @throws Exception
	 */
	public void render(OutputStream out,PageFlow annotation,ServiceResult result) throws Exception;
}
