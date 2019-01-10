package com.example.jpa.test;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Huangcz
 * @date 2019-01-10 14:49
 * @desc xxx
 */
public class Chapter02 {

	private static final boolean EXCUTE_FLAG = true;

	private static final long MAX_LIMIT = 10000;

	public void testLoginCookies(Jedis conn) {

	}


	public String checkToken(Jedis conn, String token) {
		return conn.hget("login:", token);
	}


	public void updateToken(Jedis conn, String token, String user, String item) {
		long now = System.currentTimeMillis() / 1000;
		conn.hset("login:", token, user);
		conn.zadd("recent:", now, token);

		if (item != null) {
			// 记录用户浏览过的商品
			conn.zadd("viewed:" + token, now, item);
			// 移除旧的记录，只保留用户最近浏览过的25个商品
			conn.zremrangeByRank("viewed:" + token, 0, -26);
		}
	}


	public void cleanSession(Jedis conn) throws InterruptedException {
		while (EXCUTE_FLAG) {
			long size = conn.zcard("recent:");
			if (size <= MAX_LIMIT) {
				TimeUnit.SECONDS.sleep(1);
				continue;
			}
			long endIndex = Math.min(size - MAX_LIMIT, 100);
			Set<String> tokenSet = conn.zrange("recent:", 0, endIndex - 1);
			String[] tokens = tokenSet.toArray(new String[tokenSet.size()]);
			List<String> sessionKeys = new ArrayList<>();

			for (String token : tokenSet) {
				sessionKeys.add("viewed:" + token);
			}


			conn.del(sessionKeys.toArray(new String[sessionKeys.size()]));
			conn.hdel("login:", tokens);
			conn.zrem("recent:", tokens);

		}
	}


}
