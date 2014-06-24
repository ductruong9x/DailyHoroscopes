package com.gamefree.dailyhoroscopes.common;

public class Constants {

	public static final String FANPAGE_KEY = "340505782696997";
	public static final String FANPAGE_URL = "http://graph.facebook.com/306103412789329";
	public static final String FANPAGE_URL_2 = "http://graph.facebook.com/149661191773624";
	public static String QUERY_INFO = "SELECT post_id, actor_id,created_time,description FROM stream WHERE filter_key in (SELECT filter_key FROM stream_filter WHERE uid =340505782696997 AND type = 'newsfeed') LIMIT ";
	public static final String APP_ID="658268967573220";
	public static final String SERCEP_KEY="90b0d7bbfee83dbf9c1240228544b363";
}
