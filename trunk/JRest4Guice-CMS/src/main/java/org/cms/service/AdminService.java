package org.cms.service;

import java.util.List;

import org.cms.entity.Admin;
import org.cms.exception.AdminException;
import org.cms.exception.LoginException;
import org.cms.exception.ServiceException;






/**
 * 管理员模块业务处理接口
 * 
 */
public interface AdminService {

	/**
	 * 创建管理员
	 * 
	 * @param admin 管理员对象
	 */
	public void createAdmin(Admin admin) throws ServiceException ;
		
	/**
	 * 修改管理员密码
	 * 
	 * @param adminId 管理员编号
	 * @param oldPassword 旧密码
	 * @param password 新密码
	 * @throws AdminException
	 * @throws ServiceException
	 */
	public void editAdminPassword(int adminId,String oldPassword,String password) throws AdminException,ServiceException;
	
	/**
	 * 修改管理员信息
	 * 
	 * @param admin 管理员信息
	 */
	public void editAdmin(Admin admin) throws ServiceException ;
	
	/**
	 * 取得管理员信息
	 * 
	 * @param adminId 管理员编号
	 */
	public Admin getAdmin(int adminId) throws ServiceException ;
	
	/**
	 * 根据用户名取得管理员信息
	 * 
	 * @param username 管理员名称
	 */
	public Admin getAdmin(String username) throws ServiceException ;
	
	/**
	 * 取得管理员列表
	 * 
	 * @param state 管理员状态 0,全部　1,正常 2，锁定
	 * @return List 管理员列表
	 */
	public List<Admin> getAdmins(int state) throws ServiceException ;

	/**
	 * 验证管理员登录
	 * 
	 * @param username 用户名
	 * @param password 用户密码
	 * @param AdminActionForm 用户对象
	 * @throws LoginException 登陆Exception
	 */
	public Admin authLogin(String username, String password) throws LoginException,ServiceException;	
	
	/**
	 * 管理员登录时更新登录时间和IP
	 * 
	 * @param adminId 用户编号
	 * @param lastLoginTime 最后登陆时间
	 * @param lastLoginIp 最后登陆的IP
	 * @throws ServiceException
	 */
	public void updateLoginInfo(int adminId,String lastLoginTime,String lastLoginIp) throws ServiceException;	
	
	/**
	 * 更新管理员状态
	 * @param adminId 管理员编号
	 * @param state 管理员状态
	 * @throws DAOException 
	 */
	public void updateAdminState(int adminId, int state) throws ServiceException ;

	/**
	 * 更新用户密码
	 * @param adminId 管理员编号
	 * @param oldPassword　旧密码
	 * @param newPasword　新密码
	 * @throws ServiceException
	 */
	public void updateAdminPassword(int adminId,String oldPassword,String newPasword) throws ServiceException ;
	
}
