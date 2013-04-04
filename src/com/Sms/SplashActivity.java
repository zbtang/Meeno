package com.Sms;

import myUI.MyAnimation;
import services.WidgetUpdateService;

import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.LinearLayout;

public class SplashActivity extends Activity {
	LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		startService(new Intent(this, WidgetUpdateService.class));
		new Handler().postDelayed(new Runnable() {

			public void run() {
				Intent intent = new Intent(SplashActivity.this,
						SmartyActivity.class);
				SplashActivity.this.startActivity(intent);
				SplashActivity.this.finish();
			}
		}, 1000);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
