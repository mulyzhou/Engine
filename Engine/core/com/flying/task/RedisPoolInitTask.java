package com.flying.task;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;

public class RedisPoolInitTask implements ITask {
	
	private static Log log = LogFactory.getLog(RedisPoolInitTask.class);//日志
	
	@Override
	public void execute() {
		InputStream in = null;// 文件输入流
		Properties pp = new Properties();// 数据库属性

		try{
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/redis.properties");
			pp.load(in);// 将输入流编程属性文件
		}catch(IOException e){
			log.error("redis.properties 配置文件有错误！",e);
		}
		
		//初始化redis线程池
		initialPool(pp); 
        initialShardedPool(pp);
	}
	
	/**
     * 初始化非切片池
     */
    private void initialPool(Hashtable ht) 
    { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxIdle(ht.get("maxIdle") == null ? 5 : Integer.parseInt(ht.get("maxIdle").toString())); 
        config.setMaxWaitMillis(ht.get("maxWaitMillis") == null ? 100 : Integer.parseInt(ht.get("maxWaitMillis").toString())); 
        config.setTestOnBorrow(ht.get("testOnBorrow") == null ? false : Boolean.parseBoolean(ht.get("testOnBorrow").toString())); 

        StaticVariable.JEDIS_POOL = new JedisPool(config,ht.get("ip") == null ? "" : ht.get("ip").toString(),ht.get("port") == null ? 0 : Integer.parseInt(ht.get("port").toString()));
    }
    
    /** 
     * 初始化切片池 
     */ 
    private void initialShardedPool(Hashtable ht) 
    { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxIdle(ht.get("maxIdle") == null ? 5 : Integer.parseInt(ht.get("maxIdle").toString())); 
        config.setMaxWaitMillis(ht.get("maxWaitMillis") == null ? 100 : Integer.parseInt(ht.get("maxWaitMillis").toString())); 
        config.setTestOnBorrow(ht.get("testOnBorrow") == null ? false : Boolean.parseBoolean(ht.get("testOnBorrow").toString())); 
        // slave链接 
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
        
        shards.add(new JedisShardInfo(ht.get("ip") == null ? "" : ht.get("ip").toString(),ht.get("port") == null ? 0 : Integer.parseInt(ht.get("port").toString()), "master")); 
        // 构造池 
        StaticVariable.SHARDED_JEDIS_POOL = new ShardedJedisPool(config, shards); 
    }
}
