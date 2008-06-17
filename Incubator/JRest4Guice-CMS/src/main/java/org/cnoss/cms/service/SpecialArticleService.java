/*
 * @(#)SpecialArticleService.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.service;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.SpecialArticle;
import org.cnoss.cms.exception.ServiceException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * 专题相关文章接口
 *
 */
public interface SpecialArticleService {

    /**
     * 创建专题相关文章
     *
     * @param specialArticle 专题文章
     */
    public void createSpecialArticle(SpecialArticle specialArticle) throws ServiceException;

    /**
     * 修改专题相关文章
     *
     * @param specialArticle 专题文章
     */
    public void editSpecialArticle(SpecialArticle specialArticle) throws ServiceException;

    /**
     * 删除专题相关文章
     *
     * @param specialArticleId 专题文章编号
     * @throws ServiceException
     */
    public void deleteSpecialArticle(Long specialId, int specialArticleId) throws ServiceException;

    /**
     * 取得专题相关文章
     *
     * @param specialArticleId 专题相关文章编号
     * @return SpecialArticle 专题文章对象
     * @throws ServiceException
     */
    public SpecialArticle getSpecialArticle(Long specialArticleId) throws ServiceException;

    /**
     * 列表专题相关文章
     *
     * @param specialId 专题编号
     * @return
     * @throws ServiceException
     */
    public List<SpecialArticle> getSpecialArticles(Long specialId) throws ServiceException;
}
