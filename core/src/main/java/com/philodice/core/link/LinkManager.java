package com.philodice.core.link;

import com.philodice.core.Entity.LinkEntity;
import com.philodice.core.redis.RedisService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class LinkManager {

    private final LinkService linkService;

    private final SeqPoolManager seqPoolManager;

    private final LinkEntity linkEntity;

    private final RedisService redisService;

    public LinkManager(LinkService linkService, SeqPoolManager seqPoolManager,
                       LinkEntity linkEntity, RedisService redisService) {
        this.linkService = linkService;
        this.seqPoolManager = seqPoolManager;
        this.linkEntity = linkEntity;
        this.redisService = redisService;
    }

    /**
     * 检查长链接映射表中的长链接是否存在
     * @param source 长链接
     * @return 存在则返回对应的短链接，否则返回 null
     */
    public String checkSourceExists(String source) {
        return linkService.checkSourceExists(source);
    }

    /**
     * 检查短链接映射表中的短链接是否存在
     * @param link 短链接
     * @return 存在则返回对应的长链接，否则返回 null
     */
    public String checkLinkExists(String link) {
        return linkService.checkLinkExists(link);
    }

    /**
     * 生成短链接
     * @param source 长链接
     * @return 短链接
     */
    public String createLinkCode(String source) {
        // 取出序列号
        String seq = String.valueOf(seqPoolManager.getSeq());
        // 创建短链接
        String link = linkService.createLinkCode(seq);

        // 添加到 redis 短链接映射
        redisService.hashSet(linkEntity.getLinksPrefix(), link, source);
        // 添加到 redis 长连接映射
        redisService.hashSet(linkEntity.getLinksRevPrefix(), source, link);
        // 初始化点击数
        redisService.hashSet(linkEntity.getClickCounts(), link, 0L);

        // TODO 持久化到数据库

        return link;
    }

    /**
     * 增加短链接的点击次数
     * @param link 短链接
     */
    public void updateLickIncrement(String link) {
        linkService.clickIncrement(link);
    }

    /**
     * 更新短链接最后访问时间
     * @param link 短链接
     */
    public void updateLastAccess(String link) {
        // 获取当前时间戳
        Instant now = Instant.now();
        // long currentTimestamp = now.toEpochMilli();
        // 将时间戳转换为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(now.atZone(ZoneId.systemDefault()));

        linkService.lastAccess(link, formattedDate);
    }
}
