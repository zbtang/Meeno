package services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WidgetUpdateService extends Service {

	@Override
	public void onCreate() {
		System.out.println("packageName:" + this.getPackageName());
		super.onCreate();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

}
