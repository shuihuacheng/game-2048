package com.leo.game.number.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.widget.FrameLayout;

//CellView对应的坐标查询表 和 对应的数字Bitmap对应表
public class CellViewMap {
	private Context mContext;
	ProjectUtils projectUtils;
	private static CellViewMap _instance = null;
	private HashMap<Integer, Bitmap> m_vCellBitmaps = new HashMap<Integer, Bitmap>();
	// cell子View对应的屏幕坐标
	private ArrayList<Point> m_vCellPos = new ArrayList<Point>();

	// 都是游戏坐标
	float cellWidth = 210;
	float cellHeight = 210;
	float gameCellsMarginLeft = 60; // 整个区域左边距
	float gameCellsMarginTop = 312; // 整个区域上边距
	float childCellMarginLeft = 24; // 子View的左边距
	float childCellMarginTop = 24;// 子View的上边距

	public static CellViewMap getInstance() {
		// TODO Auto-generated constructor stub
		if (_instance == null) {
			_instance = new CellViewMap();
		}
		return _instance;
	}

	public void initCellMap(Activity context) {
		mContext = context;
		projectUtils = new ProjectUtils(context);
		initCellsBitmap();
		for (int j = 0; j < 4; j++) {
			String lines = "";
			for (int i = 0; i < 4; i++) {
				Point point = new Point();
				float gamePosX = i * cellWidth + gameCellsMarginLeft + (i + 1) * childCellMarginLeft;
				float gamePosY = j * cellHeight + gameCellsMarginTop + (j + 1) * childCellMarginTop;
				point.x = (int) (projectUtils.getSizeHorizen(gamePosX));
				point.y = (int) (projectUtils.getSizeVertical(gamePosY));
				m_vCellPos.add(point);
				lines += point.x + "/" + point.y + "   ";
			}
			System.out.println(lines);
		}
	}

	public Bitmap getBitmapFromNum(int num) {
		Bitmap bitScore = m_vCellBitmaps.get(num);
		return bitScore;
	}

	public int getCellWidth() {
		return (int) projectUtils.getSizeHorizen(cellWidth);
	}

	public int getCellHeight() {
		return (int) projectUtils.getSizeVertical(cellHeight);
	}

	public Point getScreenPointr(int row, int collum) {
		Point point = m_vCellPos.get(row * 4 + collum);
		return point;
	}

	private void initCellsBitmap() {
		m_vCellBitmaps = new HashMap<Integer, Bitmap>();
		for (int i = 1; i <= 11; i++) {
			int score = (int) Math.pow(2, (double) i);
			Bitmap bitScore = getImageFromAssetFile("number" + score + ".png");
			m_vCellBitmaps.put(score, bitScore);
		}
	}

	public Bitmap getImageFromAssetFile(String fileName) {
		String filePath = "drawable/" + fileName;
		Bitmap image = null;
		try {
			AssetManager am = mContext.getAssets();
			InputStream is = am.open(filePath);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {

		}
		return image;
	}
}
