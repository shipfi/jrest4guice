package org.jrest4guice.rest.render.ctl;

import java.util.List;

import org.commontemplate.core.Context;
import org.commontemplate.standard.directive.BlockDirectiveHandlerSupport;
import org.commontemplate.standard.directive.DirectiveUtils;
import org.jrest4guice.security.Role;
import org.jrest4guice.security.SecurityContext;
import org.jrest4guice.security.UserRole;

@SuppressWarnings("unchecked")
public class RoleDirectiveHandler extends BlockDirectiveHandlerSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7051271266227970377L;
	
	@Override
	public void doRender(Context context, String directiveName, Object param,
			List innerElements) throws Exception {
		if (param != null){
			
			Object useRoleObj = context.getVariable(SecurityContext.CURRENT_USER_ROlE);
			if(useRoleObj != null){
				UserRole ur = (UserRole)useRoleObj;
				String roles[] = {param.toString()};
				if(param instanceof List){
					roles = (String[])((List)param).toArray(new String[]{});
				}
				
				boolean hasRole = false;
				List<Role> uRoles = ur.getRoles();
				top: for (String role : roles) {
					for (Role r : uRoles) {
						if (role.equalsIgnoreCase(r.getName())) {
							hasRole = true;
							break top;
						}
					}
				}
				
				if(hasRole)
					DirectiveUtils.renderAll(innerElements, context);
			}
		}
	}
}
