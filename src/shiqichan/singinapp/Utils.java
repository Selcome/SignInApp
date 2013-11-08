package shiqichan.singinapp;

import android.view.Window;
import android.view.WindowManager;

public class Utils {
	public static void openOrCloseScreen(Window window, boolean open) {
		WindowManager.LayoutParams params = window.getAttributes();
		params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		if(open){
			params.screenBrightness = 255;	
		}else{
			params.screenBrightness = 1f / 255f;
		}
		window.setAttributes(params);
	}
}
