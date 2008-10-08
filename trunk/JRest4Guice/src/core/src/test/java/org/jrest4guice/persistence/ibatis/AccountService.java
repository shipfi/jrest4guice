package org.jrest4guice.persistence.ibatis;

import java.sql.SQLException;
import java.util.List;

import org.jrest4guice.persistence.ibatis.annotations.Delete;
import org.jrest4guice.persistence.ibatis.annotations.IbatisDao;
import org.jrest4guice.persistence.ibatis.annotations.Insert;
import org.jrest4guice.persistence.ibatis.annotations.Select;
import org.jrest4guice.persistence.ibatis.annotations.Update;

import com.google.inject.Inject;
import com.ibatis.sqlmap.client.SqlMapClient;

@IbatisDao
@SuppressWarnings("unchecked")
public class AccountService {

	@Inject
	private SqlMapClient sqlMapper;

	@Select(id = "selectAllAccounts", sql = "select * from ACCOUNT")
	public List<Account> selectAllAccounts() throws SQLException {
		return sqlMapper.queryForList("selectAllAccounts");
	}

	@Select(id = "selectAccountById", sql = "select ACC_ID as id,ACC_FIRST_NAME as firstName,ACC_LAST_NAME as lastName,ACC_EMAIL as emailAddress from ACCOUNT where ACC_ID = #id#")
	public Account selectAccountById(int id) throws SQLException {
		return (Account) sqlMapper.queryForObject("selectAccountById", id);
	}

	@Insert(id = "insertAccount", sql = "insert into ACCOUNT (ACC_ID,ACC_FIRST_NAME,ACC_LAST_NAME,ACC_EMAIL) values (#id#, #firstName#, #lastName#, #emailAddress#)")
	public void insertAccount(Account account) throws SQLException {
		sqlMapper.insert("insertAccount", account);
	}

	@Update(id = "updateAccount", sql = "update ACCOUNT set ACC_FIRST_NAME = #firstName#,ACC_LAST_NAME = #lastName#,ACC_EMAIL = #emailAddress# where ACC_ID = #id#")
	public void updateAccount(Account account) throws SQLException {
		sqlMapper.update("updateAccount", account);
	}

	@Delete(id = "deleteAccount", sql = "delete from ACCOUNT where ACC_ID = #id#")
	public void deleteAccount(int id) throws SQLException {
		sqlMapper.delete("deleteAccount", id);
	}

}
