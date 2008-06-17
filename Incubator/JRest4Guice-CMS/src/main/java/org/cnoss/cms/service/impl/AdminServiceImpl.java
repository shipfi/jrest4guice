/*
 * @(#)AdminServiceImpl.java   08/06/13
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

public class AdminServiceImpl implements AdminService {
    public static Logger logger = Logger.getLogger(AdminServiceImpl.class);

    @Override
    public void createAdmin(Admin admin) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void editAdminPassword(Long adminId, String oldPassword, String password) throws AdminException, ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void editAdmin(Admin admin) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Admin getAdmin(Long adminId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Admin getAdmin(String username) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Admin> getAdmins(int state) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Admin authLogin(String username, String password) throws LoginException, ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateLoginInfo(Long adminId, String lastLoginTime, String lastLoginIp) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateAdminState(Long adminId, int state) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateAdminPassword(Long adminId, String oldPassword, String newPasword) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

  
}
