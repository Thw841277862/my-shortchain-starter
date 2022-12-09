package my.shortchain.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ShortChainProperties.class)
public class ShortChainConfig {
    @Bean
    public ShortChainClient shortChainClient(ShortChainProperties shortChainProperties) {
        return new ShortChainClient(shortChainProperties);
    }
}
