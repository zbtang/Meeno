package receiver;

import com.Sms.NoteEditActivity;
import com.Sms.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String msg = intent.getStringExtra("msg");
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.logo_new, msg,
				System.currentTimeMillis());
		intent.putExtra("msg", msg);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context, NoteEditActivity.class);
		PendingIntent intentBack = PendingIntent.getActivity(context, 0,
				intent, 0);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.defaults = notification.DEFAULT_SOUND;
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.setLatestEventInfo(context, "Meeno", msg, intentBack);
		notification.vibrate = new long[] { 0, 200, 200, 6000, 600 };
		notificationManager.notify(1, notification);
	}
}
