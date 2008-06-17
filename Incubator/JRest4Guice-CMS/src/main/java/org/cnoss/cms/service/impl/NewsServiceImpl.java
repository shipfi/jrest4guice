package org.cnoss.cms.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import java.util.List;

import org.apache.log4j.Logger;
import org.cnoss.cms.entity.*;
import org.cnoss.cms.service.*;
import org.cnoss.cms.exception.*;
/**
 * 新闻模块业务处理接口
 * 
 */
public class NewsServiceImpl implements NewsService {
public static Logger logger = Logger.getLogger(NewsServiceImpl.class);

    @Override
    public void createNews(News news) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void editNews(News news) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<News> getNewss(int state) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public News getNews(Long newsId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public News deleteNews(Long id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
	
	

}
