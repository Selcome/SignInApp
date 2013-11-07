package shiqichan.singinapp;

import android.util.Log;

public class PersonDetector {

	private static final String TAG = "person_detector";

	public boolean hasPerson() {
		Log.d(TAG, "COUNT: " + detect());
		return detect() > 0;
	}

	private native int detect();

	static {
		System.loadLibrary("PersonDetector");
	}
}
