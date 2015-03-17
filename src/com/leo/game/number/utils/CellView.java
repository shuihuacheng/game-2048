package com.leo.game.number.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

public class CellView extends ImageView {

	public CellView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CellView(Context context, int row, int colum, int num) {
		super(context);
		setLayoutParams(new LayoutParams(CellViewMap.getInstance().getCellWidth(), CellViewMap.getInstance().getCellHeight()));
		setPosition(row, colum);
		setImageBitmap(CellViewMap.getInstance().getBitmapFromNum(num));
	}

	public void startApperance() {
		Animation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(150);
		scaleAnimation.setFillAfter(true);
		startAnimation(scaleAnimation);
	}

	public void startEdited() {
		Animation scaleAnimation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(150);
		scaleAnimation.setFillAfter(false);
		startAnimation(scaleAnimation);
	}

	public void setPosition(int row, int colum) {
		Point point = CellViewMap.getInstance().getScreenPointr(row, colum);
		// setX(point.x);
		// setY(point.y);
		LayoutParams lp = (LayoutParams) getLayoutParams();
		lp.leftMargin = point.x;
		lp.topMargin = point.y;
		setLayoutParams(lp);
		invalidate();
	}

	public void moveFromTo(final CellAniPoint aniPoint, final int duration) {
		final Point sPoint = CellViewMap.getInstance().getScreenPointr(aniPoint.sRow, aniPoint.sCollum);
		final Point dPoint = CellViewMap.getInstance().getScreenPointr(aniPoint.dRow, aniPoint.dCollum);
		TranslateAnimation moveAnimation = new TranslateAnimation(0, dPoint.x - sPoint.x, 0, dPoint.y - sPoint.y);
		moveAnimation.setInterpolator(AnimationUtils.loadInterpolator(getContext(), android.R.anim.decelerate_interpolator));
		moveAnimation.setDuration(duration);
		moveAnimation.setFillAfter(true);
		moveAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				resetImageNum(aniPoint.dNum);
			}
		});
		startAnimation(moveAnimation);

	}

	public void setViewEdited() {
		Animation scaleAnimation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(150);
		scaleAnimation.setFillAfter(false);
		startAnimation(scaleAnimation);
	}

	private void resetImageNum(int num) {
		setImageBitmap(CellViewMap.getInstance().getBitmapFromNum(num));
		// setViewEdited(0);
	}

}
