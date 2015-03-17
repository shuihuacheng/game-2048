package com.leo.game.number.utils;

import java.util.ArrayList;
import java.util.Random;

import android.R.integer;
import android.util.Log;

public class CellMap {
	private int[][] m_vCellMaps = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
	private int mapEdited = 0xFFFF;

	public CellMap() {
		// for (int i = 0; i < 16; i += 3) {
		// mapEdited = BinaryUtils.setBinaryPosZero(mapEdited, i);
		// }
	}

	public boolean setPosNum(int x, int y, int num) {
		if (x >= 4 || y >= 4) {
			return false;
		}
		m_vCellMaps[x][y] = num;
		return true;
	}

	public int[][] getCellMaps() {
		return m_vCellMaps;
	}

	// 设置所有的区域都是可编辑
	private void setMapEdite() {
		mapEdited = 0xFFFF;
	}

	public void createMapNew() {
		setMapEdite();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				m_vCellMaps[i][j] = 0;
			}
		}
	}

	// 在map表中为0的位置 随机生成一个数字，并返回当前的位置数字
	public int createRandowNum(int num) {
		// 把所有的为0的数字放到数组里
		// printCells();
		ArrayList<Integer> m_vEmptyCells = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (m_vCellMaps[i][j] == 0) {
					m_vEmptyCells.add(10 * i + j);
				}
			}
		}
		int size = m_vEmptyCells.size();
		if (size == 0) {
			return -1;
		}
		int random = new Random().nextInt(size);
		int pos = m_vEmptyCells.get(random);
		setPosNum(pos / 10, pos % 10, num);
		return pos;
	}

	// 判断是否可以从左到右移动
	public boolean checkMoveRight() {
		// 判断游戏是否可以左右移动
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int num = m_vCellMaps[i][j];
				if (j + 1 >= 4 || num == 0) {
					continue;
				} else {
					int right = m_vCellMaps[i][j + 1];
					if (num == right || right == 0) {
						// Log.d("move", "可以向右滑动");
						return true;
					}
				}
			}
		}
		// Log.d("move", "不可以向右滑动");
		return false;
	}

	// 判断是否可以从右到左移动
	public boolean checkMoveLeft() {
		// 判断游戏是否可以左右移动
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int num = m_vCellMaps[i][j];
				if (j - 1 < 0 || num == 0) {
					continue;
				} else {
					int right = m_vCellMaps[i][j - 1];
					if (num == right || right == 0) {
						// Log.d("move", "可以向左滑动");
						return true;
					}
				}
			}
		}
		// Log.d("move", "不可以向左滑动");
		return false;
	}

	public boolean checkMoveBottom() {
		// 判断游戏是否可以向下移动
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int num = m_vCellMaps[i][j];
				if (i + 1 >= 4 || num == 0) {
					continue;
				} else {
					int bottom = m_vCellMaps[i + 1][j];
					if (num == bottom || bottom == 0) {
						// Log.d("move", "可以向下滑动");
						return true;
					}
				}
			}
		}
		// Log.d("move", "不可以向下滑动");
		return false;
	}

	public boolean checkMoveTop() {
		// 判断游戏是否可以上下移动
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int num = m_vCellMaps[i][j];
				if (i - 1 < 0 || num == 0) {
					continue;
				} else {
					int bottom = m_vCellMaps[i - 1][j];
					if (num == bottom || bottom == 0) {
						// Log.d("move", "可以向上滑动");
						return true;
					}
				}
			}
		}
		// Log.d("move", "不可以向上滑动");
		return false;
	}

	public boolean checkGameOver() {
		// 判断是否游戏结束
		if (checkMoveLeft() || checkMoveRight() || checkMoveTop() || checkMoveBottom()) {
			return false;
		} else
			return true;
	}

	// 数组数据 从左向右滑动
	public ArrayList<CellAniPoint> setMapTransRight() {
		setMapEdite();
		ArrayList<CellAniPoint> vAniPoints = new ArrayList<CellAniPoint>();
		for (int i = 0; i < 4; i++) {
			for (int j = 3; j >= 0; j--) {
				int next = getNextZero(getRowNums(i), j, i, true);
				if (next != j) {
					CellAniPoint cellAniPoint = new CellAniPoint();
					cellAniPoint.sCollum = j;
					cellAniPoint.sRow = i;
					cellAniPoint.dCollum = next;
					cellAniPoint.dRow = i;
					cellAniPoint.sNum = m_vCellMaps[i][j];
					cellAniPoint.dNum = m_vCellMaps[i][j];
					if (m_vCellMaps[i][next] != 0) {
						cellAniPoint.dNum = 2 * m_vCellMaps[i][next];
						setPosNum(i, j, 2 * m_vCellMaps[i][j]);
						mapEdited = BinaryUtils.setBinaryPosZero(mapEdited, 15 - (i * 4 + next));
					}
					setPosNum(i, j, 0);
					setPosNum(i, next, cellAniPoint.dNum);
					vAniPoints.add(cellAniPoint);
				}
			}
		}
		return vAniPoints;
	}

	// 数组数据 从右向左滑动
	public ArrayList<CellAniPoint> setMapTransLeft() {
		setMapEdite();
		ArrayList<CellAniPoint> vAniPoints = new ArrayList<CellAniPoint>();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int next = getBeforeZero(getRowNums(i), j, i, true);
				if (next != j) {
					CellAniPoint cellAniPoint = new CellAniPoint();
					cellAniPoint.sCollum = j;
					cellAniPoint.sRow = i;
					cellAniPoint.dCollum = next;
					cellAniPoint.dRow = i;
					cellAniPoint.sNum = m_vCellMaps[i][j];
					cellAniPoint.dNum = m_vCellMaps[i][j];
					if (m_vCellMaps[i][next] != 0) {
						cellAniPoint.dNum = 2 * m_vCellMaps[i][next];
						setPosNum(i, j, 2 * m_vCellMaps[i][j]);
						mapEdited = BinaryUtils.setBinaryPosZero(mapEdited, 15 - (i * 4 + next));
					}
					setPosNum(i, j, 0);
					setPosNum(i, next, cellAniPoint.dNum);
					vAniPoints.add(cellAniPoint);
				}
			}
		}
		return vAniPoints;
	}

	// 数组数据 从上向下滑动
	public ArrayList<CellAniPoint> setMapTransBottom() {
		setMapEdite();
		ArrayList<CellAniPoint> vAniPoints = new ArrayList<CellAniPoint>();
		for (int colum = 0; colum < 4; colum++) {
			for (int row = 3; row >= 0; row--) {
				int next = getNextZero(getColumNums(colum), row, colum, false);
				if (next != row) {
					CellAniPoint cellAniPoint = new CellAniPoint();
					cellAniPoint.sCollum = colum;
					cellAniPoint.sRow = row;
					cellAniPoint.dCollum = colum;
					cellAniPoint.dRow = next;
					cellAniPoint.sNum = m_vCellMaps[row][colum];
					cellAniPoint.dNum = m_vCellMaps[row][colum];
					if (m_vCellMaps[next][colum] != 0) {
						cellAniPoint.dNum = 2 * m_vCellMaps[next][colum];
						setPosNum(next, colum, 2 * m_vCellMaps[next][colum]);
						mapEdited = BinaryUtils.setBinaryPosZero(mapEdited, 15 - (next * 4 + colum));
					}
					setPosNum(row, colum, 0);
					setPosNum(next, colum, cellAniPoint.dNum);
					vAniPoints.add(cellAniPoint);
				}
			}
		}
		return vAniPoints;
	}

	// 数组数据 从下向上滑动
	public ArrayList<CellAniPoint> setMapTransTop() {
		setMapEdite();
		ArrayList<CellAniPoint> vAniPoints = new ArrayList<CellAniPoint>();
		for (int colum = 0; colum < 4; colum++) {
			for (int row = 0; row < 4; row++) {
				int next = getBeforeZero(getColumNums(colum), row, colum, false);
				if (next != row) {
					CellAniPoint cellAniPoint = new CellAniPoint();
					cellAniPoint.sCollum = colum;
					cellAniPoint.sRow = row;
					cellAniPoint.dCollum = colum;
					cellAniPoint.dRow = next;
					cellAniPoint.sNum = m_vCellMaps[row][colum];
					cellAniPoint.dNum = m_vCellMaps[row][colum];
					if (m_vCellMaps[next][colum] != 0) {
						cellAniPoint.dNum = 2 * m_vCellMaps[next][colum];
						setPosNum(next, colum, 2 * m_vCellMaps[next][colum]);
						mapEdited = BinaryUtils.setBinaryPosZero(mapEdited, 15 - (next * 4 + colum));
					}
					setPosNum(row, colum, 0);
					setPosNum(next, colum, cellAniPoint.dNum);
					vAniPoints.add(cellAniPoint);
				}
			}
		}
		return vAniPoints;
	}

	public void printCells() {
		String lines = "";
		for (int i = 0; i < 4; i++) {
			lines = "";
			for (int j = 0; j < 4; j++) {
				lines += m_vCellMaps[i][j] + "  ";
			}
			System.out.println(lines);
		}
	}

	public int[][] getNums() {
		return m_vCellMaps;
	}

	// 获取nRow对应的一行数据
	public int[] getRowNums(int nRow) {
		int[] vLines = m_vCellMaps[nRow];
		return vLines;
	}

	// 获取nColum对应的一列数据
	public int[] getColumNums(int nColum) {
		int[] vColums = new int[] { m_vCellMaps[0][nColum], m_vCellMaps[1][nColum], m_vCellMaps[2][nColum], m_vCellMaps[3][nColum] };
		return vColums;
	}

	// 获取index能够到达的最远距离不超过maxIndex的最远距离，0 或相同距离的都能到达
	private int getNextZero(int vValues[], int nColum, int nRow, boolean bHorizon) {
		if (nColum >= 3) {
			return 3;
		}
		int cur = vValues[nColum];
		if (cur == 0) {
			return nColum;
		}
		int resultIndex = nColum; // 能够达到的最大位置 返回结果
		for (int i = nColum + 1; i <= 3; i++) {
			int next = vValues[i];
			if (next == 0) {
				resultIndex = i;
				continue;
			}
			boolean bEdited = false;
			if (bHorizon) {
				bEdited = BinaryUtils.checkBinaryPos(mapEdited, (15 - (i + nRow * 4)));
			} else {
				bEdited = BinaryUtils.checkBinaryPos(mapEdited, (3 - nRow) + (3 - i) * 4);
			}
			if (cur == next && !bEdited) {
				resultIndex = i;
				continue;
			} else {
				break;
			}
		}
		return resultIndex;
	}

	// 获取index能够到达的最远距离不超过maxIndex的最远距离，0 或相同距离的都能到达
	private int getBeforeZero(int vValues[], int nColum, int nRow, boolean bHorizon) {
		if (nColum <= 0) {
			return 0;
		}
		int cur = vValues[nColum];
		if (cur == 0) {
			return nColum;
		}
		int resultIndex = nColum; // 能够达到的最大位置 返回结果
		for (int i = nColum - 1; i >= 0; i--) {
			int next = vValues[i];
			if (next == 0) {
				resultIndex = i;
				continue;
			}
			boolean bEdited = false;
			if (bHorizon) {
				bEdited = BinaryUtils.checkBinaryPos(mapEdited, 15 - (nRow * 4 + i));
			} else {
				bEdited = BinaryUtils.checkBinaryPos(mapEdited, 15 - (nRow + i * 4));
			}
			if (cur == next && !bEdited) {
				resultIndex = i;
				continue;
			} else {
				break;
			}
		}
		return resultIndex;
	}
}
