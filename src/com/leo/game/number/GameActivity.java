package com.leo.game.number;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestQueueManager;
import com.leo.game.number.ConfirmDialog.OnDialogClickListener;
import com.leo.game.number.rank.GameActivity_Ranking;
import com.leo.game.number.rank.GameUrl;
import com.leo.game.number.utils.AESUtils;
import com.leo.game.number.utils.CellAniPoint;
import com.leo.game.number.utils.CellMap;
import com.leo.game.number.utils.CellView;
import com.leo.game.number.utils.CellViewMap;
import com.leo.game.number.utils.DConfig;
import com.leo.game.number.utils.ProjectUtils;

public class GameActivity extends FragmentActivity {
	ProjectUtils projectUtils;
	// 都是游戏坐标
	float touchDistance; // 判断在屏幕中滑动的距离最小值

	FrameLayout rootView;
	FrameLayout cellViewContainers;

	GameTextView txtStep;
	// GameTextView txtScore;
	// GameTextView txtBestScore;

	LostView lostView;// 游戏失败现实的View
	WinView winView; // 游戏成功的View

	GameImageView btnRetry;
	GameImageView btnHome;
	GameImageView btnRank;
	
	// cell子View对应的屏幕坐标
	CellMap cellMap = new CellMap();
	// 记录子View 根据坐标 11 22 10 等定位获取
	private HashMap<Integer, CellView> m_vCellViews = new HashMap<Integer, CellView>();

	int nStep = 0;
	int nScore = 0;
	int maxScore = 0;
	int bestScore = 0;

	boolean bGameOver = false; // 是否游戏结束

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		rootView = new FrameLayout(this);
		setContentView(rootView);
		cellViewContainers = new FrameLayout(this);
		cellViewContainers.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		initScreenSize();
		addBack();
		addBtns();
		addTextTip();
		// initCell();
		resetGames();
		// addStep();
		rootView.addView(cellViewContainers);
		initUserInfo();

	}

	private GameApplication mApplication;

	private void initUserInfo() {
		mApplication = (GameApplication) getApplication();
		Bundle bundle = getIntent().getExtras();
		// bundle = testBundle();
		if (bundle != null && bundle.containsKey("id")) {
			String pluginId = bundle.getString("plugin_id");
			String id = bundle.getString("id");
			String title = bundle.getString("title");
			String cookie = bundle.getString("cookie");
			String imei = bundle.getString("imei");
			String bestScore = bundle.getString("bestScore");
			mApplication.initGameInfo(pluginId, id, cookie, imei, title, bestScore);
		}
	}

	private Bundle testBundle() {
		Bundle bundle = new Bundle();
		bundle.putString("id", "game2048");
		bundle.putString("cookie", "");
		bundle.putString("plugin_id", "");
		bundle.putString("imei", "");
		bundle.putString("title", "2048");
		bundle.putString("bestScore", "");
		return bundle;
	}

	private void initScreenSize() {
		projectUtils = new ProjectUtils(this);
		CellViewMap.getInstance().initCellMap(this);
		touchDistance = projectUtils.getDensity() * 8;
	}

	private void addBack() {
		Bitmap back = CellViewMap.getInstance().getImageFromAssetFile("back.png");
		if (back == null) {
			return;
		}
		ImageView imgBack = new ImageView(this);
		float width = projectUtils.getSizeHorizen(1080);
		float height = projectUtils.getSizeVertical(1920);
		imgBack.setLayoutParams(new LayoutParams((int) width, (int) height));
		imgBack.setScaleType(ScaleType.FIT_XY);
		imgBack.setImageBitmap(back);
		rootView.addView(imgBack);
	}

	private void addBtns() {
		int imgWidth = (int) projectUtils.getSizeHorizen(120);
		int imgHeight = (int) projectUtils.getSizeVertical(120);
		int imgWidthAchieve = (int) projectUtils.getSizeHorizen(120);
		int imgHeightAchieve = (int) projectUtils.getSizeVertical(120);
		Point posAchieve = new Point((int) projectUtils.getSizeHorizen(264), (int) projectUtils.getSizeVertical(1500));
		btnHome = new GameImageView(this, imgWidthAchieve, imgHeightAchieve, posAchieve, "home.png", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		rootView.addView(btnHome);

		Point posRank = new Point((int) projectUtils.getSizeHorizen(480), (int) projectUtils.getSizeVertical(1500));
		btnRank = new GameImageView(this, imgWidthAchieve, imgHeightAchieve, posRank, "rank.png", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mApplication.getGameInfo() != null) {
					startActivity(new Intent(GameActivity.this, GameActivity_Ranking.class));
				}
			}
		});
		rootView.addView(btnRank);

		Point posRetry = new Point((int) projectUtils.getSizeHorizen(696), (int) projectUtils.getSizeVertical(1500));
		btnRetry = new GameImageView(this, imgWidth, imgHeight, posRetry, "retry.png", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (bGameOver || nStep <= 2) {
					resetGames();
					createNewGame();
				} else {
					showConfirmDialog();
				}

			}
		});
		rootView.addView(btnRetry);
	}

	private void initLostView() {
		lostView = new LostView(this, projectUtils.getScaleW(), projectUtils.getScaleH(), new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetGames();
				createNewGame();

			}
		});
		lostView.startApperance();
	}

	private void initWinView() {
		winView = new WinView(this, projectUtils.getScaleW(), projectUtils.getScaleH(), new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetGames();
				createNewGame();

			}
		});
		winView.startApperance();
	}

	private void addTextTip() {
		txtStep = new GameTextView(this, (int) projectUtils.getSizeHorizen(300), (int) projectUtils.getSizeVertical(120), 14);
		// txtScore = new GameTextView(this, (int)
		// projectUtils.getSizeHorizen(130), (int)
		// projectUtils.getSizeVertical(50), 14);
		// txtBestScore = new GameTextView(this, (int)
		// projectUtils.getSizeHorizen(260), (int)
		// projectUtils.getSizeVertical(50), 14);
		txtStep.setX(projectUtils.getSizeHorizen(1080 - 312 - 60));
		txtStep.setY(projectUtils.getSizeVertical(96));

		// txtScore.setX(projectUtils.getSizeHorizen(420));
		// txtScore.setY(projectUtils.getSizeVertical(140));

		// txtBestScore.setX(projectUtils.getSizeHorizen(420));
		// txtBestScore.setY(projectUtils.getSizeVertical(50));
		rootView.addView(txtStep);
		// rootView.addView(txtScore);
		// rootView.addView(txtBestScore);
	}

	private void initCell() {
		checkCellH();
		resetCells();
	}

	private void resetGames() {
		if (winView != null && winView.findViewWithTag(winView.getTag()) != null) {
			rootView.removeView(winView);
		}
		if (lostView != null && rootView.findViewWithTag(lostView.getTag()) != null) {
			rootView.removeView(lostView);
		}
		setBtnClickable(true);
		nStep = 0;
		txtStep.setText("0");
		nScore = 0;
		maxScore = 0;
		bGameOver = false;
		// txtScore.setText("0");
		cellMap.createMapNew();
		m_vCellViews.clear();
		bestScore = DConfig.Preference.getIntPref(this, DConfig._Preference_Key_MAXSCORE, 10000);

		// txtBestScore.setText(bestScore + "");
		resetCells();
		if (lostView != null && rootView.findViewWithTag(lostView.getTag()) != null) {
			rootView.removeView(lostView);
		}
		if (winView != null && winView.findViewWithTag(winView.getTag()) != null) {
			rootView.removeView(winView);
		}

	}

	private void createNewGame() {
		cellMap.createMapNew();
		int nPos1 = createNewCell();
		int nPos2 = createNewCell();
		setCellNew(nPos1);
		setCellNew(nPos2);
		resetCells();
	}

	private void checkCellH() {
		cellMap.setPosNum(0, 0, 1024);
		cellMap.setPosNum(0, 1, 128);
		cellMap.setPosNum(0, 2, 512);
		cellMap.setPosNum(0, 3, 128);
		cellMap.setPosNum(1, 0, 512);
		cellMap.setPosNum(1, 1, 128);
		cellMap.setPosNum(1, 2, 1024);
		cellMap.setPosNum(1, 3, 8);
		cellMap.setPosNum(2, 0, 128);
		cellMap.setPosNum(2, 1, 256);
		cellMap.setPosNum(2, 2, 128);
		cellMap.setPosNum(2, 3, 256);
		cellMap.setPosNum(3, 0, 256);
		cellMap.setPosNum(3, 1, 128);
		cellMap.setPosNum(3, 2, 128);
		cellMap.setPosNum(3, 3, 128);
	}

	private void checkCellV() {
		cellMap.setPosNum(0, 0, 4);
		cellMap.setPosNum(1, 0, 8);
		cellMap.setPosNum(2, 0, 4);
		cellMap.setPosNum(3, 0, 0);
		cellMap.setPosNum(0, 1, 8);
		cellMap.setPosNum(1, 1, 4);
		cellMap.setPosNum(2, 1, 2);
		cellMap.setPosNum(3, 1, 2);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (bGameOver) {
			createNewGame();
		} else {
			initFeatureMap();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (bGameOver) {
			removeFeatureMap();
		} else {
			setFeatureMap();
		}
	}

	private void initFeatureMap() {
		String map = DConfig.Preference.getStringPref(getApplicationContext(), DConfig._Preference_Key_FeatureCells, "");
		map = AESUtils.AES_Decrypt("2lKLDFJKLAXC0V094RNCVK094109D09F", map);
		if (map == null) {
			map = "";
		}
		String[] value = map.split("#");
		if (value.length > 2) {
			for (int i = 0; i < value.length; i++) {
				String cell = value[i];
				// cellMap.createMapNew();
				if (!TextUtils.isEmpty(cell) && cell.contains("_")) {
					try {
						String[] vValus = cell.split("_");
						int xPos = Integer.parseInt(vValus[0]);
						int yPos = Integer.parseInt(vValus[1]);
						int v = Integer.parseInt(vValus[2]);
						cellMap.setPosNum(xPos, yPos, v);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			nStep = DConfig.Preference.getIntPref(getApplicationContext(), DConfig._Preference_Key_FeatureStep, 0);
			txtStep.setText(nStep + "");
			resetCells();
		} else {
			createNewGame();
		}

	}

	private void setFeatureMap() {
		if (cellMap != null) {
			String feature = "";
			int[][] vmaps = cellMap.getCellMaps();
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (vmaps[i][j] != 0) {
						String cell = "";
						cell = i + "_" + j + "_" + vmaps[i][j];
						feature += cell;
						feature += "#";
					}
				}
			}
			feature = AESUtils.AES_Encrypt("2lKLDFJKLAXC0V094RNCVK094109D09F", feature);
			DConfig.Preference.setStringPref(getApplicationContext(), DConfig._Preference_Key_FeatureCells, feature);
			DConfig.Preference.setIntPref(getApplicationContext(), DConfig._Preference_Key_FeatureStep, nStep);
		}
	}

	private void removeFeatureMap() {
		DConfig.Preference.removePref(getApplicationContext(), DConfig._Preference_Key_FeatureCells);
		DConfig.Preference.removePref(getApplicationContext(), DConfig._Preference_Key_FeatureStep);
	}

	// 在随机的位置 生成一个随即的数字。 数字的生成概率 是 2 50% 4 30% 8 15% 16 5%
	private int createNewCell() {

		int num = new Random().nextInt(100 + 1);
		int randomNum = 2;
		if (num >= 0 && num <= 70) {
			// 生成随机数字2
			randomNum = 2;
		}
		if (num > 70 && num <= 95) {
			// 生成随机数字4
			randomNum = 4;
		}
		if (num > 95 && num <= 98) {
			// 生成随机数字 8
			randomNum = 8;
		}
		if (num > 99 && num <= 100) {
			// 生成随机数字16
			randomNum = 16;
		}
		int nPos = cellMap.createRandowNum(randomNum);
		return nPos;
	}

	// 全体cell向左滑动
	private void moveCellsLeft() {
		if (cellMap.checkMoveLeft() && !bGameOver) {
			ArrayList<CellAniPoint> vAniPoints = cellMap.setMapTransLeft();
			moveAllCells(vAniPoints);
		}
	}

	// 全体cell向右滑动
	private void moveCellsRight() {
		if (cellMap.checkMoveRight() && !bGameOver) {
			ArrayList<CellAniPoint> vAniPoints = cellMap.setMapTransRight();
			moveAllCells(vAniPoints);
		}
	}

	// 全体Cell向上滑动

	private void moveCellsTop() {
		if (cellMap.checkMoveTop() && !bGameOver) {
			ArrayList<CellAniPoint> vAniPoints = cellMap.setMapTransTop();
			moveAllCells(vAniPoints);
		}

	}

	// 全体Cell向下滑动
	private void moveCellsBottom() {
		if (cellMap.checkMoveBottom() && !bGameOver) {
			ArrayList<CellAniPoint> vAniPoints = cellMap.setMapTransBottom();
			moveAllCells(vAniPoints);
		}

	}

	private void setBtnClickable(boolean bEnable) {
		btnHome.setClickable(bEnable);
		btnRank.setClickable(bEnable);
	}

	private void moveAllCells(ArrayList<CellAniPoint> vAniPoints) {
		final ArrayList<CellAniPoint> vEditedCellPos = new ArrayList<CellAniPoint>();
		addStep();
		// 开启动画
		for (int i = 0; i < vAniPoints.size(); i++) {
			CellAniPoint aniPoint = vAniPoints.get(i);
			CellView cellView = m_vCellViews.get(aniPoint.sRow * 10 + aniPoint.sCollum);
			if (cellView == null) {
				continue;
			}
			cellView.moveFromTo(aniPoint, 250);
			if (aniPoint.bEdited()) {
				vEditedCellPos.add(aniPoint);
			}
		}
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int nPos = createNewCell();
				resetCells();
				if (maxScore >= 2048) {
					bGameOver = true;
					showWin();
				} else {
					setCellNew(nPos);
					setCellEditedAni(vEditedCellPos);
					bGameOver = cellMap.checkGameOver();
					Log.d("move", "游戏是否结束：" + bGameOver + " nPos:" + nPos);
					if (bGameOver) {
						showLost();
					}
				}
			}
		}, 250);
	}

	private void resetCells() {
		cellViewContainers.removeAllViews();
		int[][] m_vCells = cellMap.getNums();
		for (int i = 0; i < m_vCells.length; i++) {
			for (int j = 0; j < m_vCells[i].length; j++) {
				if (m_vCells[i][j] != 0) {
					CellView cellView = new CellView(this, i, j, m_vCells[i][j]);
					cellViewContainers.addView(cellView);
					m_vCellViews.put(i * 10 + j, cellView);
					if (maxScore < m_vCells[i][j]) {
						maxScore = m_vCells[i][j];
					}
				}
			}
		}
		// System.out.println("重置结果集：");
		// cellMap.printCells();
		// rootView.postInvalidate();
	}

	// 将需要编辑的cell设置为已经编辑，并开启动画
	private void setCellEditedAni(ArrayList<CellAniPoint> vEditedCellPos) {
		for (int i = 0; i < vEditedCellPos.size(); i++) {
			CellAniPoint curCell = vEditedCellPos.get(i);
			int nPos = curCell.dRow * 10 + curCell.dCollum;
			CellView cellView = m_vCellViews.get(nPos);
			if (cellView != null) {
				cellView.startEdited();
				addScore(curCell.sNum);
			}

		}
	}

	// 将nPos位置设置动画生成
	private void setCellNew(int nPos) {
		CellView cellView = m_vCellViews.get(nPos);
		if (null != cellView) {
			cellView.startApperance();
		}
	}

	/**
	 * 判断是向左还是滑动方向
	 */
	float x_tmp1;
	float y_tmp1;
	float x_tmp2;
	float y_tmp2;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取当前坐标
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x_tmp1 = x;
			y_tmp1 = y;
			break;
		case MotionEvent.ACTION_UP:
			x_tmp2 = x;
			y_tmp2 = y;
			float xDistance = x_tmp1 - x_tmp2;
			float yDistance = y_tmp1 - y_tmp2;
			if (Math.abs(xDistance) >= Math.abs(yDistance) && Math.abs(xDistance) >= touchDistance) {
				if (xDistance > 0) {
					moveCellsLeft();
					return true;
				} else {
					moveCellsRight();
					return true;
				}
			}
			if (Math.abs(xDistance) < Math.abs(yDistance) && Math.abs(yDistance) > touchDistance) {
				if (yDistance > 0) {
					moveCellsTop();
					return true;
				} else {
					moveCellsBottom();
					return true;
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	private void showLost() {
		if (lostView == null) {
			initLostView();
		}
		setBtnClickable(false);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (nScore > bestScore) {
					bestScore = nScore;
					DConfig.Preference.setIntPref(GameActivity.this, DConfig._Preference_Key_MAXSCORE, bestScore);
				}
				if (lostView != null && rootView.findViewWithTag(lostView.getTag()) != null) {
					rootView.removeView(lostView);
				}
				rootView.addView(lostView);
			}
		}, 500);
	}

	private void showWin() {
		if (null == winView) {
			initWinView();
		}
		setBtnClickable(false);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (nScore > bestScore) {
					bestScore = nScore;
					DConfig.Preference.setIntPref(GameActivity.this, DConfig._Preference_Key_MAXSCORE, bestScore);
				}
				updateNet(nStep);
				if (winView != null && winView.findViewWithTag(winView.getTag()) != null) {
					rootView.removeView(winView);
				}
				rootView.addView(winView);
			}
		}, 250);
	}

	private void addStep() {
		nStep++;
		if (txtStep != null) {
			txtStep.setText(nStep + "");
		}
	}

	private void addScore(int score) {
		nScore += score;
		// if (txtScore != null) {
		// txtScore.setText(nScore + "");
		// }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 更新服务端数据
	private void updateNet(final int score) {
		if (mApplication == null || mApplication.getGameInfo() == null) {
			return;
		}
		HashMap<String, Object> vParams = new HashMap<String, Object>();
		vParams.put("id", mApplication.getGameInfo().getId());
		vParams.put("score", score);
		vParams.put("begin_time", mApplication.getBeginTime());
		vParams.put("imei", mApplication.getGameInfo().getImei());
		vParams.put("end_time", mApplication.getEndTime());
		RequestQueueManager.getInstance().postJsonRequest(this, GameUrl.geturl_updateScore(), new JSONObject(vParams), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				System.out.println(error.toString());

			}
		});

	};

	// 现实是否刷新的对话框
	private void showConfirmDialog() {
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setTitle(0, "游戏还没结束");
		dialog.setOnDialigClick(new OnDialogClickListener() {

			@Override
			public void onClickConfirm() {
				// TODO Auto-generated method stub
				removeFeatureMap();
				dialog.dismiss();
				resetGames();
				createNewGame();
			}

			@Override
			public void onClickCancle() {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show(getSupportFragmentManager(), "dialog");
	}
}
