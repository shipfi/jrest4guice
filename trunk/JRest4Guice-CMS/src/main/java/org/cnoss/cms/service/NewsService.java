/*
 * @(#)NewsService.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.service;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.News;
import org.cnoss.cms.exception.ServiceException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * 新闻模块业务处理接口
 *
 */
public interface NewsService {

    /**
     * 创建新闻
     *
     * @param news 新闻对象
     */
    public void createNews(News news) throws ServiceException;

    /**
     * 修改新闻信息
     *
     * @param admin 新闻信息
     */
    public void editNews(News news) throws ServiceException;

    /**
     * 取得新闻列表
     *
     * @param state 新闻分类 0、前台页面　1、后台显示 2、网站动态
     * @return List 新闻列表
     */
    public List<News> getNewss(int state) throws ServiceException;

    /**
     * 取得新闻信息
     *
     * @param newsId 新闻编号
     */
    public News getNews(Long newsId) throws ServiceException;

    /**
     * 删除新闻
     * @param id 新闻编号
     * @throws ServiceException
     * @return content 删除的content对象
     */
    public News deleteNews(Long id) throws ServiceException;
}
