package com.leo.game.number.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

//该工具类的作用是把游戏坐标 反射到屏幕坐标，左上角是 （0，0）
public class ProjectUtils {
	Activity mContext;
	private float gameWidth = 1080;
	private float gameHeight = 1920;

	private float screenWidth;
	private float screenHeight;
	private float density;

	private float scaleW;
	private float scaleH;

	public ProjectUtils(Activity context) {
		this.mContext = context;
		screenWidth = mContext.getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = mContext.getWindowManager().getDefaultDisplay().getHeight();

		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();
		density = dm.density;
		scaleW = screenWidth / gameWidth;
		scaleH = screenHeight / gameHeight;
	}

	// 获取水平方向的心坐标
	public float getSizeHorizen(float size) {
		return size * scaleW;
	}

	public float getSizeVertical(float size) {
		return size * scaleH;
	}

	public float getScaleW() {
		return scaleW;
	}

	public float getScaleH() {
		return scaleH;
	}

	public float getDensity() {
		return density;
	}

}
