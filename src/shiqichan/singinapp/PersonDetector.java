package shiqichan.singinapp;

//import android.util.Log;

public class PersonDetector {

	public boolean hasPerson() {
		return detect() > 0;
	}

	private native int detect();

	static {
		System.loadLibrary("PersonDetector");
	}
}
