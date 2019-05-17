package com.fooapp.cart.conf.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static net.logstash.logback.argument.StructuredArguments.value;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Value("${redis.server:localhost}")
    private String redisServer;

    @Value("${redis.port:6379}")
    private Integer redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("[starting] RedisConfiguration.jedisConnectionFactory() for redis: {}:{}"
                ,value("redisHost",redisServer)
                ,value("redisPort", redisPort));

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisServer, redisPort);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

}
