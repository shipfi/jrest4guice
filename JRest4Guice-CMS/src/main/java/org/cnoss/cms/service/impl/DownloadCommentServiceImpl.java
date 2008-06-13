/*
 * @(#)DownloadCommentServiceImpl.java   08/06/13
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

import java.util.Date;
import java.util.List;
import java.util.List;
import java.util.Map;

public class DownloadCommentServiceImpl implements DownloadCommentService {

    @Override
    public void createDownloadComment(DownloadComment downloadComment) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DownloadComment> getComments(Long downloadId, String keyword, int pageIndex, int pageSize) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DownloadComment deleteDownloadComment(Long downloadId, Long commentId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
