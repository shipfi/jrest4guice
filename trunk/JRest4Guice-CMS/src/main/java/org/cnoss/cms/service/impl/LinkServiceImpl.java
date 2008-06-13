/*
 * @(#)LinkServiceImpl.java   08/06/13
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

public class LinkServiceImpl implements LinkService {
    private static Logger logger = Logger.getLogger(LinkServiceImpl.class);

    @Override
    public void createLink(Link link) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void editLink(Link link) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Link getLink(Long id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Link> getLinks(int isLogo, int columnId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Link deleteLink(Long id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
