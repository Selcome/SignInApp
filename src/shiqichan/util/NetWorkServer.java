package shiqichan.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NetWorkServer {

	private static NetWorkServer instance = new NetWorkServer();
	
	private Context ctx;

	private NetWorkServer() {
	}

	public static NetWorkServer getInstance() {
		return instance;
	}

	public void postNetWork(Context context,String url, RequestParams params) {
		ctx=context;
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				 ToastUtil.showMessage(ctx,
				 "网络异常:" + arg0.getMessage(), Toast.LENGTH_SHORT);
			}
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onStart() {
				Log.e("person_detector", "-----***********************************---------");
				super.onStart();
				ProgressDialog.show(ctx, // context 
					    "", // title 
					    "正在上传，请稍等...", // message 
					    true); //进度是否是不确定的，这只和创建进度条有关 
			}

			@Override
			public void onSuccess(String arg0) {
				Log.e("person_detector", "---------------------------------------------------");
				ToastUtil.showMessage(ctx,
						 "发送成功", Toast.LENGTH_SHORT);
				 Activity mActivity=(Activity)ctx;
				 mActivity.finish();
			}
		});
	}
}
