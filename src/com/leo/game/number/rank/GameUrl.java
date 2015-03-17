package com.leo.game.number.rank;

import com.leo.game.number.utils.DConfig;

public class GameUrl {
	// public static final String host_Debug =
	// "http://10.236.127.20:8081/index.php?";
	public static final String host_Debug = "http://api.d.n.xiaomi.com/index.php?";
	private static final String host_Public = "https://api.d.xiaomi.com/index.php?";

	public static final String geturl_updateScore() {
		String host = DConfig.DEBUG ? host_Debug : host_Public;
		return host + "action=game&do=game2048_score";
	}

	public static final String getUrl_getRank() {
		String host = DConfig.DEBUG ? host_Debug : host_Public;
		return host + "action=game&do=game2048_rank";
	}
}
