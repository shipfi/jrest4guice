package org.jrest4guice.persistence;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jrest4guice.client.Page;
import org.jrest4guice.client.Pagination;

import com.google.inject.ProvidedBy;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@ProvidedBy(BaseEntityManagerProvider.class)
@SuppressWarnings("unchecked")
public interface BaseEntityManager<PK extends Serializable, E extends EntityAble<PK>> {

	public static final String COUNT_SUFFIX = "[count]";
	public static final String FIND_SUFFIX = "[find]";
	public static final String LOAD_SUFFIX = "[load]";

	public abstract long countByNamedQuery(final String qname,
			final HashMap<String, Object> parameters);

	public abstract long countByNamedQuery(final String qname,
			final Object... parameters);

	/**
	 * 创建新实体
	 * 
	 * @param newInstance
	 *            实体对象
	 * @return
	 */
	public abstract PK create(E newInstance);

	/**
	 * 物理删除实体e
	 * 
	 * @param e
	 *            实体对象
	 */
	public abstract boolean delete(E entity);

	/**
	 * 根据实体主键物理删除指定的实体对象
	 * 
	 * @param pk
	 *            实体主键
	 */
	public abstract boolean deleteById(final PK pk);

	/**
	 * 动态查询符合条件的实体对象集合
	 * 
	 * @param type
	 *            实体对象的类型
	 * @param parameters
	 *            参数MAP
	 * @return
	 */
	public abstract List<E> dynamicList(final Map parameters);

	/**
	 * 查询符合条件的实体对象，并分页的形式返回
	 * 
	 * @param type
	 *            实体对象的类型
	 * @param pageIndex
	 *            当前页码
	 * @param pageSize
	 *            每页的记录数
	 * @param parameters
	 *            参数MAP
	 * @return
	 */
	public abstract Page<E> dynamicPage(final Pagination pagination,
			final Map parameters);

	/**
	 * 返回所有的实体对象集合，不包括已经做上删除标记的
	 * 
	 * @param type
	 *            实体对象的类型
	 * @return
	 */
	public abstract List<E> listAll();

	public abstract List<E> listByNamedQuery(final String qname,
			final HashMap<String, Object> parameters);

	public abstract List<E> listByNamedQuery(final String qname,
			final Object... parameters);

	public abstract List<E> listByNamedQuery(final String qname,
			final Pagination pagination,
			final HashMap<String, Object> parameters);

	public abstract List<E> listByNamedQuery(final String qname,
			final Pagination pagination, final Object... parameters);

	/**
	 * 返回所有已经做上删除标记的实体对象集合
	 * 
	 * @param type
	 *            实体对象的类型
	 * @return
	 */
	public abstract List<E> listInvalidAll();

	/**
	 * 根据实体主键获取实体对象
	 * 
	 * @param pk
	 *            实体主键
	 * @return 如对应的实体不存在返回 null
	 */
	public abstract E load(final PK pk);

	public abstract E loadByNamedQuery(final String qname,
			final HashMap<String, Object> parameters);

	public abstract E loadByNamedQuery(final String qname,
			final Object... parameters);

	/**
	 * 基于所有的实体对象做分页，只处理没有做上删除标识的实体
	 * 
	 * @param type
	 *            实体对象的类型
	 * @param pageIndex
	 *            当前页码
	 * @param pageSize
	 *            每页的记录数
	 * @return
	 */
	public abstract Page<E> pageAll(final Pagination pagination);

	public abstract Page<E> pageByNamedQuery(final String qname,
			final Pagination pagination,
			final HashMap<String, Object> parameters);

	public abstract Page<E> pageByNamedQuery(final String qname,
			final Pagination pagination, final Object... parameters);

	/**
	 * 基于所有的实体对象做分页，只处理已做上删除标记的实体
	 * 
	 * @param type
	 *            实体对象的类型
	 * @param pageIndex
	 *            当前页码
	 * @param pageSize
	 *            每页的记录数
	 * @return
	 */
	public abstract Page<E> pageInvalidAll(final Pagination pagination);

	/**
	 * 直接恢复实体的删除标记
	 * 
	 * @param entity
	 *            要进行恢复操作的实体
	 */
	public abstract boolean recovery(final DeletedFlag entity);

	/**
	 * 根据实体主键将指定的实体对象删除标记取消，属于恢复逻辑删除
	 * 
	 * @param type
	 *            实体对象的类型
	 * @param pk
	 *            实体主键
	 */
	public abstract boolean recoveryById(final PK pk);

	public abstract E refresh(final E entity);

	/**
	 * 根据实体pk将指定的实体对象做上删除标记，属于逻辑删除
	 * 
	 * @param type
	 *            实体对象的类型
	 * @param pk
	 *            实体主键
	 */
	public abstract boolean remove(final DeletedFlag entity);

	/**
	 * 根据实体pk将指定的实体对象做上删除标记，属于逻辑删除
	 * 
	 * @param type
	 *            实体对象的类型
	 * @param pk
	 *            实体主键
	 */
	public abstract boolean removeById(final PK pk);

	/**
	 * 更新实体对象
	 * 
	 * @param entity
	 */
	public abstract E update(E entity);

}