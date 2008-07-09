package ${context.packageName}.service;

import java.util.List;

import junit.framework.Assert;

import ${context.packageName}.entity.${context.entityName};
import org.jrest4guice.client.Page;
import org.jrest4guice.guice.GuiceContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class ${context.entityName}ServiceTest {
	private static ${context.entityName}Service service;
	
	private static String id;

	@BeforeClass
	public static void setUp() throws Exception {
		//初始化JRest4Guice
		GuiceContext.getInstance().useJPA().init();
		//获取服务
		service = GuiceContext.getInstance().getBean(${context.entityName}Service.class);
	}

	@Test
	public void testCreate${context.entityName}() {
		${context.entityName} entity = new ${context.entityName}();
		entity.setName("cnoss");
		service.create${context.entityName}(entity);
		id = entity.getId();
		Assert.assertNotNull(id);
	}

	@Test
	public void testList${context.entityName}s() {
		Page<${context.entityName}> page = service.list${context.entityName}s(1, 10);
		Assert.assertNotNull(page);
		
		List<${context.entityName}> entites = page.getResult();
		
		Assert.assertEquals(entites.size(), 1);
	}

	@Test
	public void testFind${context.entityName}ById() {
		Assert.assertNotNull(id);
		${context.entityName} entity = service.find${context.entityName}ById(id);
		Assert.assertNotNull(entity);
	}

	@Test
	public void testUpdate${context.entityName}() {
		Assert.assertNotNull(id);
		${context.entityName} entity = service.find${context.entityName}ById(id);
		Assert.assertNotNull(entity);
		
		entity.setName("JRest4Guice");
		
		service.update${context.entityName}(entity);
		
		entity = service.find${context.entityName}ById(id);
		Assert.assertEquals(entity.getName(),"JRest4Guice");
	}

	@Test
	public void testDelete${context.entityName}() {
		Assert.assertNotNull(id);
		service.delete${context.entityName}(id);

		${context.entityName} entity = service.find${context.entityName}ById(id);
		Assert.assertNull(entity);
	}

}
