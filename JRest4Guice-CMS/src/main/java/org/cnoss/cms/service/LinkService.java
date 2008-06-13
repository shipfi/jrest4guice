/*
 * @(#)LinkService.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.service;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.Link;
import org.cnoss.cms.exception.ServiceException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * 友情链接模块业务处理接口
 *
 */
public interface LinkService {

    /**
     * 创建友情链接
     *
     * @param link 友情链接对象
     */
    public void createLink(Link link) throws ServiceException;

    /**
     * 修改友情链接信息
     *
     * @param link 友情链接对象
     */
    public void editLink(Link link) throws ServiceException;

    /**
     * 取得友情链接列表
     *
     * @param id 友情链接编号
     * @return Link 友情链接
     */
    public Link getLink(Long id) throws ServiceException;

    /**
     * 取得友情链接信息
     *
     * @param isLogo 友情链接类型　０,文字友情链接 1,图片友情链接
     * @param columnId 友情链接栏目编号　０,首页 1,技术文档
     * @return List 友情链接
     */
    public List<Link> getLinks(int isLogo, int columnId) throws ServiceException;

    /**
     * 删除友情链接
     * @param id 友情链接编号
     * @throws ServiceException
     * @return Link 删除友情链接对象
     */
    public Link deleteLink(Long id) throws ServiceException;
}
