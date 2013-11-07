package shiqichan.singinapp;

import static shiqichan.singinapp.Config.*;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class SignInAppView extends FrameLayout implements
		SurfaceHolder.Callback {

	SurfaceView videoView;

	Camera camera;

	RelativeLayout rootView;

	public SignInAppView(Context context) {
		super(context);
		videoView = new SurfaceView(context);
		videoView.getHolder().addCallback(this);
	}

	public void resume() {

	}

	public void pause() {

	}

	protected void addToRootView(RelativeLayout rootView) {
		Log.d(TAG, "add to rootView");
		this.rootView = rootView;
		computeVideoSize();
		rootView.addView(videoView);
	}

	private void computeVideoSize() {
		Camera camera = Camera.open(CAMERA_NO);
		camera.setDisplayOrientation(90);

		Camera.Parameters parameters = camera.getParameters();

		Camera.Size size = null;
		int minDelta = Integer.MAX_VALUE;

		for (Camera.Size s : parameters.getSupportedPreviewSizes()) {
			Log.d(TAG, "compute video size, width: " + s.height + ", height: "
					+ s.width);
			if (s.width == rootView.getHeight()
					&& s.height == rootView.getWidth()) {// 找到正好屏幕匹配的尺寸
				size = s;
				break;
			}

			int currentDelta = Math.abs(s.width + s.height
					- rootView.getHeight() - rootView.getWidth());
			Log.d(TAG, "currentDelta: " + currentDelta);

			if(currentDelta<minDelta){
				minDelta=currentDelta;
				size=s;
			}

		}

		if (size != null) {
			Log.d(TAG, "results >> compute video size, width: " + size.height
					+ ", height: " + size.width);
		}

		camera.release();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "surface changed.");
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

}
