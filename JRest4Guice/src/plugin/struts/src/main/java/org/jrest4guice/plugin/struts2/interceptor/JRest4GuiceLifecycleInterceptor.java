package org.jrest4guice.plugin.struts2.interceptor;

import javax.persistence.EntityTransaction;

import org.hibernate.Transaction;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.guice.PersistenceGuiceContext;
import org.jrest4guice.persistence.hibernate.SessionFactoryHolder;
import org.jrest4guice.persistence.hibernate.SessionInfo;
import org.jrest4guice.persistence.jpa.EntityManagerFactoryHolder;
import org.jrest4guice.persistence.jpa.EntityManagerInfo;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class JRest4GuiceLifecycleInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8495328369653597266L;


	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		boolean need2ProcessTransaction = false;
		
		String result = null;
		Transaction hbTS = null;
		EntityTransaction jpaTS = null;
		
		try {
			result = invocation.invoke();

			if(PersistenceGuiceContext.getInstance().isUseJPA()){
				EntityManagerFactoryHolder emfH = GuiceContext.getInstance().getBean(EntityManagerFactoryHolder.class);
				EntityManagerInfo entityManager = emfH.getEntityManagerInfo();
				jpaTS = entityManager.getEntityManager().getTransaction();
				need2ProcessTransaction = entityManager.isNeed2ProcessTransaction();
			}else if(PersistenceGuiceContext.getInstance().isUseHibernate()){
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
		}
		return result;
	}
}
