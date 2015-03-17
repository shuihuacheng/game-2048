package com.leo.game.number;

import android.app.Application;

import com.android.volley.toolbox.RequestQueueManager;
import com.leo.game.number.utils.DConfig;

public class GameApplication extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		setDebug(false);
	}

	UserInfo gameInfo;

	public void initGameInfo(String plugin_id, String id, String cookie, String imei, String title, String bestScore) {
		gameInfo = new UserInfo();
		gameInfo.setPlugin_id(plugin_id);
		gameInfo.setId(id);
		gameInfo.setUserCookie(cookie);
		gameInfo.setImei(imei);
		gameInfo.setTitle(title);
		gameInfo.setBestScore(bestScore);
		RequestQueueManager.getInstance().init(this, gameInfo.getUserCookie());
	}

	public void clearGameInfo() {
		gameInfo = null;
	}

	public UserInfo getGameInfo() {
		return gameInfo;
	}

	private void setDebug(boolean debug) {
		DConfig.DEBUG = debug;
	}

	long beginTime;
	long endTime;

	public void updateBeginTime() {
		beginTime = System.currentTimeMillis() / 1000;
	}

	public void updateEndTime() {
		endTime = System.currentTimeMillis() / 1000;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public long getEndTime() {
		return endTime;
	}
}
