package ${context.packageName}.domain;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;

import ${context.packageName}.entity.${context.entityName};
import org.jrest4guice.client.Page;
import org.jrest4guice.client.Pagination;
import org.jrest4guice.jpa.BaseEntityManager;
import org.jrest4guice.transaction.annotations.Transactional;
import org.jrest4guice.transaction.annotations.TransactionalType;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings( { "unchecked", "unused" })
public class ${context.entityName}Domain{
	private BaseEntityManager<String, ${context.entityName}> entityManager;

	@Inject
	private void init(EntityManager em) {
		this.entityManager = new BaseEntityManager<String, ${context.entityName}>(
				${context.entityName}.class, em);
	}

	@Transactional
	public String create${context.entityName}(${context.entityName} entity) {
		if (entity == null)
			throw new RuntimeException("实体对象不能为空");

		if (this.entityManager.loadByNamedQuery("byName", entity.getName()) != null) {
			throw new RuntimeException("实体对象的名称相同，请重新输入");
		}

		this.entityManager.create(entity);
		return entity.getId();
	}

	@Transactional
	public void delete${context.entityName}(String entityId) {
		String[] ids = entityId.split(",");
		${context.entityName} entity;
		for (String id : ids) {
			entity = this.find${context.entityName}ById(id);
			if (entity == null)
				throw new RuntimeException("实体对象不存在");
			this.entityManager.delete(entity);
		}
	}

	@Transactional(type = TransactionalType.READOLNY)
	public ${context.entityName} find${context.entityName}ById(String entityId) {
		return this.entityManager.load(entityId);
	}

	@Transactional(type = TransactionalType.READOLNY)
	public Page<${context.entityName}> list${context.entityName}s(int pageIndex, int pageSize)
			throws RuntimeException {
		return this.entityManager.pageByNamedQuery("list", new Pagination(
				pageIndex, pageSize));
	}

	@Transactional
	public void update${context.entityName}(${context.entityName} entity) {
		if (entity == null)
			throw new RuntimeException("实体对象不能为空");

		${context.entityName} tmp${context.entityName} = this.entityManager.loadByNamedQuery(
				"byName", entity.getName());
		if (tmp${context.entityName} != null
				&& !entity.getId().equals(tmp${context.entityName}.getId()))
			throw new RuntimeException("实体对象的名称相同，请重新输入");

		this.entityManager.update(entity);
	}
}
