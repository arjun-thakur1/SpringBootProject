package work1.project1.package1.configuration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import work1.project1.package1.dto.response.DepartmentResponse;

import java.util.List;

@EnableCaching
@Configuration
public class RedisConfiguration {


    @Bean
    JedisConnectionFactory jedisConnectionFactory() {

        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public JedisPool getJedisPool() {
        JedisPool jedisPool = new JedisPool(getPoolConfig(),host,port );
        return jedisPool;
    }
    private GenericObjectPoolConfig getPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();;
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxWaitMillis(2);
        return jedisPoolConfig;
    }
  /*  private GenericObjectPoolConfig getPoolConfig() {
        return new GenericObjectPoolConfig();
    }
   */
}
