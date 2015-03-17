package com.leo.game.number;

import java.io.Serializable;

public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1619006544489730814L;
	private String plugin_id;
	private String id;
	private String title;
	private String userCookie;
	private String imei;
	private String bestScore;

	public String getPlugin_id() {
		return plugin_id;
	}

	public void setPlugin_id(String plugin_id) {
		this.plugin_id = plugin_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserCookie() {
		return userCookie;
	}

	public void setUserCookie(String userCookie) {
		this.userCookie = userCookie;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getBestScore() {
		return bestScore;
	}

	public void setBestScore(String bestScore) {
		this.bestScore = bestScore;
	}

	public int getbestSore() {
		try {
			int score = Integer.parseInt(bestScore);
			return score;
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		}
	}
}
