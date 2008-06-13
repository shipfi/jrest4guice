package org.cnoss.cms.service;

import java.util.List;

import org.cnoss.cms.entity.Download;
import org.cnoss.cms.exception.ServiceException;





/**
 * 下载模块业务接口
 * 
*/

public interface DownloadService{

	/**
	 * 创建下载条目
	 * 
	 * @param Download download　内容
	 */
	public void createDownload(Download download) throws ServiceException ;
		
	/**
	 * 修改下载内容
	 * 
	 * @param Download download　内容
	 */
	public void editDownload(Download download) throws ServiceException;
		
	/**
	 * 查询下载内容
	 * 
	 * @param contentId 内容编号
	 */
	public Download getDownload(int downloadId) throws ServiceException ;
		
	/**
	 * 分页取得下载条目
	 * 
	 * @param classId 类别编号

	 * @param isVouch 是否被推荐 0,不推荐 1,推荐 2,全部
	 * @param pagination　分页对象
	 */
	public List<Download> getDownloads(int classId,int isVouch, int pageIndex,int pageSize) throws ServiceException ;


	/**
	 * 删除下载
	 * @param id 下载编号
	 * @throws ServiceException
	 * @return download 删除的download对象
	 */
	public Download deleteDownload(int id) throws ServiceException;
	
	
	
	/**
	 * 删除多个下载条目 
	 * 
	 * @param ids 下载条目编号数组
	 * 
	 * @throws ServiceException
	 */
	public void deleteDownloads(int[] ids) throws ServiceException;
	
	
	/**
	 * 批量更新下载内容类别
	 * @param oldClassId 旧下载编号
	 * @param newClassId 新下载编号
	 * @throws ServiceException
	 */
	public void updateDownloads(int oldClassId,int newClassId) throws ServiceException;
	
	
	
	/**
	 * 根据类别批量删除下载内容
	 * @param classId
	 * @throws ServiceException
	 */
	public void deleteDownloadsByClassId(int classId) throws ServiceException;

	
	/**
	 * 更新下载推荐状态
	 * @param downloadId 下载编号
	 * @param isVouch 是否推荐  0、不推荐 1、推荐
	 */
	public void updateDownloadVouch(int downloadId, int isVouch) throws ServiceException;

	/**
	 * 列表站点推荐下载
	 * 
	 * @param classId 下载类别 ( 0:全部类别查找 )
	 * @param num 检索下载信息数
	 * @return　list 下载集合 
	 * @throws ServiceException 业务处理异常
	 */
	public List<Download> getDownloadsByCommend(int classId, int num) throws ServiceException ;
	
	/**
	 * 列表站点最新下载
	 * 
	 * @param classId 下载类别 ( 0:全部类别查找 )
	 * @param num 检索下载信息数
	 * @return　list 下载集合 
	 * @throws ServiceException 业务处理异常
	 */
	public List<Download> getDownloadsByNewly(int classId, int num) throws ServiceException ;
	
	/**
	 * 列表站点热门下载
	 * 
	 * @param classId 下载类别 ( 0:全部类别查找 )
	 * @param num 检索下载信息数
	 * @return　list 下载集合 
	 * @throws ServiceException 业务处理异常
	 */
	public List<Download> getDownloadsByHot(int classId, int num) throws ServiceException ;

	/**
	 * 更新文件被下载次数
	 * @param downlaodId 下载编号
	 * @throws DAOException 
	 */
	public void updateDownloadTimes(int downloadId) throws ServiceException ;
	
	/**
	 * 更新下载页面被访问次数
	 * @param downlaodId 下载编号
	 * @throws DAOException 
	 */
	public void updateDownloadHits(int downloadId) throws ServiceException ;
}
