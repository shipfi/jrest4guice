/*
 * @(#)SpecialService.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.service;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.Special;
import org.cnoss.cms.exception.ServiceException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * 专题模块业务处理接口
 *
 *
 */
public interface SpecialService {

    /**
     * 创建专题
     *
     * @param special 专题
     */
    public void createSpecial(Special special) throws ServiceException;

    /**
     * 修改专题
     *
     * @param special 专题
     */
    public void editSpecial(Special special) throws ServiceException;

    /**
     * 删除专题
     *
     * @param specialId 专题编号
     * @throws ServiceException
     */
    public void deleteSpecial(Long specialId) throws ServiceException;

    /**
     * 取得专题
     *
     * @param specialId 专题编号
     * @return Special 专题对象
     * @throws ServiceException
     */
    public Special getSpecial(Long specialId) throws ServiceException;

    public List<Special> getSpecials(String category, String keyWords, int pageIndex, int pageSize)
            throws ServiceException;
}
