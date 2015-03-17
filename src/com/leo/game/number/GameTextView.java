package com.leo.game.number;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class GameTextView extends TextView {

	public GameTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public GameTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GameTextView(Context context, int width, int height, int size) {
		super(context);
		// TODO Auto-generated constructor stub
		initTextView(width, height, size);

	}

	private void initTextView(int width, int height, int size) {
		setLayoutParams(new LayoutParams(width, height));
		setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		setTextColor(getResources().getColor(R.color.txt_bg));
		setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/myFont3.ttf"), Typeface.BOLD);
		getPaint().setFakeBoldText(true);
		setGravity(Gravity.CENTER);
	}
}
