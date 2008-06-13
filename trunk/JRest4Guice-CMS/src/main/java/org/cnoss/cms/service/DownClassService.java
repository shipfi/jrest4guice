/*
 * @(#)DownClassService.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.service;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.DownClass;
import org.cnoss.cms.exception.ServiceException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * 下载分类模块业务处理接口
 *
 *
 */
public interface DownClassService {

    /**
     * 创建类别
     *
     *
     * @param downClass　分类
     */
    public void createDownClass(DownClass downClass) throws ServiceException;

    /**
     * 修改类别
     *
     * @param downClass　分类
     */
    public void editDownClass(DownClass downClass) throws ServiceException;

    /**
     * 取得指定编号的类别信息
     *
     * @param downClassId 类别编号
     */
    public DownClass getDownClass(Long downClassId) throws ServiceException;

    /**
     * 删除多个分类
     *
     * @param ids 类别编号数组
     *
     * @throws ServiceException
     */
    public void deleteDownClasses(Long[] ids) throws ServiceException;

    /**
     * 删除分类
     *
     * @param id 类别编号
     * @throws ServiceException
     */
    public void deleteDownClass(Long id) throws ServiceException;

    /**
     * 取得类别列表
     *
     * @throws ServiceException
     */
    public List<DownClass> getDownClasses() throws ServiceException;
}
