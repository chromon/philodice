package com.philodice.core.Entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * redis 数据库相关 key 值实体类
 */
@Component
public class LinkEntity {

    /**
     * 短链接映射表，短链接到长链接的映射
     */
    @Value("${links.prefix}")
    private String linksPrefix;

    /**
     * 长链接映射表，长链接到短链接的映射
     */
    @Value("${links.rev.prefix}")
    private String linksRevPrefix;

    /**
     * 链接最后访问时间
     */
    @Value("${last.access}")
    private String lastAccess;

    /**
     * 点击统计，短链接的点击量
     */
    @Value("${click.counts}")
    private String clickCounts;

    public String getLinksPrefix() {
        return linksPrefix;
    }

    public void setLinksPrefix(String linksPrefix) {
        this.linksPrefix = linksPrefix;
    }

    public String getLinksRevPrefix() {
        return linksRevPrefix;
    }

    public void setLinksRevPrefix(String linksRevPrefix) {
        this.linksRevPrefix = linksRevPrefix;
    }

    public String getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(String lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getClickCounts() {
        return clickCounts;
    }

    public void setClickCounts(String clickCounts) {
        this.clickCounts = clickCounts;
    }
}
