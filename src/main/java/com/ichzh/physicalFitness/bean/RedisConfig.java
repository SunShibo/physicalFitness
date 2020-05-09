package com.ichzh.physicalFitness.bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.ichzh.physicalFitness.domain.RedisObjectSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * redis配置
 */
@Configuration
public class RedisConfig {
	
	@Value("${spring.redis.host}")
	private String host;
	
	@Value("${spring.redis.password}")
	private String password;
	
	@Value("${spring.redis.port}")
	private int port;
	
	@Value("${spring.redis.pool.max-idle}")
	private int max_idle;
	
	@Value("${spring.redis.pool.min-idle}")
	private int min_idle;
	
	@Value("${spring.redis.pool.max-wait}")
	private int max_wait;
	
	@Value("${spring.redis.pool.max-active}")
	private int max_active;
	
	@Value("${spring.redis.pool.testOnBorrow}")
	private boolean testOnBorrow;
	
	@Value("${spring.redis.sentinel.master}")
	private String master;
	
	@Value("${spring.redis.sentinel.nodes}")
	private String nodes;
	

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
//        RedisSentinelConfiguration redisSentinelConfig = new RedisSentinelConfiguration();
//        redisSentinelConfig.setMaster(master);
//        if(nodes!=null && nodes.length() >0){
//        	String[] nodeArr = nodes.split(",");
//        	for(String node : nodeArr){
//        		String[] nodeIpAndPort = node.split(":");
//        		redisSentinelConfig.sentinel(nodeIpAndPort[0], Integer.parseInt(nodeIpAndPort[1]));
//        	}
//        }
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(max_idle);
//        jedisPoolConfig.setMaxWaitMillis(max_wait);
//        jedisPoolConfig.setMinIdle(min_idle);
//        jedisPoolConfig.setMaxTotal(max_active);
//        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
//        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(redisSentinelConfig,jedisPoolConfig);
//        redisConnectionFactory.setPassword(password);
//        return redisConnectionFactory;
//        单点配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(max_idle);
        jedisPoolConfig.setMaxWaitMillis(max_wait);
        jedisPoolConfig.setMinIdle(min_idle);
        jedisPoolConfig.setMaxTotal(max_active);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
//        jedisPoolConfig.setEvictionPolicyClassName(evictionPolicyClassName);
//        jedisPoolConfig.setTestOnReturn(true);
//        jedisPoolConfig.setTestWhileIdle(true);//空闲时进行连接扫描
//        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);//表示idle object evitor两次扫描之间要sleep的毫秒数
//        jedisPoolConfig.setNumTestsPerEvictionRun(10);//表示idle object evitor每次扫描的最多的对象数
//        jedisPoolConfig.setMinEvictableIdleTimeMillis(60000);//表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        redisConnectionFactory.setPassword(password);
        redisConnectionFactory.setHostName(host);
        redisConnectionFactory.setPort(port);
        return redisConnectionFactory;  
    }
    
    @Bean
    public StringRedisSerializer stringRedisSerializer(){
    	return new StringRedisSerializer();
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(stringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }


}
