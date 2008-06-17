/*
 * @(#)ContentTextService.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.service;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.ContentText;
import org.cnoss.cms.exception.ServiceException;

/**
 * 文章内容模块业务处理接口
 *
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
    public ContentText getContentText(Long textId) throws ServiceException;
}
