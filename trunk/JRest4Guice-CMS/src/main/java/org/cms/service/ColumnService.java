package org.cms.service;

import java.util.List;

import org.cms.entity.Column;
import org.cms.exception.ServiceException;




/**
 * 栏目模块业务处理接口
 * 
 */
public interface ColumnService  {

	/**
	 * 创建栏目
	 * 
	 * @param column 栏目
	 */
	public void createColumn(Column column) throws ServiceException ;
	
	/**
	 * 修改栏目
	 * 
	 * @param column 栏目
	 */
	public void editColumn(Column column) throws ServiceException ;
	
	/**
	 * 删除栏目
	 * 
	 * @param columnId 栏目编号
	 * @throws ServiceException
	 */
	public void deleteColumn(int columnId) throws ServiceException ;
	
	/**
	 * 取得栏目
	 * 
	 * @param columnId 栏目编号
	 * @return Column 栏目对象
	 * @throws ServiceException 
	 */
	public Column getColumn(int columnId) throws ServiceException ;
	
	/**
	 * 栏目列表
	 * @return
	 * @throws ServiceException
	 */
	public List<Column> getColumns() throws ServiceException;
	
}
