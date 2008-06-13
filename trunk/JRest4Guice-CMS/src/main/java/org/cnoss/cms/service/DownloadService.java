/*
 * @(#)DownloadService.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.service;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.Download;
import org.cnoss.cms.exception.ServiceException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * 下载模块业务接口
 *
 */
public interface DownloadService {

    /**
     * 创建下载条目
     *
     * @param Download download　内容
     */
    public void createDownload(Download download) throws ServiceException;

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
    public Download getDownload(Long downloadId) throws ServiceException;

    /**
     * 分页取得下载条目
     *
     * @param classId 类别编号
     *
     * @param isVouch 是否被推荐 0,不推荐 1,推荐 2,全部
     * @param pagination　分页对象
     */
    public List<Download> getDownloads(Long classId, int isVouch, int pageIndex, int pageSize) throws ServiceException;

    /**
     * 删除下载
     * @param id 下载编号
     * @throws ServiceException
     * @return download 删除的download对象
     */
    public Download deleteDownload(Long id) throws ServiceException;

    /**
     * 删除多个下载条目
     *
     * @param ids 下载条目编号数组
     *
     * @throws ServiceException
     */
    public void deleteDownloads(Long[] ids) throws ServiceException;

    /**
     * 批量更新下载内容类别
     * @param oldClassId 旧下载编号
     * @param newClassId 新下载编号
     * @throws ServiceException
     */
    public void updateDownloads(Long oldClassId, Long newClassId) throws ServiceException;

    /**
     * 根据类别批量删除下载内容
     * @param classId
     * @throws ServiceException
     */
    public void deleteDownloadsByClassId(Long classId) throws ServiceException;

    /**
     * 更新下载推荐状态
     * @param downloadId 下载编号
     * @param isVouch 是否推荐  0、不推荐 1、推荐
     */
    public void updateDownloadVouch(Long downloadId, int isVouch) throws ServiceException;

    /**
     * 列表站点推荐下载
     *
     * @param classId 下载类别 ( 0:全部类别查找 )
     * @param num 检索下载信息数
     * @return　list 下载集合
     * @throws ServiceException 业务处理异常
     */
    public List<Download> getDownloadsByCommend(Long classId, int num) throws ServiceException;

    /**
     * 列表站点最新下载
     *
     * @param classId 下载类别 ( 0:全部类别查找 )
     * @param num 检索下载信息数
     * @return　list 下载集合
     * @throws ServiceException 业务处理异常
     */
    public List<Download> getDownloadsByNewly(Long classId, int num) throws ServiceException;

    /**
     * 列表站点热门下载
     *
     * @param classId 下载类别 ( 0:全部类别查找 )
     * @param num 检索下载信息数
     * @return　list 下载集合
     * @throws ServiceException 业务处理异常
     */
    public List<Download> getDownloadsByHot(Long classId, int num) throws ServiceException;

    /**
     * 更新文件被下载次数
     * @param downlaodId 下载编号
     * @throws DAOException
     */
    public void updateDownloadTimes(Long downloadId) throws ServiceException;

    /**
     * 更新下载页面被访问次数
     * @param downlaodId 下载编号
     * @throws DAOException
     */
    public void updateDownloadHits(Long downloadId) throws ServiceException;
}
