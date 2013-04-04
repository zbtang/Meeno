package receiver;

import com.Sms.R;

import Utils.Meta;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetReceiver extends AppWidgetProvider {
	private Context mContext;
	private AppWidgetManager mWidgetManager = null;
	private int[] mWidgetIds;
	private int mNoteNumber;
	private CharSequence mNote;
	RemoteViews mRemoteViews;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		setup(context, appWidgetIds);
		Intent broadCastIntent = new Intent(Meta.ACTION_NEXT_NOTE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				broadCastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mRemoteViews.setOnClickPendingIntent(R.id.widget_right, pendingIntent);
		mRemoteViews.setTextViewText(R.id.widget_text, "hjjjhdfjf");
		appWidgetManager.updateAppWidget(appWidgetIds, mRemoteViews);
	}

	private void setup(Context context, int[] appWidgetIds) {
		mContext = context;
		mWidgetManager = AppWidgetManager.getInstance(context);
		mWidgetIds = appWidgetIds;
		mRemoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (intent.getAction().equals(Meta.ACTION_NEXT_NOTE)) {
			System.out.println(intent.getAction());
			updateText(context);

		} else {

		}
	}

	private void updateText(Context context) {
		RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		mRemoteViews.setTextViewText(R.id.widget_text, "hello!");
		AppWidgetManager mWidgetManager = AppWidgetManager.getInstance(context);
		mWidgetManager.updateAppWidget(new ComponentName(context,
				WidgetReceiver.class), mRemoteViews);
	}
}
