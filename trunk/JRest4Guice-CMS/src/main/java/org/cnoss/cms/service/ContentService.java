package org.cnoss.cms.service;

import java.util.List;

import org.cnoss.cms.entity.Content;
import org.cnoss.cms.exception.ServiceException;





/**
 * 内容模块业务处理接口
 * 

 */
public interface ContentService {

	/**
	 * 创建内容
	 * 
	 * @param content　内容
	 * @param isPagination 文章是否分页
	 */
	public void createContent(Content content,int isPagination) throws ServiceException ;
		
	/**
	 * 修改内容信息
	 * 
	 * @param content　内容
	 * @param isPagination 是否分页
	 */
	public void editContent(Content content) throws ServiceException;
		
	/**
	 * 取得内容信息
	 * 
	 * @param contentId 内容编号
	 */
	public Content getContent(int contentId) throws ServiceException ;
		
	/**
	 * 分页取得内容
	 * 
	 * @param categoryId 类别编号
	 * @param keyword 模糊检索关键字
	 * @param status 状态 1、已审核 0、未审核 2全部
	 * @param isVouch 是否被推荐 0,不推荐 1,推荐 2,全部
	 * @param pagination　分页对象
	 */
	public List<Content> getContents(int categoryId, String keyword,  int status, int isVouch, int pageIndex,int pageSize) throws ServiceException ;

	/**
	 * 删除多个内容 
	 * 
	 * @param ids 内容编号数组
	 * 
	 * @throws ServiceException
	 */
	public void deleteContents(int[] ids) throws ServiceException;

	/**
	 * 删除内容
	 * @param id 内容编号
	 * @throws ServiceException
	 * @return content 删除的content对象
	 */
	public Content deleteContent(int id) throws ServiceException;
	
	/**
	 * 批量更新内容类别
	 * @param categoryId 内容编号
	 * @throws DAOException
	 */
	public void updateContent(int oldCategoryId,int newCategoryId) throws ServiceException ;

	/**
	 * 更新内容状态
	 * @param contentId 内容编号
	 * @param isVouch 是否推荐
	 */
	public void updateContentVouch(int contentId, int isVouch) throws ServiceException ;	
	
	/**
	 * 列表站点推荐文章
	 * 
	 * @param categoryId 文章类别 ( 0:全部类别查找 )
	 * @param dataNum 检索文章数
	 * @return　list 文章集合 
	 * @throws ServiceException 业务处理异常
	 */
	public List<Content> getContentsByCommend(int categoryId, int dataNum) throws ServiceException ;
	
	/**
	 * 列表一周内指定数目的热门文章
	 * 
	 * @param categoryId 文章类别
	 * @param dataNum 检索文章数
	 * @return　list 文章集合 
	 * @throws DAOException
	 */
	public List<Content> getContentsByWeekhot(int categoryId, int dataNum) throws ServiceException ;
	
	/**
	 * 列表最新发表的文章
	 * 
	 * @param categoryId 文章类别 ( 0:全部类别查找 )
	 * @param dataNum 检索文章数
	 * @return　list 文章集合 
	 * @throws DAOException
	 */
	public List<Content> getContentsByNewly(int categoryId, int dataNum) throws ServiceException ;	

	/**
	 * 列表月内指定数目的热门文章
	 * 
	 * @param categoryId 文章类别
	 * @param dataNum 检索文章数
	 * @return　list 文章集合 
	 * @throws DAOException
	 */
	public List<Content> getContentsByMonthhot(int categoryId, int dataNum) throws ServiceException ;

	

	/**
	 * 更新文章被访问次数
	 * @param contentId 内容编号
	 * @throws DAOException 
	 */
	public void updateContentHits(int contentId) throws ServiceException ;
	
}
