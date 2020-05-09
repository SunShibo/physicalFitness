package com.ichzh.physicalFitness.service;

public interface RedisLockService {

	/**
	 * 加锁
	 * @param key key
	 * @param retry 加锁重试次数
	 * @param expireSecs 失效时间,单位：秒
	 * @param sleepMills 每次重试等待时间,单位：毫秒
	 * @return //是否获得锁 0:否 1：是
	 */
	public Integer lock(String key,final int retry, long expireSecs, long sleepMills);
	
	//解锁
	public void unlock(String key);
}
