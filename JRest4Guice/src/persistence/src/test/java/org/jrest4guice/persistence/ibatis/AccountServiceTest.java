package org.jrest4guice.persistence.ibatis;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.guice.PersistenceGuiceContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class AccountServiceTest {
	private static AccountService service;

	@BeforeClass
	public static void setUp() throws Exception {
		// 初始化JRest4Guice
		PersistenceGuiceContext.getInstance().useIbatis(
				"org.jrest4guice.persistence.ibatis").init();
		// 获取服务
		service = GuiceContext.getInstance().getBean(AccountService.class);
	}

	@Test
	public void doTest() {
		List<Account> accounts;
		try {
			Account account = new Account();
			account.setFirstName("张");
			account.setLastName("学友");
			account.setEmailAddress("jackey@rest4g.org");
			// 添加
			service.createAccount(account);

			account = new Account();
			account.setFirstName("刘");
			account.setLastName("学友");
			account.setEmailAddress("test@rest4g.org");
			// 添加
			service.createAccount(account);
			
			//查询（按lastName）
			Account queryCondition = new Account();
			queryCondition.setLastName("学友");
			accounts = service.queryAccounts(queryCondition);
			Assert.assertEquals(2, accounts.size());
			
			//查询（按firstName和lastName）
			queryCondition.setFirstName("张");
			accounts = service.queryAccounts(queryCondition);
			Assert.assertEquals(1, accounts.size());

			// 修改
			account = accounts.get(0);
			account.setFirstName("何");
			service.updateAccount(account);
			account = service.getAccountById(account.getId());
			Assert.assertNotNull(account);
			Assert.assertEquals("何", account.getFirstName());

			//查询所有
			accounts = service.findAll();
			Assert.assertEquals(2, accounts.size());

			// 删除
			for (Account ac : accounts){
				service.deleteAccount(ac.getId());
			}
			
			//断言删除的结果
			accounts = service.findAll();
			Assert.assertEquals(0, accounts.size());
		} catch (SQLException e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}
}
