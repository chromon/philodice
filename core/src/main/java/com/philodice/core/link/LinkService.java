package com.philodice.core.link;

import com.philodice.core.Entity.LinkEntity;
import com.philodice.core.redis.RedisService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 链接处理 service
 */
@Service
public class LinkService {

    private final LinkEntity linkEntity;

    private final RedisService redisService;

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final int MAX_SHORT_CODE_LENGTH = 6;

    public LinkService(LinkEntity linkEntity, RedisService redisService) {
        this.linkEntity = linkEntity;
        this.redisService = redisService;
    }

    /**
     * 检查长链接映射表中的长链接是否存在
     * @param source 长链接
     * @return 存在则返回对应的短链接，否则返回 null
     */
    public String checkSourceExists(String source) {
        Object obj = redisService.hashGet(linkEntity.getLinksRevPrefix(), source);
        return obj != null? (String) obj: null;
    }

    /**
     * 检查短链接映射表中的短链接是否存在
     * @param link 短链接
     * @return 存在则返回对应的长链接，否则返回 null
     */
    public String checkLinkExists(String link) {
        Object obj = redisService.hashGet(linkEntity.getLinksPrefix(), link);
        return obj != null? (String) obj: null;
    }

    /**
     * 生成短链接
     * @param id 序列号
     * @return 短链接
     */
    public String createLinkCode(String id) {
        String hashValue = this.getSHA256Hash(id);

        // 截取哈希值的长度，截取哈希值的前 14 个字符
        int truncatedLength = 14;
        String truncatedHash = hashValue.substring(0, truncatedLength);
        // 将截取的哈希子串视为 16 进制数
        long decimalValue = Long.parseLong(truncatedHash, 16);
        long range = (long) Math.pow(62, MAX_SHORT_CODE_LENGTH);
        // 映射到短码长度范围内
        long mappedValue = decimalValue % range;

        // 使用 Base62 编码将映射后的数值转换为字符串
        StringBuilder sb = new StringBuilder();
        while (mappedValue > 0) {
            sb.append(CHARS.charAt((int) (mappedValue % 62)));
            mappedValue /= 62;
        }
        return sb.reverse().toString();
    }

    /**
     * 将序列号 id 进行 SHA256 哈希
     * @param id 待哈希序列号
     * @return 序列号的哈希值
     */
    private String getSHA256Hash(String id) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(id.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error calculating SHA-256 hash", e);
        }
    }

    /**
     * 将序列号 id 进行 md5 哈希
     * @param id 待哈希序列号
     * @return 序列号的哈希值
     */
    private String getMD5Hash(String id) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(id.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error calculating MD5 hash", e);
        }
    }

    /**
     * 增加短链接的点击次数
     * @param link 短链接
     */
    public void clickIncrement(String link) {
        redisService.hashIncreby(linkEntity.getClickCounts(), link, 1);
    }

    /**
     * 更新短链接最后访问时间
     * @param link 短链接
     * @param timestamp 格式化后的时间戳字符串
     */
    public void lastAccess(String link, String timestamp) {
        redisService.hashSet(linkEntity.getLastAccess(), link, timestamp);
    }
}
