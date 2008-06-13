/*
 * @(#)ContentServiceImpl.java   08/06/13
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
import org.apache.log4j.jmx.LoggerDynamicMBean;

import org.cnoss.cms.entity.*;
import org.cnoss.cms.exception.*;
import org.cnoss.cms.service.*;

//~--- JDK imports ------------------------------------------------------------

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.UUID;


public class ContentServiceImpl implements ContentService {

    @Override
    public void createContent(Content content, int isPagination) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void editContent(Content content) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Content getContent(Long contentId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Content> getContents(Long categoryId, String keyword, int status, int isVouch, int pageIndex, int pageSize) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteContents(int[] ids) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Content deleteContent(Long id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateContent(Long oldCategoryId, Long newCategoryId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateContentVouch(Long contentId, Long isVouch) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Content> getContentsByCommend(Long categoryId, int dataNum) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Content> getContentsByWeekhot(Long categoryId, int dataNum) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Content> getContentsByNewly(Long categoryId, int dataNum) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Content> getContentsByMonthhot(Long categoryId, int dataNum) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateContentHits(Long contentId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
