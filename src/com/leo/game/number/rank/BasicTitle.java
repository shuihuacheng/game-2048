package com.leo.game.number.rank;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leo.game.number.R;

public class BasicTitle extends RelativeLayout {

	public BasicTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		createTitle(context);
		createLBtn(context);
		createRBtn(context);
		createCustomView(context);
	}

	public BasicTitle(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void createTitle(Context context) {
		txtTitle = new TextView(context);
		txtTitle.setId(R.id.BaseTitle_Title);
		RelativeLayout.LayoutParams lpTitle = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpTitle.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		txtTitle.setTextColor(getResources().getColor(R.color.white));
		this.addView(txtTitle, lpTitle);
	}

	public void createCustomView(Context context) {
		customView = new FrameLayout(context);
		customView.setId(R.id.BaseTitle_CustomView);
		RelativeLayout.LayoutParams lpCustom = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpCustom.addRule(RelativeLayout.RIGHT_OF, txtTitle.getId());
		lpCustom.addRule(RelativeLayout.ALIGN_BOTTOM, txtTitle.getId());
		this.addView(customView, lpCustom);
	}

	public void createLBtn(Context context) {
		btnL = new ImageView(context);
		btnL.setId(R.id.BaseTitle_LBtn);
		RelativeLayout.LayoutParams lpLBtn = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.title_dimens), (int) getResources().getDimension(R.dimen.title_dimens));
		lpLBtn.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		btnL.setBackgroundResource(R.color.orange_cur);
		int dpValue = DensityUtil.dip2px(context, 10);
		btnL.setPadding(dpValue, dpValue, dpValue, dpValue);
		btnL.setVisibility(View.GONE);
		btnL.setClickable(true);
		this.addView(btnL, lpLBtn);
	}

	public void createRBtn(Context context) {
		btnR = new ImageView(context);
		btnR.setId(R.id.BaseTitle_RBtn);
		RelativeLayout.LayoutParams lpRBtn = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.title_dimens), (int) getResources().getDimension(R.dimen.title_dimens));
		lpRBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		btnR.setBackgroundResource(R.color.orange_cur);
		int dpValue = DensityUtil.dip2px(context, 10);
		btnR.setPadding(dpValue, dpValue, dpValue, dpValue);
		btnR.setVisibility(View.GONE);
		btnL.setClickable(true);
		this.addView(btnR, lpRBtn);
	}

	TextView txtTitle;
	ImageView btnL;
	ImageView btnR;
	FrameLayout customView;

	public void setTitle(String title) {
		txtTitle.setText(title);
	}

	public void setLBtn(int resourceId, OnClickListener clickListener) {
		btnL.setImageResource(resourceId);
		btnL.setOnClickListener(clickListener);
		btnL.setVisibility(View.VISIBLE);
		setLBtnClickStatus(false);
	}

	public void setLBtn(Bitmap icon, OnClickListener clickListener) {
		btnL.setImageBitmap(icon);
		btnL.setOnClickListener(clickListener);
		btnL.setVisibility(View.VISIBLE);
		setLBtnClickStatus(false);
	}

	public void setLBtnClickStatus(boolean bClick) {
		if (bClick) {
			btnL.setBackgroundResource(R.color.orange_cur);
			btnL.getDrawable().setAlpha(255);
		} else {
			btnL.setBackgroundResource(R.color.orange);
			btnL.getDrawable().setAlpha((int) (0.9 * 255));

		}
	}

	public void setLBtnGone() {
		btnL.setVisibility(View.GONE);
	}

	public void setRBtn(int resurceId, OnClickListener clickListener) {
		btnR.setImageResource(resurceId);
		btnR.setOnClickListener(clickListener);
		btnR.setVisibility(View.VISIBLE);
		setRBtnClickStatus(false);
	}

	public void setRBtn(Bitmap icon, OnClickListener clickListener) {
		btnR.setImageBitmap(icon);
		btnR.setOnClickListener(clickListener);
		btnR.setVisibility(View.VISIBLE);
		setRBtnClickStatus(false);
	}

	public void setRBtnClickStatus(boolean bClick) {
		if (bClick) {
			btnR.setBackgroundResource(R.color.orange_cur);
			btnR.getDrawable().setAlpha(255);
		} else {
			btnR.setBackgroundResource(R.color.orange);
			btnR.getDrawable().setAlpha((int) (0.9 * 255));

		}
	}

	public void setRBtnGone() {
		btnR.setVisibility(View.GONE);
	}

	public View getCustomView() {
		return customView;
	}

}
