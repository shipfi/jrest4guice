/*
 * @(#)ContentCommentServiceImpl.java   08/06/13
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
import java.util.Map;


public class ContentCommentServiceImpl implements ContentCommentService {

    @Override
    public void createContentComment(ContentComment contentComment) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ContentComment> getComments(Long contentId, String keyword, int pageIndex, int pageSize) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ContentComment deleteContentComment(Long contentId, Long commentId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
