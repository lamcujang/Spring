package com.dbiz.app.systemservice.domain;

public class AuditContext {
    private static final ThreadLocal<AuditInfo> auditInfo = new ThreadLocal<>();

    public static void setAuditInfo(AuditInfo info) {
        auditInfo.set(info);
    }

    public static AuditInfo getAuditInfo() {
        return auditInfo.get();
    }

    public static void clear() {
        auditInfo.remove();
    }
}
