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
			
			//查询
			accounts = service.findAll();
			Assert.assertEquals(1, accounts.size());

			for (Account ac : accounts)
				System.out.println(ac.getFirstName() + ac.getLastName()
						+ "的邮件地址是：" + ac.getEmailAddress());

			// 修改
			account = accounts.get(0);
			account.setFirstName("刘");
			service.updateAccount(account);

			account = service.getAccountById(account.getId());

			Assert.assertNotNull(account);

			Assert.assertEquals("刘", account.getFirstName());

			// 删除
			service.deleteAccount(account.getId());

			accounts = service.findAll();
			Assert.assertEquals(0, accounts.size());
		} catch (SQLException e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}
}
