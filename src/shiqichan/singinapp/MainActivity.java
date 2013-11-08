package shiqichan.singinapp;

import static shiqichan.singinapp.Config.*;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

public class MainActivity extends Activity {

	SignInAppView appView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		ViewGroup rootView = (ViewGroup) findViewById(R.id.rootView);
		appView = new SignInAppView(this, rootView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		appView.resume();
		Log.d(TAG, "activity resume..");
	}
	
	@Override
	protected void onPause() {
		appView.pause();
		super.onPause();
		Log.d(TAG, "activity pause..");
	}
}
