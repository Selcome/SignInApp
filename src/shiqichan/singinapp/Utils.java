package shiqichan.singinapp;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.view.Window;
import android.view.WindowManager;

public class Utils {

	public static Bitmap rotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.setScale(-1, 1);
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, true);
	}

	public static Bitmap convertYuvDataToBitmap(byte[] data, Camera.Size size) {
		YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width,
				size.height, null);
		Rect rectangle = new Rect();
		rectangle.bottom = size.height;
		rectangle.top = 0;
		rectangle.left = 0;
		rectangle.right = size.width;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		image.compressToJpeg(rectangle, 100, out);

		byte[] imageBytes = out.toByteArray();
		return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
	}

	public static void openOrCloseScreen(Window window, boolean open) {
		WindowManager.LayoutParams params = window.getAttributes();
		params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		if (open) {
			params.screenBrightness = 255;
		} else {
			params.screenBrightness = 1f / 255f;
		}
		window.setAttributes(params);
	}
}
