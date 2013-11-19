package shiqichan.singinapp;

import static shiqichan.singinapp.Config.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import shiqichan.util.NetWorkServer;

import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

@SuppressLint("HandlerLeak")
public class PersonActivty extends Activity {

	private ImageView photoImageView;

	private Button pressButton;
	
	private Spinner spinner;
	
	private ArrayAdapter<String> adapter;  

	private Timer mTimer = null;

	@SuppressWarnings("unused")
	private TimerTask mTimerTask = null;

	private Handler handler;
	 
	private String[] students=new String[]{
			"吴逸秋",
			"王军",
			"曹磊",
			"马如军",
			"王云超",
			"袁首超",
			"韩冲",
			"梁捷",
			"肖媛媛",
			"李震"
	};
	
	private String name=students[0];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.person_activty);

		photoImageView = (ImageView) findViewById(R.id.personImage);
		pressButton = (Button) findViewById(R.id.pressButton);
		spinner = (Spinner) findViewById(R.id.studentSpinner);  
		//将可选内容与ArrayAdapter连接起来  
		  adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,students);
		//设置下拉列表的风格  
		  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		  
		  //将adapter 添加到spinner中  
		  spinner.setAdapter(adapter); 
		//添加事件Spinner事件监听    
		  spinner.setOnItemSelectedListener(new SpinnerSelectedListener()); 
		  //设置默认值 
		  spinner.setVisibility(View.VISIBLE); 

		String fileImagePath = this.getIntent().getStringExtra("fileImagePath");
		File file = new File(fileImagePath);
		if (file.exists()) {
			Bitmap bm = BitmapFactory.decodeFile(fileImagePath);
			// 将图片显示到ImageView中
			photoImageView.setImageBitmap(bm);
		}
		init();
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startTimer();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		stopTimer();
	}

	private void init() {
		// TODO Auto-generated method stub
		pressButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				stopTimer();
				RequestParams requestParams = new RequestParams();
				requestParams.put("command", "uploadPic");
				requestParams.put("name", name);
				try {
					String fileImagePath = getIntent().getStringExtra(
							"fileImagePath");
					File file = new File(fileImagePath);
					requestParams.put("pic", file);
					NetWorkServer.getInstance().postNetWork(PersonActivty.this,
							Url, requestParams);
				} catch (FileNotFoundException e) {
				}
			}
		});

		// 定义Handler

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				finish();
			}
		};
	}

	private void startTimer() {
		if (mTimer == null) {
			mTimer = new Timer();
		}
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				stopTimer();
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		}, 10 * 1000, 10);

	}

	private void stopTimer() {

		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

	}
	
	//使用数组形式操作  
    class SpinnerSelectedListener implements OnItemSelectedListener{  

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			name=students[arg2];
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}  
    }  
}
