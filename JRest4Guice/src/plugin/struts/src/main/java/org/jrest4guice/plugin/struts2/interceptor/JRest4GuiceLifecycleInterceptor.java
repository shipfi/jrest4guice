package org.jrest4guice.plugin.struts2.interceptor;

import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Transaction;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.persistence.hibernate.SessionFactoryHolder;
import org.jrest4guice.persistence.hibernate.SessionInfo;
import org.jrest4guice.persistence.jpa.EntityManagerFactoryHolder;
import org.jrest4guice.persistence.jpa.EntityManagerInfo;
import org.jrest4guice.rest.context.RestContextManager;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class JRest4GuiceLifecycleInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8495328369653597266L;


	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		ActionContext ctx = ActionContext.getContext();
		if(ctx != null){
			//注入http的上下文
			HttpServletRequest request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			
			HttpServletResponse response = (HttpServletResponse) ctx
					.get(ServletActionContext.HTTP_RESPONSE);  		
			RestContextManager.setContext(request, response, new ModelMap<String, String>());
		}
		
		boolean need2ProcessTransaction = false;
		
		String result = null;
		Transaction hbTS = null;
		EntityTransaction jpaTS = null;
		
		try {
			result = invocation.invoke();

			if(GuiceContext.getInstance().isUseJPA()){
				EntityManagerFactoryHolder emfH = GuiceContext.getInstance().getBean(EntityManagerFactoryHolder.class);
				EntityManagerInfo entityManager = emfH.getEntityManagerInfo();
				jpaTS = entityManager.getEntityManager().getTransaction();
				need2ProcessTransaction = entityManager.isNeed2ProcessTransaction();
			}else if(GuiceContext.getInstance().isUseHibernate()){
				SessionFactoryHolder sessionFH = GuiceContext.getInstance().getBean(SessionFactoryHolder.class);
				SessionInfo session = sessionFH.getSessionInfo();
				hbTS = session.getSession().getTransaction();
				need2ProcessTransaction = session.isNeed2ProcessTransaction() && !hbTS.wasCommitted();
			}

			if(need2ProcessTransaction){
				if(hbTS != null)
					hbTS.commit();
				if(jpaTS != null)
					jpaTS.commit();
			}
		} catch (Exception e) {
			if(hbTS != null)
				hbTS.rollback();
			if(jpaTS != null)
				jpaTS.rollback();
			throw e;
		} finally {
			//清除上下文对象
			RestContextManager.clearContext();
		}
		return result;
	}
}
