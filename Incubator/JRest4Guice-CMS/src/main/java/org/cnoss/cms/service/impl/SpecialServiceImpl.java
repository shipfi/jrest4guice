/*
 * @(#)SpecialServiceImpl.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.service.impl;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.log4j.Logger;

import org.cnoss.cms.entity.*;
import org.cnoss.cms.exception.*;
import org.cnoss.cms.service.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;


public class SpecialServiceImpl implements SpecialService {

    @Override
    public void createSpecial(Special special) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void editSpecial(Special special) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteSpecial(Long specialId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Special getSpecial(Long specialId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Special> getSpecials(String category, String keyWords, int pageIndex, int pageSize) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
