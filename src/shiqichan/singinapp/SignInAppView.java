package shiqichan.singinapp;

import static shiqichan.singinapp.Config.CAMERA_NO;
import static shiqichan.singinapp.Config.DELAY_INTERVAL_CHECK;
import static shiqichan.singinapp.Config.DELAY_START_CHECK;
import static shiqichan.singinapp.Config.TAG;
import static shiqichan.singinapp.Utils.openOrCloseScreen;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

public class SignInAppView extends FrameLayout implements
		SurfaceHolder.Callback, View.OnLayoutChangeListener, PreviewCallback {

	LooperThread looperThread;// 用于检查镜头数据，是否开启屏幕（有人的时候），开启屏幕后间隔抽取图像

	VideoView videoView;// 显示摄像头图像

	Camera camera;// 摄像头硬件对象

	Camera.Size currentSize;// 适合当前设备的预览图像尺寸

	PersonDetector detector;

	Window window;

	public SignInAppView(Context context, ViewGroup rootView) {
		super(context);

		detector = new PersonDetector();
		rootView.addOnLayoutChangeListener(this);// 当外部视图布局完毕后再加载

		window = ((Activity) context).getWindow();
	}

	public void resume() {
		looperThread = new LooperThread();
		looperThread.start();
	}

	public void pause() {
		looperThread.mHandler.post(new Runnable() {
			@Override
			public void run() {
				Looper.myLooper().quit();
			}
		});
	}

	private void computeVideoSize(View rootView) {
		Camera camera = Camera.open(CAMERA_NO);
		camera.setDisplayOrientation(90);

		Camera.Parameters parameters = camera.getParameters();

		Camera.Size size = null;
		int minDelta = Integer.MAX_VALUE;

		for (Camera.Size s : parameters.getSupportedPreviewSizes()) {
			// Log.d(TAG, "compute video size, width: " + s.height +
			// ", height: "
			// + s.width);
			if (s.width == rootView.getHeight()
					&& s.height == rootView.getWidth()) {// 找到正好屏幕匹配的尺寸
				size = s;
				break;
			}

			int currentDelta = Math.abs(s.width + s.height
					- rootView.getHeight() - rootView.getWidth());
			// Log.d(TAG, "currentDelta: " + currentDelta);

			if (currentDelta < minDelta) {
				minDelta = currentDelta;
				size = s;
			}
		}

		currentSize = size;
		detector.setPreviewSize(size.height, size.width);// 因为是竖版的
		camera.release();

		// Log.d(TAG, "currentSize, w: " + currentSize.width + ", h: "
		// + currentSize.height);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "surface changed.");

		if (width > height) {// 如果不是竖屏情况下，啥也不做
			return;
		}

		Camera.Parameters params = camera.getParameters();
		params.setPreviewSize(currentSize.width, currentSize.height);

		camera.setParameters(params);
		camera.setDisplayOrientation(90);// 摄像头默认是横屏的，竖屏需要旋转一下

		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		camera.startPreview();
		startCheckLoop();
	}

	private void startCheckLoop() {
		getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "start check..");
				checkLoop();
			}
		}, DELAY_START_CHECK);
	}

	private void checkLoop() {
		if (!looperThread.running) {// 如果looper线程不再运行，就不发了
			return;
		}

		looperThread.mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, ">> check..");
				camera.setOneShotPreviewCallback(SignInAppView.this);
				checkLoop();
			}
		}, DELAY_INTERVAL_CHECK);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open(CAMERA_NO);
		Log.d(TAG, "surface create.");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.release();
		Log.d(TAG, "surface destroyed.");
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right,
			int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		v.removeOnLayoutChangeListener(this);
		Log.d(TAG, ">>w,h: " + v.getWidth() + ", " + v.getHeight());

		computeVideoSize(v);

		setBackgroundColor(Color.BLUE);

		ViewGroup rootView = (ViewGroup) v;
		ViewGroup.LayoutParams params = new LayoutParams(rootView.getWidth(),
				rootView.getHeight());
		rootView.addView(this, params);

		videoView = new VideoView(getContext());
		videoView.getHolder().addCallback(this);

		if (rootView.getHeight() != currentSize.width) {
			//如果长宽不匹配preview尺寸，将有一部分高度（底部）不显示，比如xoom
			// params=new LayoutParams(800,1422);
			float currentHeight = currentSize.width * rootView.getWidth()
					/ currentSize.height;
//			Log.d(TAG, "current height: " + currentHeight);
			params = new LayoutParams(rootView.getWidth(), (int) currentHeight);
			addView(videoView, params);
		} else {
			addView(videoView);
		}
	}

	class VideoView extends SurfaceView {

		public VideoView(Context context) {
			super(context);
		}

	}

	class LooperThread extends Thread {
		public Handler mHandler;

		public boolean running;

		@Override
		public void run() {
			running = true;
			Looper.prepare();
			mHandler = new Handler();
			Looper.loop();
			running = false;
		}
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Size size = camera.getParameters().getPreviewSize();
		detector.setData(data);

		openOrCloseScreen(window, detector.hasPerson());
	}

}
