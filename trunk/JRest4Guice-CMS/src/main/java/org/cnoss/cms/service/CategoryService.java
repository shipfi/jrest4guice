package org.cnoss.cms.service;

import java.util.List;

import org.cnoss.cms.entity.Category;
import org.cnoss.cms.exception.ServiceException;


/**
 * 文章分类模块业务处理接口
 * 
 */
public interface CategoryService{

	/**
	 * 创建类别
	 * 
	 * 
	 * @param category　分类
	 */
	public void createCategory(Category category) throws ServiceException ;
		
	/**
	 * 修改类别
	 * 
	 * @param category　分类
	 */
	public void editCategory(Category category) throws ServiceException;
		
	/**
	 * 取得指定编号的类别信息
	 * 
	 * @param categoryId 类别编号
	 */
	public Category getCategory(int categoryId) throws ServiceException ;
		
	/**
	 * 删除多个分类 
	 * 
	 * @param ids 类别编号数组
	 * 
	 * @throws ServiceException
	 */
	public void deleteCategories(int[] ids) throws ServiceException;

	/**
	 * 删除分类 
	 * 
	 * @param id 类别编号
	 * @throws ServiceException
	 */
	public void deleteCategory(int id) throws ServiceException;

	/**
	 * 取得类别列表
	 * 
	 * @throws ServiceException
	 */
	public List<Category> getCategories() throws ServiceException;
		
}
