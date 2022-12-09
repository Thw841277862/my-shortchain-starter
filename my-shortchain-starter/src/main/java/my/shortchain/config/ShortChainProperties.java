package my.shortchain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "short-client")
@Data
public class ShortChainProperties {
    /**
     * 默认使用本地文件缓存存储已使用的数据
     * 0 表示本地文件缓存
     * 1 表示redis缓存
     */
    private int defaultCache = 0;
    /**
     * 本地缓存时用于存储已使用的短链信息
     */
    private String localCacheFile = "short-cache.txt";
}
