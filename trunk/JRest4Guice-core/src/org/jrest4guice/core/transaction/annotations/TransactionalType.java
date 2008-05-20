package org.jrest4guice.core.transaction.annotations;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
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
