package org.cms.service;

import java.util.List;

import org.cms.entity.DownClass;
import org.cms.exception.ServiceException;





/**
 * 下载分类模块业务处理接口
 * 

 */
public interface DownClassService {

	/**
	 * 创建类别
	 * 
	 * 
	 * @param downClass　分类
	 */
	public void createDownClass(DownClass downClass) throws ServiceException ;
		
	/**
	 * 修改类别
	 * 
	 * @param downClass　分类
	 */
	public void editDownClass(DownClass downClass) throws ServiceException;
		
	/**
	 * 取得指定编号的类别信息
	 * 
	 * @param downClassId 类别编号
	 */
	public DownClass getDownClass(int downClassId) throws ServiceException ;
		
	/**
	 * 删除多个分类 
	 * 
	 * @param ids 类别编号数组
	 * 
	 * @throws ServiceException
	 */
	public void deleteDownClasses(int[] ids) throws ServiceException;

	/**
	 * 删除分类 
	 * 
	 * @param id 类别编号
	 * @throws ServiceException
	 */
	public void deleteDownClass(int id) throws ServiceException;

	/**
	 * 取得类别列表
	 * 
	 * @throws ServiceException
	 */
	public List<DownClass> getDownClasses() throws ServiceException;
	

}
