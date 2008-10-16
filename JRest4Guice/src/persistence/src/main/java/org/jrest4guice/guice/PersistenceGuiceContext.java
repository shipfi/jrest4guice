package org.jrest4guice.guice;

import org.jrest4guice.commons.lang.Assert;
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

import com.google.inject.Module;

/**
 * 全局上下文对象实体
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
public class PersistenceGuiceContext implements ContextCleaner{

	private static volatile PersistenceGuiceContext me;

	private boolean useJPA;
	private boolean useIbatis;
	private boolean useHibernate;
	
	private GuiceContext guiceContext;

	private PersistenceGuiceContext() {
		this.guiceContext = GuiceContext.getInstance();
		WebContextManager.addContextCleaner(this);
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






	/**
	 * 从当前上下文中获取对象
	 * @param <T> 对象类型
	 * @param clazz 要获取对象的 class
	 * @return 对象实例
	 */
	public <T> T getBean(Class<T> clazz) {
		return this.guiceContext.getBean(clazz);
	}

	/**
	 * 使用当前上下文为对象注入依赖的成员对象
	 * @param o 要注入成员的对象
	 */
	public void injectorMembers(Object o) {
		this.guiceContext.injectorMembers(o);
	}

	/**
	 * 添加 模块提供者
	 * @param providers 模块提供者实例
	 * @return 全局上下文对象自身
	 */
	public GuiceContext addModuleProvider(ModuleProvider... providers) {
		return this.guiceContext.addModuleProvider(providers);
	}

	public GuiceContext addUserModule(Module... modules) {
		return this.guiceContext.addUserModule(modules);
	}
	
	public boolean isInitialized() {
		return this.guiceContext.isInitialized();
	}


	/**
	 * 打开自定义的拦截器支持，允许通过@Interceptors来支持自定义的拦截器
	 * @param packages
	 * @return
	 */
	public GuiceContext enableCustomInterceptor(String... packages){
		return this.guiceContext.enableCustomInterceptor(packages);
	}

	/**
	 * 打开JAAS支持
	 * @return
	 */
	public GuiceContext useSecurity(){
		return this.guiceContext.useSecurity();
	}
	
	/**
	 * 打开SNA支持
	 * @param packages cache提供者的扫描路径
	 * @return
	 */
	public GuiceContext useCache(String... packages){
		return this.guiceContext.useCache(packages);
	}

	/**
	 * 初始化方法,该对象只会被初始化一次
	 * @return 全局上下文对象自身
	 */
	public GuiceContext init() {
		return this.guiceContext.init();
	}

	public boolean isUseSecurity() {
		return this.guiceContext.isUseSecurity();
	}

	@Override
	public void clearContext() {
		this.closePersistenceContext();
	}
}
