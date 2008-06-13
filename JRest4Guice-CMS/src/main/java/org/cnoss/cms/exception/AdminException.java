/*
 * @(#)AdminException.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.exception;

/**
 * 业务处理异常
 *
 */
public class AdminException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 2416474607404813186L;

    public AdminException(String arg0) {
        super(arg0);
    }
}
