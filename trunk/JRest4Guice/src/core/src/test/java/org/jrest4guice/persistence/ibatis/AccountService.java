package org.jrest4guice.persistence.ibatis;

import java.sql.SQLException;
import java.util.List;

import org.jrest4guice.persistence.ibatis.annotations.Delete;
import org.jrest4guice.persistence.ibatis.annotations.IbatisDao;
import org.jrest4guice.persistence.ibatis.annotations.Insert;
import org.jrest4guice.persistence.ibatis.annotations.Select;
import org.jrest4guice.persistence.ibatis.annotations.Update;
import org.jrest4guice.transaction.annotations.Transactional;
import org.jrest4guice.transaction.annotations.TransactionalType;

import com.google.inject.Inject;
import com.ibatis.sqlmap.client.SqlMapClient;

@IbatisDao
@SuppressWarnings("unchecked")
@Transactional
public class AccountService {
	@Inject
	private SqlMapClient sqlMapper;

	@Select(id = "selectAllAccounts", sql = "select * from ACCOUNT")
	@Transactional(type=TransactionalType.READOLNY)
	public List<Account> findAll() throws SQLException {
		return sqlMapper.queryForList("selectAllAccounts");
	}

	@Select(sql = "select id as id,firstName,lastName,emailAddress from ACCOUNT where id = #id#")
	@Transactional(type=TransactionalType.READOLNY)
	public Account getAccountById(int id) throws SQLException {
		return (Account) sqlMapper.queryForObject("getAccountById", id);
	}

	@Insert(id = "insertAccount", sql = "insert into ACCOUNT (id,firstName,lastName,emailAddress) " +
			"values (#id#, #firstName#, #lastName#, #emailAddress#)")
	public void createAccount(Account account) throws SQLException {
		sqlMapper.insert("insertAccount", account);
	}

	@Update(sql = "update ACCOUNT set firstName = #firstName#,lastName = #lastName#," +
			"emailAddress = #emailAddress# where id = #id#")
	public void updateAccount(Account account) throws SQLException {
		sqlMapper.update("updateAccount", account);
	}

	@Delete(id = "deleteAccount", sql = "delete from ACCOUNT where id = #id#")
	public void deleteAccount(int id) throws SQLException {
		sqlMapper.delete("deleteAccount", id);
	}
}
