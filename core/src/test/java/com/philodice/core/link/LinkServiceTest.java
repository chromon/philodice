package com.philodice.core.link;

import com.philodice.core.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class LinkServiceTest {

    private final LinkService linkService;

    private final RedisService redisService;

    private static final String BASE62_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public LinkServiceTest(LinkService linkService, RedisService redisService) {
        this.linkService = linkService;
        this.redisService = redisService;
    }

    @Test
    public void testCheckLinkExists() {
        String source = "https://www.baidu.com";

        redisService.hashSet("links_rev", source, "abcd");
        String s = linkService.checkSourceExists(source);
        if (s != null) {
            System.out.println(s);
        } else {
            System.out.println("link not found");
        }
    }

    public String generateShortCode(String input) {
        int SHORT_CODE_LENGTH = 6;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger hashNumber = new BigInteger(1, hashBytes);
            String hashString = hashNumber.toString(16);

            // Convert hexadecimal hash string to decimal
            BigInteger decimalHash = new BigInteger(hashString, 16);

            // Convert decimal hash to base62
            StringBuilder shortCode = new StringBuilder();
            while (decimalHash.compareTo(BigInteger.ZERO) > 0) {
                BigInteger remainder = decimalHash.mod(BigInteger.valueOf(62));
                shortCode.insert(0, BASE62_CHARACTERS.charAt(remainder.intValue()));
                decimalHash = decimalHash.divide(BigInteger.valueOf(62));
            }

            // Pad the short code with leading zeros if necessary
            int shortCodePaddingLength = SHORT_CODE_LENGTH - shortCode.length();
            for (int i = 0; i < shortCodePaddingLength; i++) {
                shortCode.insert(0, BASE62_CHARACTERS.charAt(0));
            }

            return shortCode.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Test
    public void testHash() {
        String input = "919377199809437699";
        String shortCode = generateShortCode(input);
        System.out.println("Short Code: " + shortCode);
    }

    @Test
    public void testTimestamp() {
        // 获取当前时间戳
        Instant now = Instant.now();
        long currentTimestamp = now.toEpochMilli();
        System.out.println(currentTimestamp);

        // 将时间戳转换为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(now.atZone(ZoneId.systemDefault()));
        System.out.println(formattedDate);
    }
}