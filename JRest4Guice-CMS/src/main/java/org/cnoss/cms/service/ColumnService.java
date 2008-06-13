/*
 * @(#)ColumnService.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.service;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.TColumn;
import org.cnoss.cms.exception.ServiceException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * 栏目模块业务处理接口
 *
 */
public interface ColumnService {

    /**
     * 创建栏目
     *
     * @param column 栏目
     */
    public void createColumn(TColumn column) throws ServiceException;

    /**
     * 修改栏目
     *
     * @param column 栏目
     */
    public void editColumn(TColumn column) throws ServiceException;

    /**
     * 删除栏目
     *
     * @param columnId 栏目编号
     * @throws ServiceException
     */
    public void deleteColumn(Long columnId) throws ServiceException;

    /**
     * 取得栏目
     *
     * @param columnId 栏目编号
     * @return Column 栏目对象
     * @throws ServiceException
     */
    public TColumn getColumn(Long columnId) throws ServiceException;

    /**
     * 栏目列表
     * @return
     * @throws ServiceException
     */
    public List<TColumn> getColumns() throws ServiceException;
}
