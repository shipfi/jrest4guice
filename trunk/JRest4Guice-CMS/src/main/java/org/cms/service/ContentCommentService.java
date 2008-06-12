package org.cms.service;

import java.util.List;

import org.cms.entity.ContentComment;
import org.cms.exception.ServiceException;





/**
 * 内容评论模块业务处理接口
 * 

 */
public interface ContentCommentService  {

	/**
	 * 创建内容评论
	 * 
	 * @param ContentComment contentComment　评论对象
	 */
	public void createContentComment(ContentComment contentComment) throws ServiceException ;
		
	/**
	 * 取得文章对应的评论列表
	 * @param contentId　文章编号
	 * @param keyword 内容关键字 默认keyword为""
	 * @return 评论列表
	 * @throws ServiceException 数据处理异常
	 */
	public List<ContentComment> getComments(int contentId, String keyword, int pageIndex,int pageSize) throws ServiceException ;

	/**
	 * 删除评论 
	 * 
	 * @param contentId 文章编号
	 * @param commentId 评论编号
	 * @return contentComment 评论编号
	 * @throws ServiceException
	 */
	public ContentComment deleteContentComment(int contentId,int commentId) throws ServiceException;

	
}
