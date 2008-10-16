package org.jrest4guice.rest.writer.renderer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.rest.annotations.PageInfo;

import com.google.inject.Singleton;

/**
 * 视图渲染器注册
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
@Singleton
public class ViewRenderRegister {

	private static Map<String, Class<ViewRender>> renders = new HashMap<String, Class<ViewRender>>(0);
	private static Map<String, Class<ViewRender>> shortNameRenders;
	private boolean isInit = false;

	public ViewRenderRegister registViewRender(String renderType,
			Class<ViewRender> viewRender) {
		renders.put(renderType, viewRender);
		return this;
	}
	
	private void initShortNameRenders(){
		final Collection<Class<ViewRender>> values = renders.values();
		String shortName;
		shortNameRenders = new HashMap<String, Class<ViewRender>>();
		for(Class<ViewRender> r:values){
			try {
				shortName = ((ViewRender)r.newInstance()).getRenderTypeShortName();
				if(shortName != null){
					shortNameRenders.put(shortName, r);
				}
			} catch (Exception e) {
			}
		}
		isInit = true;
	}

	public ViewRender getViewRender(PageInfo pageInfo) {
		Class<ViewRender> clazz = null;
		
		String renderType = pageInfo.value();
		
		if(!isInit)
			this.initShortNameRenders();
		
		int index = renderType.lastIndexOf(".");
		if(index != -1){
			String shortName = renderType.substring(index);
			clazz = shortNameRenders.get(shortName);
		}

		if(clazz == null){
			renderType = pageInfo.type();
			clazz = renders.get(renderType);
		}
		
		if(clazz != null)
			return  GuiceContext.getInstance().getBean(clazz);
		else
			return null;
	}
	
	public static ViewRenderRegister getInstance(){
		return GuiceContext.getInstance().getBean(ViewRenderRegister.class);
	}

}
