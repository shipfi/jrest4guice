/*
 * @(#)RoleServiceImpl.java   08/06/13
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
import org.apache.log4j.Logger;

import org.cnoss.cms.entity.*;
import org.cnoss.cms.exception.*;
import org.cnoss.cms.service.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.List;


public class RoleServiceImpl implements RoleService {
    private static Logger logger = Logger.getLogger(RoleServiceImpl.class);

    @Override
    public void createRole(Role role) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void editRole(Role role) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteRole(Long roleId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Role getRole(Long roleId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Role> getRoles() throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
  
}
