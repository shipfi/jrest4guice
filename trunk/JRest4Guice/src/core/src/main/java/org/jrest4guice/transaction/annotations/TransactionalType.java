package org.jrest4guice.transaction.annotations;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public enum TransactionalType {
    REQUIRED,
    REQUIRESNEW,
    SUPPORTS,
    NOTSUPPORTED,
    NEVER,
    READOLNY
}
