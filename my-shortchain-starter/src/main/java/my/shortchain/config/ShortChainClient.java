package my.shortchain.config;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.IdUtil;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import my.shortchain.ShortId;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

@Slf4j
public class ShortChainClient {
    private ShortChainProperties shortChainProperties;
    private BloomFilter<String> bloomFilter;

    public ShortChainClient(ShortChainProperties shortChainProperties) {
        this.shortChainProperties = shortChainProperties;
        this.bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf-8")), 1000);
        createCacheFile();
    }

    /**
     * 随机生成唯一的短数据
     */
    public String generateShortChain() {
        //暂且一雪花id做随机
        long random = IdUtil.getSnowflake().nextId();
        //得到随机生成的随机短号
        String shortId = ShortId.generateUnionDeviceId(random);
        //判断生成的短号是否存在，如果命中说明之前使用过，那么重新生成
        while (isExistence(shortId)) {
            generateShortChain();
        }
        //没有命中则没有被使用过
        putCache(shortId);
        return shortId;
    }

    /**
     * 将指定数据生成为唯一的短链
     *
     * @param targetChain
     * @return
     */
    public String generateShortChain(long targetChain) {
        //得到随机生成的随机短号
        String shortId = ShortId.generateUnionDeviceId(targetChain);
        //判断生成的短号是否存在
        boolean existence = isExistence(shortId);
        if (existence) {
            Assert.isTrue(existence, "目标数据生成的短号已存在,请重新调用");
        }
        //没有命中则没有被使用过
        putCache(shortId);
        return shortId;
    }

    /**
     * 判断是否存在
     *
     * @return
     */
    private synchronized boolean isExistence(String shortId) {
        return bloomFilter.mightContain(shortId);
    }

    /**
     * 存入缓存
     *
     * @param shortId
     */
    private synchronized void putCache(String shortId) {
        bloomFilter.put(shortId);
        //存入本地缓存
        String localCacheFile = shortChainProperties.getLocalCacheFile();
        String path = this.getClass().getClassLoader().getResource(localCacheFile).getPath();
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.appendLines(Collections.singletonList(shortId));
    }

    /**
     * 生成缓存文件
     */
    private void createCacheFile() {
        String localCacheFile = shortChainProperties.getLocalCacheFile();
        ClassPathResource classPathResource = new ClassPathResource(localCacheFile);
        File file = new File(classPathResource.getPath());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                Assert.hasText(localCacheFile, "短号缓存文件生成失败: " + ioException);
            }
        }
    }
}

