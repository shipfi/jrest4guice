/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cnoss.security.resources;
import org.cnoss.security.entity.UserInfo;
import org.jrest4guice.client.Page;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.ModelBean;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.Post;

/**
 *
 * @author Administrator
 */
public class SecurityResource {
    
	@Post
	public boolean reg(@ModelBean UserInfo user) {
		return true;
	}
           
	@Post
	public boolean portalLogin(String userName, String userPassword) {
		return true;
	} 
        	@Post
	public boolean adminLogin(String userName, String userPassword) {
		return true;
	} 
                
        public boolean logout(String userName){
            return true;
        }       
        
}
