package org.cms.service;

import java.util.List;

import org.cms.entity.DownloadComment;
import org.cms.exception.ServiceException;


/**
 * 下载评论业务处理接口
 * 

 */

public interface DownloadCommentService  {

	/**
	 * 创建下载评论
	 * 
	 * @param downloadComment 下载评论
	 */
	public void createDownloadComment(DownloadComment downloadComment) throws ServiceException ;
		
	/**
	 * 取得文章对应的评论列表
	 * @param contentId　文章编号
	 * @param keyword 内容关键字 默认keyword为""
	 * @param pagination 分页对象
	 * @return 评论列表
	 * @throws DAOException 数据处理异常
	 */
	public List<DownloadComment> getComments(int downloadId, String keyword,  int pageIndex,int pageSize) throws ServiceException;


	/**
	 * 删除评论 
	 * 
	 * @param downloadId 下载编号
	 * @param commentId 评论编号
	 * @throws ServiceException
	 */
	public DownloadComment deleteDownloadComment(int downloadId,int commentId) throws ServiceException;
}
