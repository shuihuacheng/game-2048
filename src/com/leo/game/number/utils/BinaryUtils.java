package com.leo.game.number.utils;

public class BinaryUtils {
	// 检查第nPos位置是否为 0
	public static boolean checkBinaryPos(int binary, int nPos) {
		boolean bResult = (binary & ((int) Math.pow(2, nPos))) == 0;
		return bResult;
	}

	// 将第nPos位置置为0
	public static int setBinaryPosZero(int binary, int nPos) {
		int nResult = binary;
		nResult = binary ^ ((int) Math.pow(2, nPos));
		return nResult;
	}
}
