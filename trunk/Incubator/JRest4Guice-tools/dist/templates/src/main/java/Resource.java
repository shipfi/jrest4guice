package ${context.packageName}.resource;

import ${context.packageName}.entity.${context.entityName};
import ${context.packageName}.service.${context.entityName}Service;
import org.jrest4guice.client.Page;
import org.jrest4guice.rest.annotations.Delete;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.ModelBean;
import org.jrest4guice.rest.annotations.Parameter;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.Post;
import org.jrest4guice.rest.annotations.Put;
import org.jrest4guice.rest.annotations.Remote;

import com.google.inject.Inject;

/**
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
@Path( { "/${context.entityName}", "/${context.entityName}s/{id}" })
@Remote
public class ${context.entityName}Resource {
	@Inject
	private ${context.entityName}Service service;

	@Post
	public String createContact(@ModelBean ${context.entityName} entity) {
		return this.service.create${context.entityName}(entity);
	}

	@Put
	public void put${context.entityName}(@ModelBean ${context.entityName} entity) {
		this.service.update${context.entityName}(entity);
	}

	@Get
	@Path("/${context.entityName}s")
	public Page<${context.entityName}> list${context.entityName}s(int pageIndex, int pageSize) {
		return this.service.list${context.entityName}s(pageIndex, pageSize);
	}

	@Get
	public ${context.entityName} get${context.entityName}(@Parameter("id") String id) {
		return this.service.find${context.entityName}ById(id);
	}

	@Delete
	public void delete${context.entityName}(@Parameter("id") String id) {
		this.service.delete${context.entityName}(id);
	}
}
