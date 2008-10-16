package org.jrest4guice.rest.writer.renderer.ctl;

import java.util.List;

import org.commontemplate.core.Context;
import org.commontemplate.standard.directive.BlockDirectiveHandlerSupport;
import org.commontemplate.standard.directive.DirectiveUtils;
import org.jrest4guice.security.SecurityContext;

@SuppressWarnings("unchecked")
public class IsLoginDirectiveHandler extends BlockDirectiveHandlerSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7051271266227970377L;
	
	@Override
	public void doRender(Context context, String directiveName, Object param,
			List innerElements) throws Exception {
		boolean expectValue = true;
		boolean isLogin = false;
		if (param != null){
			Object isLoginObj = context.getVariable(SecurityContext.USER_IS_LOGIN);
			if(isLoginObj != null){
				isLogin = Boolean.parseBoolean(isLoginObj.toString());
			}
			expectValue = Boolean.parseBoolean(param.toString());
		}
		
		if(expectValue==isLogin)
			DirectiveUtils.renderAll(innerElements, context);
	}
}
