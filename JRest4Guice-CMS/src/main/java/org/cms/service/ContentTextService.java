package org.cms.service;




import org.cms.entity.ContentText;
import org.cms.exception.ServiceException;


/**
 * 文章内容模块业务处理接口
 * 

 */
public interface ContentTextService {
	
	/**
	 * 修改文章内容
	 * 
	 * @param contentText 内容文本
	 * @throws ServiceException
	 */
	public void editContentText(ContentText contentText) throws ServiceException;
	
	/**
	 * 查找内容信息
	 * @return
	 * @throws ServiceException
	 */
	public ContentText getContentText(long textId) throws ServiceException;
}
