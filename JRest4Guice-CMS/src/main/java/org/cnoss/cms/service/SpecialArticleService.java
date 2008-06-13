package org.cnoss.cms.service;

import java.util.List;

import org.cnoss.cms.entity.SpecialArticle;
import org.cnoss.cms.exception.ServiceException;




/**
 * 专题相关文章接口

 */
public interface SpecialArticleService {

	/**
	 * 创建专题相关文章
	 * 
	 * @param specialArticle 专题文章
	 */
	public void createSpecialArticle(SpecialArticle specialArticle) throws ServiceException ;
	
	/**
	 * 修改专题相关文章
	 * 
	 * @param specialArticle 专题文章
	 */
	public void editSpecialArticle(SpecialArticle specialArticle) throws ServiceException ;
	
	/**
	 * 删除专题相关文章
	 * 
	 * @param specialArticleId 专题文章编号
	 * @throws ServiceException
	 */
	public void deleteSpecialArticle(int specialId, int specialArticleId) throws ServiceException ;
	
	/**
	 * 取得专题相关文章
	 * 
	 * @param specialArticleId 专题相关文章编号
	 * @return SpecialArticle 专题文章对象
	 * @throws ServiceException 
	 */
	public SpecialArticle getSpecialArticle(int specialArticleId) throws ServiceException ;
	
	/**
	 * 列表专题相关文章
	 * 
	 * @param specialId 专题编号
	 * @return 
	 * @throws ServiceException
	 */
	public List<SpecialArticle> getSpecialArticles(int specialId) throws ServiceException;
}
