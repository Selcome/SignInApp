package shiqichan.singinapp;

import static shiqichan.singinapp.Config.*;
import static shiqichan.singinapp.Utils.*;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.util.Log;

public class PersonDetector {

	byte[] bestData;

	int bestFactor, currentFactor;

	Camera.Size size;

	int sampleCount;

	SampleFinishedCallback sampleFinishedCallback;

	public void setPreviewSize(Camera.Size size) {
		this.size = size;
	}

	public void setSampleFinishedCallback(
			SampleFinishedCallback sampleFinishedCallback) {
		this.sampleFinishedCallback = sampleFinishedCallback;
	}

	// 设置摄像头图像数据
	public void setData(byte[] data) {
		currentFactor = detect(data, size.height, size.width);// 因为是竖版
		sampleCount++;
		if (currentFactor > bestFactor) {
			bestFactor = currentFactor;
			bestData = data;
		}
		if (sampleCount == MAX_SAMPLE_COUNT && sampleFinishedCallback != null) {
			sampleFinishedCallback.callback(getBestImage());
		}
	}

	// 获取最清晰的帧作为照片
	private Bitmap getBestImage() {
		return rotateBitmap(convertYuvDataToBitmap(bestData, size), 90);
	}

	// 清空所有数据
	public void clear() {
		bestData = null;
		bestFactor = 0;
		currentFactor = 0;
		sampleCount=0;
		if(sampleFinishedCallback!=null){
			sampleFinishedCallback.clear();
		}
	}

	// 是否有人
	public boolean hasPerson() {
		Log.d(TAG, "current factor: " + currentFactor);
		return currentFactor > 0;
	}

	interface SampleFinishedCallback {
		public void callback(Bitmap bestImage);
		
		public void clear();
	}

	private native int detect(byte[] frame, int width, int height);

	static {
		System.loadLibrary("PersonDetector");
	}
}
