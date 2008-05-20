package org.jrest4guice.core.transaction.annotations;

public enum TransactionalType {
    REQUIRED,
    REQUIRESNEW,
    SUPPORTS,
    NOTSUPPORTED,
    NEVER,
    READOLNY
}
