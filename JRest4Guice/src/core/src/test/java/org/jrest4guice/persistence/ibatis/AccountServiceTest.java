package org.jrest4guice.persistence.ibatis;

import java.util.List;

import org.jrest4guice.guice.GuiceContext;


public class AccountServiceTest {
	private static AccountService service;
	public static void main(String[] args) throws Exception{
		// 初始化JRest4Guice
		GuiceContext.getInstance().useIbatis("org.jrest4guice.persistence.ibatis").init();
		// 获取服务
		service = GuiceContext.getInstance().getBean(AccountService.class);
		List<Account> accounts = service.selectAllAccounts();
		for(Account account:accounts)
			System.out.println(account.getFirstName()+" "+account.getLastName());
	}
}
