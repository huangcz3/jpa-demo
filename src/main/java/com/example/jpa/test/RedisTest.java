package com.example.jpa.test;

import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * @author Huangcz
 * @date 2019-01-09 12:01
 * @desc xxx
 */
public class RedisTest {

	private static final int ONE_WEEK_IN_SECONDS = 7 * 86400;

	private static final int VOTE_SCORE = 432;

	private static final int ARTICLES_PER_PAGE = 25;

	/**
	 * 文章Key前缀，例： article:111
	 */
	private static final String ARTICLE_KEY_PREFIX = "article:";
	/**
	 * 投票Key前缀，例： voted:111
	 */
	private static final String VOTED_KEY_PREFIX = "voted:";
	/**
	 * 发布时间Key
	 */
	private static final String TIME_KEY = "time:";
	/**
	 * 评分Key
	 */
	private static final String SCORE_KEY = "score:";
	/**
	 * 分组key前缀，例： group:program
	 */
	private static final String GROUP_KEY_PREFIX = "group:";


	public static void main(String[] args) {
		new RedisTest().run();
	}

	public void run() {

		Jedis conn = new Jedis("localhost");

		String articleId = postArticle(conn, "huangcz", "A title", "http://www.baidu.com");
		System.out.println("We posted a new article with id: " + articleId);
		System.out.println("Its HASH looks like:");

		Map<String, String> articleMapData = conn.hgetAll(ARTICLE_KEY_PREFIX + articleId);
		articleMapData.forEach((k, v) -> System.out.println(" " + k + ": " + v));

	}

	/**
	 * 文章发布
	 *
	 * @param conn
	 * @param user
	 * @param title
	 * @param link
	 * @return
	 */
	public String postArticle(Jedis conn, String user, String title, String link) {
		String articleId = String.valueOf(conn.incr(ARTICLE_KEY_PREFIX));

		String voted = VOTED_KEY_PREFIX + articleId;
		// 投票人
		conn.sadd(voted, user);
		conn.expire(voted, ONE_WEEK_IN_SECONDS);

		long now = System.currentTimeMillis() / 1000;
		String article = ARTICLE_KEY_PREFIX + articleId;
		Map<String, String> articleDataMap = new HashMap<>();
		articleDataMap.put("title", title);
		articleDataMap.put("link", link);
		articleDataMap.put("user", user);
		articleDataMap.put("now", String.valueOf(now));
		articleDataMap.put("votes", "1");
		// 文章内容等信息
		conn.hmset(article, articleDataMap);
		// 评分
		conn.zadd(SCORE_KEY, now + VOTE_SCORE, article);
		// 发布时间
		conn.zadd(TIME_KEY, now, article);

		return articleId;
	}

	/**
	 * 文章投票
	 *
	 * @param conn
	 * @param user
	 * @param article
	 */
	public void articleVote(Jedis conn, String user, String article) {

		long cutoff = (System.currentTimeMillis() / 1000) - ONE_WEEK_IN_SECONDS;
		if (conn.zscore(TIME_KEY, article) < cutoff) {
			return;
		}

		String articleId = article.substring(article.indexOf(":") + 1);

		if (conn.sadd(VOTED_KEY_PREFIX + articleId, user) == 1) {
			conn.zincrby(SCORE_KEY, VOTE_SCORE, article);
			conn.hincrBy(article, "votes", 1);
		}
	}

	public List<Map<String, String>> getArticles(Jedis conn, int page) {
		return getArticles(conn, page, SCORE_KEY);
	}

	/**
	 * 获取文章
	 *
	 * @param conn
	 * @param page
	 * @param order
	 * @return
	 */
	public List<Map<String, String>> getArticles(Jedis conn, int page, String order) {
		int start = (page - 1) * ARTICLES_PER_PAGE;
		int end = start + ARTICLES_PER_PAGE - 1;

		Set<String> ids = conn.zrevrange(order, start, end);

		List<Map<String, String>> articles = new ArrayList<>();


		for (String id : ids) {
			Map<String, String> articleData = conn.hgetAll(id);
			articleData.put("id", id);
			articles.add(articleData);
		}
		return articles;
	}

	/**
	 * 对文章进行分组
	 *
	 * @param conn
	 * @param articleId
	 * @param toAdd
	 * @param toRemove
	 */
	public void addAndRemoveGroups(Jedis conn, String articleId, String[] toAdd, String[] toRemove) {

		for (String addStr : toAdd) {
			conn.sadd(GROUP_KEY_PREFIX + addStr, ARTICLE_KEY_PREFIX + articleId);
		}
		for (String removeStr : toRemove) {
			conn.srem(GROUP_KEY_PREFIX + removeStr, ARTICLE_KEY_PREFIX + articleId);
		}

	}

}
