package com.leo.game.number.utils;

//变化规则
public class CellAniPoint {
	// 坐标范围为16宫格的坐标
	public int sRow; // 原始位置行坐标
	public int sCollum; // 原始位置列
	public int dRow; // 目标移动行
	public int dCollum; // 目标移动列

	public int sNum; // 当前值
	public int dNum; // 变化值

	public boolean bEdited() {
		return sNum != dNum;
	}
}
