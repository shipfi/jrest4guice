package org.jrest4guice.persistence;

import org.jrest4guice.commons.lang.Assert;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.persistence.hibernate.HibernateGuiceModuleProvider;
import org.jrest4guice.persistence.hibernate.SessionFactoryHolder;
import org.jrest4guice.persistence.ibatis.IbatisGuiceModuleProvider;
import org.jrest4guice.persistence.ibatis.SqlMapClientHolder;
import org.jrest4guice.persistence.jpa.EntityManagerFactoryHolder;
import org.jrest4guice.persistence.jpa.JpaGuiceModuleProvider;
import org.jrest4guice.search.hs.HibernateSearchGuiceModuleProvider;
import org.jrest4guice.transaction.HibernateLocalTransactionInterceptor;
import org.jrest4guice.transaction.IbatisLocalTransactionInterceptor;
import org.jrest4guice.transaction.JpaLocalTransactionInterceptor;
import org.jrest4guice.transaction.TransactionGuiceModuleProvider;

/**
 * 全局上下文对象实体
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
public class PersistenceGuiceContext extends GuiceContext {

	private static volatile PersistenceGuiceContext me;

	private boolean useJPA;
	private boolean useIbatis;
	private boolean useHibernate;

	protected PersistenceGuiceContext() {
		super();
	}

	/**
	 * 获取对象实例
	 * @return
	 */
	public static PersistenceGuiceContext getInstance() {
		if (me == null)
			synchronized (PersistenceGuiceContext.class) {
				if (me == null)
					me = new PersistenceGuiceContext();
			}
		return me;
	}

	/**
	 * 打开JPA支持
	 * @return
	 */
	public PersistenceGuiceContext useJPA(String... packages){
		Assert.isFalse(this.useJPA, "已经打开了JPA支持");
		Assert.isFalse(this.useHibernate, "已经打开了Hibernate支持，不能再使用JPA");
		this.useJPA = true;
		
		JpaGuiceModuleProvider jpaGuiceModuleProvider = new JpaGuiceModuleProvider();
		if(packages != null)
			jpaGuiceModuleProvider.addScanPackages(packages);
		
		this.addModuleProvider(jpaGuiceModuleProvider,new TransactionGuiceModuleProvider(new JpaLocalTransactionInterceptor()));
		return this;
	}

	/**
	 * 打开Hibernate支持
	 * @return
	 */
	public PersistenceGuiceContext useHibernate(String... packages){
		Assert.isFalse(this.useHibernate, "已经打开了Hibernate支持");
		Assert.isFalse(this.useJPA, "已经打开了JPA支持，不能再使用Hibernate");
		this.useHibernate = true;
		
		HibernateGuiceModuleProvider hibernateGuiceModuleProvider = new HibernateGuiceModuleProvider();
		if(packages != null)
			hibernateGuiceModuleProvider.addScanPackages(packages);
		
		this.addModuleProvider(hibernateGuiceModuleProvider,new TransactionGuiceModuleProvider(new HibernateLocalTransactionInterceptor()));
		return this;
	}

	/**
	 * 打开IBatis支持
	 * @return
	 */
	public PersistenceGuiceContext useIbatis(String... packages){
		this.useIbatis = true;
		this.addModuleProvider(new IbatisGuiceModuleProvider(packages),new TransactionGuiceModuleProvider(new IbatisLocalTransactionInterceptor()));
		return this;
	}
	
	/**
	 * 打开Hibernate search功能
	 * @return
	 */
	public PersistenceGuiceContext useHibernateSearch(){
		Assert.isTrue(this.useHibernate || this.useJPA, "Hibernate search 需要hibernate 或者 jpa的支持");
		this.addModuleProvider(new HibernateSearchGuiceModuleProvider());
		return this;
	}

	/**
	 * 关闭持久化上下文环境
	 */
	public void closePersistenceContext(){
		if(this.isUseJPA()){
			this.getBean(EntityManagerFactoryHolder.class).closeEntityManager();
		}else if(this.isUseIbatis()){
			this.getBean(SqlMapClientHolder.class).closeSqlMapClient();
		}else if(this.isUseHibernate()){
			this.getBean(SessionFactoryHolder.class).closeSession();
		}
	}

	public boolean isUseIbatis() {
		return useIbatis;
	}

	public boolean isUseJPA() {
		return useJPA;
	}

	public boolean isUseHibernate() {
		return useHibernate;
	}
}
