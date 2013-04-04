package myUI;

import com.Sms.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class myTabWidget extends TabWidget {
	Resources resources;
	int widht;
	int height;

	public myTabWidget(Context context) {
		super(context);
		widht = dip2px(context, 60);
		height = dip2px(context, 40);
		resources = getResources();
		setOrientation(VERTICAL);

	}

	public myTabWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		widht = dip2px(context, 60);
		height = dip2px(context, 40);
		resources = getResources();
		setOrientation(VERTICAL);
	}

	public myTabWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		widht = dip2px(context, 60);
		height = dip2px(context, 40);
		resources = getResources();
		setOrientation(VERTICAL);
		ScrollView scrollView;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void addView(View child) {
		LayoutParams layoutParams = new LayoutParams(widht, height);
		layoutParams.setMargins(0, 1, 0, 1);
		child.setLayoutParams(layoutParams);
//		if (child.getParent() != null) {
//			ViewGroup group = (ViewGroup) child.getParent();
//			System.out.println(group.toString());
//			group.removeView(child);
//		}

		super.addView(child);

	}
}
