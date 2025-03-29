package com.alibou.security.config.cache;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Log4j2
@Configuration
public class RedisConfig {

    @Value("${redis.standalone.host}")
    private String redisHost;

    @Value("${redis.standalone.port}")
    private int redisPort;

    @Value("${redis.standalone.database}")
    private int redisDatabase;

    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost); // Cấu hình host
        configuration.setPort(redisPort);     // Cấu hình port
        configuration.setDatabase(redisDatabase); // Chọn database (mặc định là 0)
        log.info("Connecting to Redis at {}:{}", redisHost, redisPort);
        return new JedisConnectionFactory(configuration); // Kết nối Redis standalone
    }

    @Primary
    @Bean(value = "redisTemplate")
    public RedisTemplate<String, String> jedisTemplate() {
        RedisTemplate<String, String> jedisTemplate = new RedisTemplate<>();
        jedisTemplate.setConnectionFactory(jedisConnectionFactory());
        jedisTemplate.setKeySerializer(new StringRedisSerializer());
        jedisTemplate.setValueSerializer(new StringRedisSerializer());
        jedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        jedisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return jedisTemplate;
    }
}
