package com.leo.game.number;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.leo.game.number.utils.CellViewMap;

public class LostView extends FrameLayout {

	private float scaleW;
	private float scaleH;
	private OnClickListener clickRefresh;
	int layoutWidth;
	int layoutHeight;

	public LostView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public LostView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public LostView(Context context, float scaleW, float scaleH, OnClickListener onClickListener) {
		super(context);
		// TODO Auto-generated constructor stub
		setTag("lostView");
		this.clickRefresh = onClickListener;
		this.scaleH = scaleH;
		this.scaleW = scaleW;
		this.layoutWidth = (int) (scaleW * 1080);
		this.layoutHeight = (int) (scaleH * 1920);
		setLayoutParams(new LayoutParams(layoutWidth, layoutHeight));
		setBackgroundColor(Color.BLACK);
		getBackground().setAlpha(100);
		initBack();
		initBtnRefresh();
	}

	private void initBack() {
		Bitmap bitmap = CellViewMap.getInstance().getImageFromAssetFile("lost.png");
		if (bitmap == null) {
			return;
		}
		ImageView imgBack = new ImageView(getContext());
		imgBack.setLayoutParams(new LayoutParams(layoutWidth, layoutHeight));
		imgBack.setScaleType(ScaleType.FIT_XY);
		imgBack.setImageBitmap(bitmap);
		addView(imgBack);
	}

	private void initBtnRefresh() {
		Bitmap bitmap = CellViewMap.getInstance().getImageFromAssetFile("retry.png");
		if (bitmap == null) {
			return;
		}
		int layoutWidth = (int) (scaleW * (120 + 40));
		int layoutHeight = (int) (scaleH * (120 + 40));
		ImageView imgBtn = new ImageView(getContext());
		imgBtn.setLayoutParams(new LayoutParams(layoutWidth, layoutHeight));
		imgBtn.setScaleType(ScaleType.FIT_XY);
		imgBtn.setImageBitmap(bitmap);
		float xPos = scaleW * (696 - 20);
		float yPos = scaleH * (1440 - 20);
		imgBtn.setX(xPos);
		imgBtn.setY(yPos);
		addView(imgBtn);
		imgBtn.setOnClickListener(clickRefresh);
	}

	public void startApperance() {
		Animation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(150);
		scaleAnimation.setFillAfter(true);
		startAnimation(scaleAnimation);
	}
}
