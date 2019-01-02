/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.neusoft.framework.common.security.shiro.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.neusoft.framework.common.utils.JodisUtils;
import com.neusoft.framework.common.web.Servlets;

import redis.clients.jedis.Jedis;

/**
 * 自定义授权缓存管理类
 * @author ThinkGem
 * @version 2014-7-20
 */
public class JedisCacheManager implements CacheManager {

	private String cacheKeyPrefix = "shiro_cache_";
	
	private static final int CACHE_DATABASE = 1;
	
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new JedisCache<K, V>(cacheKeyPrefix + name);
	}

	public String getCacheKeyPrefix() {
		return cacheKeyPrefix;
	}

	public void setCacheKeyPrefix(String cacheKeyPrefix) {
		this.cacheKeyPrefix = cacheKeyPrefix;
	}
	
	/**
	 * 自定义授权缓存管理类
	 * @author ThinkGem
	 * @version 2014-7-20
	 */
	public class JedisCache<K, V> implements Cache<K, V> {

		private Logger logger = LoggerFactory.getLogger(getClass());

		private String cacheKeyName = null;

		public JedisCache(String cacheKeyName) {
			this.cacheKeyName = cacheKeyName;
//			if (!JodisUtils.exists(cacheKeyName)){
//				Map<String, Object> map = Maps.newHashMap();
//				JodisUtils.setObjectMap(cacheKeyName, map, 60 * 60 * 24);
//			}
//			logger.debug("Init: cacheKeyName {} ", cacheKeyName);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public V get(K key) throws CacheException {
			if (key == null){
				return null;
			}
			
			V v = null;
			HttpServletRequest request = Servlets.getRequest();
			if (request != null){
				v = (V)request.getAttribute(cacheKeyName);
				if (v != null){
					return v;
				}
			}
			
			V value = null;
			Jedis jedis = null;
			try {
				jedis = JodisUtils.getResource(CACHE_DATABASE);
				value = (V)JodisUtils.toObject(jedis.hget(JodisUtils.getBytesKey(cacheKeyName), JodisUtils.getBytesKey(key)));
				logger.debug("get {} {} {}", cacheKeyName, key, request != null ? request.getRequestURI() : "");
			} catch (Exception e) {
				logger.error("get {} {} {}", cacheKeyName, key, request != null ? request.getRequestURI() : "", e);
			} finally {
				if(jedis!=null){
					jedis.close();
				}
			}
			
			if (request != null && value != null){
				request.setAttribute(cacheKeyName, value);
			}
			
			return value;
		}

		@Override
		public V put(K key, V value) throws CacheException {
			if (key == null){
				return null;
			}
			
			Jedis jedis = null;
			try {
				jedis = JodisUtils.getResource(CACHE_DATABASE);
				jedis.hset(JodisUtils.getBytesKey(cacheKeyName), JodisUtils.getBytesKey(key), JodisUtils.toBytes(value));
				logger.debug("put {} {} = {}", cacheKeyName, key, value);
			} catch (Exception e) {
				logger.error("put {} {}", cacheKeyName, key, e);
			} finally {
				if(jedis!=null){
					jedis.close();
				}
			}
			return value;
		}

		@SuppressWarnings("unchecked")
		@Override
		public V remove(K key) throws CacheException {
			V value = null;
			Jedis jedis = null;
			try {
				jedis = JodisUtils.getResource(CACHE_DATABASE);
				value = (V)JodisUtils.toObject(jedis.hget(JodisUtils.getBytesKey(cacheKeyName), JodisUtils.getBytesKey(key)));
				jedis.hdel(JodisUtils.getBytesKey(cacheKeyName), JodisUtils.getBytesKey(key));
				logger.debug("remove {} {}", cacheKeyName, key);
			} catch (Exception e) {
				logger.warn("remove {} {}", cacheKeyName, key, e);
			} finally {
				if(jedis!=null){
					jedis.close();
				}
			}
			return value;
		}

		@Override
		public void clear() throws CacheException {
			Jedis jedis = null;
			try {
				jedis = JodisUtils.getResource(CACHE_DATABASE);
				jedis.hdel(JodisUtils.getBytesKey(cacheKeyName));
				logger.debug("clear {}", cacheKeyName);
			} catch (Exception e) {
				logger.error("clear {}", cacheKeyName, e);
			} finally {
				if(jedis!=null){
					jedis.close();
				}
			}
		}

		@Override
		public int size() {
			int size = 0;
			Jedis jedis = null;
			try {
				jedis = JodisUtils.getResource(CACHE_DATABASE);
				size = jedis.hlen(JodisUtils.getBytesKey(cacheKeyName)).intValue();
				logger.debug("size {} {} ", cacheKeyName, size);
				return size;
			} catch (Exception e) {
				logger.error("clear {}",  cacheKeyName, e);
			} finally {
				if(jedis!=null){
					jedis.close();
				}
			}
			return size;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Set<K> keys() {
			Set<K> keys = Sets.newHashSet();
			Jedis jedis = null;
			try {
				jedis = JodisUtils.getResource(CACHE_DATABASE);
				Set<byte[]> set = jedis.hkeys(JodisUtils.getBytesKey(cacheKeyName));
				for(byte[] key : set){
					keys.add((K)key);
	        	}
				logger.debug("keys {} {} ", cacheKeyName, keys);
				return keys;
			} catch (Exception e) {
				logger.error("keys {}", cacheKeyName, e);
			} finally {
				if(jedis!=null){
					jedis.close();
				}
			}
			return keys;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Collection<V> values() {
			Collection<V> vals = Collections.emptyList();;
			Jedis jedis = null;
			try {
				jedis = JodisUtils.getResource(CACHE_DATABASE);
				Collection<byte[]> col = jedis.hvals(JodisUtils.getBytesKey(cacheKeyName));
				for(byte[] val : col){
					vals.add((V)val);
	        	}
				logger.debug("values {} {} ", cacheKeyName, vals);
				return vals;
			} catch (Exception e) {
				logger.error("values {}",  cacheKeyName, e);
			} finally {
				if(jedis!=null){
					jedis.close();
				}
			}
			return vals;
		}
	}
}
