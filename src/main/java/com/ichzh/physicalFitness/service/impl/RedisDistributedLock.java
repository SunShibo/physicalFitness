package com.ichzh.physicalFitness.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.service.RedisLockService;

/**
 * 分布式锁
 *
 */
@Service("redisDistributedLock")
@Slf4j
public class RedisDistributedLock implements RedisLockService{

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public Integer lock(String key, final int retry, long expireSecs, long sleepMills) {
		log.info("RedisDistributedLock.lock.key:{}, retry:{}, expireSecs:{}, sleepMills:{}", key, retry, expireSecs, sleepMills);
		RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        try{
	        int retryCount = 0;
        	while (!connection.setNX(key.getBytes(), "".getBytes())) {
        		if(retry > 0){
        			if (retryCount > retry) {
        				log.error("RedisDistributedLock.lock.key:{}, retry:{}, retryCount:{}", key, retry, retryCount);
        				return 0;
        			} else {
        				retryCount++;
        				Thread.sleep(sleepMills);
        				log.info("RedisDistributedLock.lock.key:{}, retry:{}, sleepCount:{}", key, retry, retryCount);
        			}
    	        } else {
    	        	log.error("RedisDistributedLock. get lock failed, lock.key:{}, retry:{}, retryCount:{}", key, retry, retryCount);
    	        	return 0;
    	        }
        	}
        	connection.expire(key.getBytes(), expireSecs);
        	log.info("RedisDistributedLock. get lock succss.key:{}, retry:{}, sleepCount:{}", key, retry, retryCount);
        	return 1;
        } catch (InterruptedException e){
        	log.error("RedisDistributedLock.lock:{}, e:{}", key, ExceptionUtils.getStackTrace(e));
        	connection.del(key.getBytes());
        	return 0;
        }finally{
    		RedisConnectionUtils.releaseConnection(connection, redisTemplate.getConnectionFactory());
    		log.info("RedisDistributedLock.lock release connection success");
        }
	}

	@Override
	public void unlock(String key) {
		RedisConnection connection = null;
		try {
			connection = redisTemplate.getConnectionFactory().getConnection();
			connection.del(key.getBytes());
			log.info("unlock.key:{} success", key);
		} catch (Exception e) {
			log.error("RedisDistributedLock.unlock:{}, e:{}", key, ExceptionUtils.getStackTrace(e));
			if (connection != null) {
				connection.del(key.getBytes());
			}
		} finally {
			if (connection != null) {
				RedisConnectionUtils.releaseConnection(connection, redisTemplate.getConnectionFactory());
				log.info("RedisDistributedLock.unlock release connection success");
			}
		}
	}
	
}
