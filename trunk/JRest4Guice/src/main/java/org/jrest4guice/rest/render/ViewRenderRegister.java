package org.jrest4guice.rest.render;

import java.util.HashMap;
import java.util.Map;

import org.jrest4guice.guice.GuiceContext;

import com.google.inject.Singleton;

/**
 * 视力渲染器注册
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
@Singleton
public class ViewRenderRegister {

	private static Map<String, Class<ViewRender>> renders = new HashMap<String, Class<ViewRender>>(0);

	public ViewRenderRegister registViewRender(String renderType,
			Class<ViewRender> viewRender) {
		renders.put(renderType, viewRender);
		return this;
	}

	public ViewRender getViewRender(String renderType) {
		Class<ViewRender> clazz = renders.get(renderType);
		if(clazz != null)
			return  GuiceContext.getInstance().getBean(clazz);
		else
			return null;
	}
	
	public static ViewRenderRegister getInstance(){
		return GuiceContext.getInstance().getBean(ViewRenderRegister.class);
	}

}
