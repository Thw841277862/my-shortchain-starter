package my.shortchain.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Component
public class ShortChainRunner implements ApplicationRunner {
    @Autowired ShortChainProperties shortChainProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createCacheFile();
        //将缓存文件里面的数据 读取出来存入到布隆过滤器中
        FileReader fileReader = new FileReader(shortChainProperties.getLocalCacheFile());
//        fileReader.re
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
