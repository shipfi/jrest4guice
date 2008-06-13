/*
 * @(#)CategoryService.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.service;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.Category;
import org.cnoss.cms.exception.ServiceException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * 文章分类模块业务处理接口
 *
 */
public interface CategoryService {

    /**
     * 创建类别
     *
     *
     * @param category　分类
     */
    public void createCategory(Category category) throws ServiceException;

    /**
     * 修改类别
     *
     * @param category　分类
     */
    public void editCategory(Category category) throws ServiceException;

    /**
     * 取得指定编号的类别信息
     *
     * @param categoryId 类别编号
     */
    public Category getCategory(Long categoryId) throws ServiceException;

    /**
     * 删除多个分类
     *
     * @param ids 类别编号数组
     *
     * @throws ServiceException
     */
    public void deleteCategories(Long[] ids) throws ServiceException;

    /**
     * 删除分类
     *
     * @param id 类别编号
     * @throws ServiceException
     */
    public void deleteCategory(Long id) throws ServiceException;

    /**
     * 取得类别列表
     *
     * @throws ServiceException
     */
    public List<Category> getCategories() throws ServiceException;
}
