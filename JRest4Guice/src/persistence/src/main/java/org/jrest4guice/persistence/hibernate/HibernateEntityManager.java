package org.jrest4guice.persistence.hibernate;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jrest4guice.client.Page;
import org.jrest4guice.client.Pagination;
import org.jrest4guice.persistence.BaseEntityManager;
import org.jrest4guice.persistence.DeletedFlag;
import org.jrest4guice.persistence.EntityAble;
import org.jrest4guice.persistence.ParameterObject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings("unchecked")
public class HibernateEntityManager<PK extends Serializable, E extends EntityAble<PK>>
		implements BaseEntityManager<PK, E> {

	/** 实体类型 */
	private final Class<E> type;
	/** 实体会话 */
	private final Session session;

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            实体类的 class
	 * @param session
	 *            当前应用的实体管理器
	 */
	public HibernateEntityManager(final Class<E> type, final Session session) {
		this.type = type;
		this.session = session;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#countByNamedQuery(java
	 * .lang.String, java.util.Map)
	 */
	public long count(final String qname,
			final Map<String, Object> parameters) {
		final Query query = this.createQuery(this.getFullQueryName(qname,
				BaseEntityManager.COUNT_SUFFIX));
		this.fittingQuery(query, parameters);
		final Object result = query.uniqueResult();
		if (result instanceof BigInteger) {
			return ((BigInteger) result).longValue();
		} else {
			return ((Long) result).longValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#countByNamedQuery(java
	 * .lang.String, java.lang.Object)
	 */
	public long count(final String qname,
			final Object... parameters) {
		final Query query = this.createQuery(this.getFullQueryName(qname,
				BaseEntityManager.COUNT_SUFFIX));
		this.fittingQuery(query, parameters);
		final Object result = query.uniqueResult();
		if (result instanceof BigInteger) {
			return ((BigInteger) result).longValue();
		} else {
			return ((Long) result).longValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jrest4guice.persistence.jpa.BaseEntityManager#create(E)
	 */
	public PK create(E newInstance) {
		E orginalObj = this.checkEnhanced(newInstance);
		boolean equals = orginalObj.equals(newInstance);

		this.session.persist(orginalObj);

		if (!equals) {
			try {
				BeanUtils.copyProperties(newInstance, orginalObj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return newInstance.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jrest4guice.persistence.jpa.BaseEntityManager#delete(E)
	 */
	public boolean delete(E entity) {
		if (entity != null) {
			entity = this.checkEnhanced(entity);
			this.session.delete(entity);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jrest4guice.persistence.jpa.BaseEntityManager#deleteById(PK)
	 */
	public boolean deleteById(final PK pk) {
		final E entity = this.load(pk);
		if (entity != null) {
			this.session.delete(entity);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jrest4guice.persistence.BaseEntityManager#dynamicLoad(java.util.Map)
	 */
	public E dynamicLoad(final Map<String, Object> parameters){
		List result = this.doDynamicQuery(null, parameters, EntityAble.SCOPE_VALID);
		if(result.size()>0)
			return (E)result.get(0);
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#dynamicList(java.util
	 * .Map)
	 */
	public List<E> dynamicList(final Map<String, Object> parameters) {
		return this.doDynamicQuery(null, parameters, EntityAble.SCOPE_VALID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#dynamicPage(org.jrest4guice
	 * .client.Pagination, java.util.Map)
	 */
	public Page<E> dynamicPage(final Pagination pagination, final Map<String, Object> parameters) {
		return (Page<E>) this.doDynamicQuery(pagination, parameters,
				EntityAble.SCOPE_VALID).get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jrest4guice.persistence.jpa.BaseEntityManager#listAll()
	 */
	public List<E> listAll() {
		return this.listByEntityState(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#listByNamedQuery(java
	 * .lang.String, java.util.Map)
	 */
	public List<E> list(final String qname,
			final Map<String, Object> parameters) {
		final Query query = this.createQuery(this.getFullQueryName(qname,
				BaseEntityManager.FIND_SUFFIX));
		this.fittingQuery(query, parameters);
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#listByNamedQuery(java
	 * .lang.String, java.lang.Object)
	 */
	public List<E> list(final String qname,
			final Object... parameters) {
		final Query query = this.createQuery(this.getFullQueryName(qname,
				BaseEntityManager.FIND_SUFFIX));
		this.fittingQuery(query, parameters);
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#listByNamedQuery(java
	 * .lang.String, org.jrest4guice.client.Pagination, java.util.Map)
	 */
	public List<E> list(final String qname,
			final Pagination pagination,
			final Map<String, Object> parameters) {
		final Query query = this.createQuery(this.getFullQueryName(qname,
				BaseEntityManager.FIND_SUFFIX));
		this.fittingQuery(query, parameters);
		this.pagingQuery(query, pagination);
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#listByNamedQuery(java
	 * .lang.String, org.jrest4guice.client.Pagination, java.lang.Object)
	 */
	public List<E> list(final String qname,
			final Pagination pagination, final Object... parameters) {
		final Query query = this.createQuery(this.getFullQueryName(qname,
				BaseEntityManager.FIND_SUFFIX));
		this.fittingQuery(query, parameters);
		this.pagingQuery(query, pagination);
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jrest4guice.persistence.jpa.BaseEntityManager#listInvalidAll()
	 */
	public List<E> listInvalidAll() {
		return this.listByEntityState(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jrest4guice.persistence.jpa.BaseEntityManager#load(PK)
	 */
	public E load(final PK pk) {
		return (E)this.session.get(this.type,pk);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#loadByNamedQuery(java
	 * .lang.String, java.util.Map)
	 */
	public E load(final String qname,
			final Map<String, Object> parameters) {
		final Query query = this.createQuery(this.getFullQueryName(qname,
				BaseEntityManager.LOAD_SUFFIX));
		this.fittingQuery(query, parameters);
		try {
			return (E) query.uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#loadByNamedQuery(java
	 * .lang.String, java.lang.Object)
	 */
	public E load(final String qname, final Object... parameters) {
		final Query query = this.createQuery(this.getFullQueryName(qname,
				BaseEntityManager.LOAD_SUFFIX));
		this.fittingQuery(query, parameters);
		try {
			return (E) query.uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#pageAll(org.jrest4guice
	 * .client.Pagination)
	 */
	public Page<E> pageAll(final Pagination pagination) {
		return (Page<E>) this.doDynamicQuery(pagination, null,
				EntityAble.SCOPE_VALID).get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#pageByNamedQuery(java
	 * .lang.String, org.jrest4guice.client.Pagination, java.util.Map)
	 */
	public Page<E> page(final String qname,
			final Pagination pagination,
			final Map<String, Object> parameters) {
		if (pagination == null) {
			throw new IllegalArgumentException("缺少分页参数设置");
		}
		final long count = this.count(qname, parameters);
		if (count == 0) {
			return new Page<E>(pagination.getFirstResult(), count, pagination
					.getPageSize(), new ArrayList<E>());
		}
		final List<E> result = this.list(qname, pagination,
				parameters);
		if(result.size()==0 && pagination.getPageIndex()>1){
			pagination.setPageIndex(pagination.getPageIndex()-1);
			return this.page(qname, pagination, parameters);
		}
		return new Page<E>(Page.getStartOfPage(pagination.getPageIndex(),
				pagination.getPageSize()), count, pagination.getPageSize(),
				result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#pageByNamedQuery(java
	 * .lang.String, org.jrest4guice.client.Pagination, java.lang.Object)
	 */
	public Page<E> page(final String qname,
			final Pagination pagination, final Object... parameters) {
		if (pagination == null) {
			throw new IllegalArgumentException("缺少分页参数设置");
		}
		final long count = this.count(qname, parameters);
		if (count == 0) {
			return new Page<E>(pagination.getFirstResult(), count, pagination
					.getPageSize(), new ArrayList<E>());
		}
		final List<E> result = this.list(qname, pagination,
				parameters);
		if(result.size()==0 && pagination.getPageIndex()>1){
			pagination.setPageIndex(pagination.getPageIndex()-1);
			return this.page(qname, pagination, parameters);
		}
		return new Page<E>(Page.getStartOfPage(pagination.getPageIndex(),
				pagination.getPageSize()), count, pagination.getPageSize(),
				result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#pageInvalidAll(org.
	 * jrest4guice.client.Pagination)
	 */
	public Page<E> pageInvalidAll(final Pagination pagination) {
		return (Page<E>) this.doDynamicQuery(pagination, null,
				EntityAble.SCOPE_INVALID).get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#recovery(org.jrest4guice
	 * .persistence.DeletedFlag)
	 */
	public boolean recovery(final DeletedFlag entity) {
		if (entity != null && entity.isDeleted()) {
			entity.setDeleted(false);
			this.session.merge(entity);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jrest4guice.persistence.jpa.BaseEntityManager#recoveryById(PK)
	 */
	public boolean recoveryById(final PK pk) {
		if (DeletedFlag.class.isAssignableFrom(this.type)) {
			return this.recovery((DeletedFlag) this.load(pk));
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jrest4guice.persistence.jpa.BaseEntityManager#refresh(E)
	 */
	public E refresh(final E entity) {
		this.session.refresh(entity);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.persistence.jpa.BaseEntityManager#remove(org.jrest4guice
	 * .persistence.DeletedFlag)
	 */
	public boolean remove(final DeletedFlag entity) {
		if (entity != null && !entity.isDeleted()) {

			E orginalObj = this.checkEnhanced((E) entity);
			boolean equals = orginalObj.equals((E)entity);

			((DeletedFlag) orginalObj).setDeleted(true);
			this.session.merge(orginalObj);

			if (!equals) {
				try {
					BeanUtils.copyProperties(entity, orginalObj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jrest4guice.persistence.jpa.BaseEntityManager#removeById(PK)
	 */
	public boolean removeById(final PK pk) {
		final E entity = this.load(pk);
		if (entity != null && entity instanceof DeletedFlag) {
			return this.remove((DeletedFlag) entity);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jrest4guice.persistence.jpa.BaseEntityManager#update(E)
	 */
	public E update(E entity) {
		if (entity != null) {
			E orginalObj = this.checkEnhanced(entity);
			boolean equals = orginalObj.equals(entity);

			this.session.merge(orginalObj);

			if (!equals) {
				try {
					BeanUtils.copyProperties(entity, orginalObj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return entity;
		} else {
			return null;
		}
	}

	/**
	 * 创建动态查询语句
	 * 
	 * @param type
	 * @param parameters
	 * @param isCount
	 * @return
	 */
	private String buildDynamicSQL(final Map parameters,
			final boolean isCount, final int scope) {
		final StringBuffer sqls = new StringBuffer();
		if (isCount) {
			sqls.append("select count(e) ");
		}
		sqls.append("from " + this.type.getName() + " e");

		// 范围是所有，包括已经做上删除标记的记录
		boolean hasWhere = false;
		if (DeletedFlag.class.isAssignableFrom(this.type)) {
			if (scope == EntityAble.SCOPE_ALL) {
				sqls.append(" where e.deleted is not null");
			} else {
				sqls.append(" where e.deleted="
						+ (EntityAble.SCOPE_ALL == 1 ? true : false));
			}
			hasWhere = true;
		}

		if (parameters != null) {
			final Collection keys = parameters.keySet();
			// 组装查询条件
			int index = 0;
			Object value;
			String logic,relation;
			for (final Object key : keys) {
				value = parameters.get(key);
				logic = "and";
				relation = "=";
				if(value instanceof ParameterObject){
					logic = ((ParameterObject)value).getLogicSymbol();
					logic = ((ParameterObject)value).getRelationSymbol();
				}
				if (index == 0 && !hasWhere) {
					sqls.append(" where e." + key + ""+relation+":" + key.toString().replaceAll("\\.","_"));
				} else {
					sqls.append(" "+logic+" e." + key + ""+relation+":" +  key.toString().replaceAll("\\.","_"));
				}
				index++;
			}
		}

		return sqls.toString();
	}

	/**
	 * 处理动态查询
	 * 
	 * @param type
	 * @param pageIndex
	 * @param pageSize
	 * @param parameters
	 * @return
	 */
	private List doDynamicQuery(final Pagination pagination,
			final Map parameters, final int scope) {
		Query query = null;

		// 是否需要分页
		final boolean needToPage = pagination != null;
		int recordAmount = 0;
		if (needToPage) {// 初始化分页信息
			// 构造动态查询的统计SQL语句
			query = this.session.createQuery(this.buildDynamicSQL(parameters,
					true, scope));
			// 处理参数
			this.fittingQuery(query, parameters);
			// 获取记录总数
			recordAmount = Integer.parseInt(query.uniqueResult().toString());
		}

		// 构造动态查询的统计SQL语句
		query = this.session.createQuery(this.buildDynamicSQL(parameters,
				false, scope));

		// 数据分页处理
		if (needToPage) {
			this.pagingQuery(query, pagination);
		}

		// 处理参数
		this.fittingQuery(query, parameters);

		// 获取查询结果
		final List result = query.list();

		// 返回查询结果
		if (needToPage) {
			Page<E> page = null;
			if (recordAmount == 0) {
				page = new Page<E>(pagination.getFirstResult(), recordAmount,
						pagination.getPageSize(), new ArrayList<E>());
			} else {
				page = new Page<E>(Page.getStartOfPage(pagination
						.getPageIndex(), pagination.getPageSize()),
						recordAmount, pagination.getPageSize(), result);
			}
			return Arrays.asList(new Page[] { page });
		} else {
			return result;
		}
	}

	/**
	 * 填充查询条件
	 * 
	 * @param query
	 *            查询对象
	 * @param parameters
	 *            Map形式的参数
	 */
	private void fittingQuery(final Query query,
			final Map<String, Object> parameters) {
		if (parameters == null) {
			return;
		}
		Object parameter;
		for (final String key : parameters.keySet()) {
			parameter = parameters.get(key);
			if(parameter instanceof ParameterObject)
				query.setParameter(key.replaceAll("\\.","_"), ((ParameterObject)parameter).getValue());
			else
				query.setParameter(key.replaceAll("\\.","_"), parameter);
		}
	}

	/**
	 * 填充查询条件
	 * 
	 * @param query
	 *            查询对象
	 * @param parameters
	 *            数组形式的参数
	 */
	private void fittingQuery(final Query query, final Object[] parameters) {
		if (parameters == null) {
			return;
		}
		int index = 0;
		for (final Object parameter : parameters) {
			query.setParameter(index++, parameter);
		}
	}

	/**
	 * 拼接命名查询的名称
	 * 
	 * @param qname
	 *            基本的查询名称
	 * @param suffix
	 *            命名查询的后缀
	 * @return
	 */
	private String getFullQueryName(final String sql, final String suffix) {
		if(sql.startsWith(NAMED_PREVIX))
			return NAMED_PREVIX+this.type.getSimpleName() + "." + sql.substring(NAMED_PREVIX.length()) + suffix;
		else
			return sql;
	}

	/**
	 * 根据命名查询的名称生成查询对象的引用
	 * 
	 * @param qname
	 *            命名查询的名称
	 * @return
	 */
	private Query createQuery(final String qname) {
		if(qname.startsWith(NAMED_PREVIX))
			return this.session.getNamedQuery(qname.substring(NAMED_PREVIX.length()));
		else
			return this.session.createQuery(qname);
	}

	/**
	 * 返回指定状态的所有实体
	 * 
	 * @param deleted
	 * @return
	 */
	private List listByEntityState(final boolean deleted) {
		String sql = "select e from " + this.type.getName() + " e";
		if (DeletedFlag.class.isAssignableFrom(this.type)) {
			sql += " where e.deleted=" + deleted;
		}
		return this.session.createQuery(sql).list();
	}

	/**
	 * 设置查询的分布条件
	 * 
	 * @param query
	 *            查询对象
	 * @param pagination
	 *            分页信息
	 */
	private void pagingQuery(final Query query, final Pagination pagination) {
		query.setFirstResult(pagination.getFirstResult());
		query.setMaxResults(pagination.getMaxResults());
	}

	/**
	 * 检查对象是否被动态增强过，如果被增强，则返回增强前的类型，并拷贝属性值
	 * 
	 * @param entity
	 * @return
	 */
	private E checkEnhanced(E entity) {
		if (entity == null)
			return entity;
		String name;
		int index;
		name = entity.getClass().getName();
		index = name.indexOf("$$");
		if (index != -1) {
			name = name.substring(0, index);
			Object origObject;
			try {
				origObject = Class.forName(name).newInstance();
				BeanUtils.copyProperties(origObject, entity);
				return (E) origObject;
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(name);
		}
		return entity;
	}

	/* (non-Javadoc)
	 * @see org.jrest4guice.persistence.BaseEntityManager#executeUpdate(java.lang.String)
	 */
	@Override
	public int executeUpdate(String sql) {
		return this.session.createQuery(sql).executeUpdate();
	}

	public Session getSession() {
		return session;
	}
}
