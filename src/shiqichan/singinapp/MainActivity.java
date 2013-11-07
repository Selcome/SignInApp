package shiqichan.singinapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import static shiqichan.singinapp.Config.*;

public class MainActivity extends Activity implements
		View.OnLayoutChangeListener {

	

	RelativeLayout rootView;
	
	SignInAppView appView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		rootView = (RelativeLayout) findViewById(R.id.rootView);
		rootView.addOnLayoutChangeListener(this);
		

		appView=new SignInAppView(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "activity resume..");
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right,
			int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		v.removeOnLayoutChangeListener(this);
		Log.d(TAG,
				"layout change, width: " + v.getWidth() + ", height: "
						+ v.getHeight());
		appView.addToRootView(rootView);
	}

}
