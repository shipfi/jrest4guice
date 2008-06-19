package ${context.packageName}.service;


import ${context.packageName}.entity.${context.entityName};
import ${context.packageName}.service.impl.${context.entityName}ServiceBean;
import org.jrest4guice.client.Page;

import com.google.inject.ImplementedBy;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@ImplementedBy(${context.entityName}ServiceBean.class)
public interface ${context.entityName}Service {
	public String create${context.entityName}(${context.entityName} entity);
	
	public Page<${context.entityName}> list${context.entityName}s(int pageIndex,int pageSize);
	
	public ${context.entityName} find${context.entityName}ById(String entityId);
	
	public void update${context.entityName}(${context.entityName} entity);

	public void delete${context.entityName}(String entityId);
}
