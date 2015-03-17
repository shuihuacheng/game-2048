package com.leo.game.number;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.leo.game.number.utils.CellViewMap;

public class GameImageView extends ImageView {

	public GameImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public GameImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GameImageView(Context context, int width, int height, Point position, String iconName, OnClickListener onClickListener) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(width, height, position, iconName, onClickListener);
	}

	private void initView(int width, int height, Point position, String iconName, OnClickListener onClickListener) {
		setLayoutParams(new LayoutParams(width, height));
		Bitmap bitmap = CellViewMap.getInstance().getImageFromAssetFile(iconName);
		if (bitmap != null) {
			setImageBitmap(bitmap);
		}
		setScaleType(ScaleType.FIT_XY);
		setX(position.x);
		setY(position.y);
		setOnClickListener(onClickListener);
	}

}
