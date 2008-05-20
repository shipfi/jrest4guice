package org.cnoss.core.transaction.annotations;

public enum TransactionalType {
    REQUIRED,
    REQUIRESNEW,
    SUPPORTS,
    NOTSUPPORTED,
    NEVER,
    READOLNY
}
