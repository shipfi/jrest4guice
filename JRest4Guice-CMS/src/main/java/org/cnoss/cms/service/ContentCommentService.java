/*
 * @(#)ContentCommentService.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.service;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.ContentComment;
import org.cnoss.cms.exception.ServiceException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * 内容评论模块业务处理接口
 *
 *
 */
public interface ContentCommentService {

    /**
     * 创建内容评论
     *
     * @param ContentComment contentComment　评论对象
     */
    public void createContentComment(ContentComment contentComment) throws ServiceException;

    /**
     * 取得文章对应的评论列表
     * @param contentId　文章编号
     * @param keyword 内容关键字 默认keyword为""
     * @return 评论列表
     * @throws ServiceException 数据处理异常
     */
    public List<ContentComment> getComments(Long contentId, String keyword, int pageIndex, int pageSize)
            throws ServiceException;

    /**
     * 删除评论
     *
     * @param contentId 文章编号
     * @param commentId 评论编号
     * @return contentComment 评论编号
     * @throws ServiceException
     */
    public ContentComment deleteContentComment(Long contentId, Long commentId) throws ServiceException;
}
