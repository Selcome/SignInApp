package shiqichan.singinapp;

import static shiqichan.singinapp.Config.*;

import android.util.Log;

public class PersonDetector {

	byte[] bestData;

	byte[] currentData;

	int width, height;

	int bestFactor;

	public void setPreviewSize(int width, int height) {
		Log.d(TAG, "==>>preview w,h: " + width + "," + height);
		this.width = width;
		this.height = height;
	}

	// 设置摄像头图像数据
	public void setData(byte[] data) {
		currentData = data;
	}

	// 获取最优的数据，用于拍照
	public byte[] getBestData() {
		return bestData;
	}

	// 清空所有数据
	public void clear() {
		bestData = null;
		currentData = null;
		bestFactor = 0;
	}

	// 是否有人
	public boolean hasPerson() {
		return detect(currentData, width, height) > 0;
	}

	private native int detect(byte[] frame, int width, int height);

	static {
		System.loadLibrary("PersonDetector");
	}
}
