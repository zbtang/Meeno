package com.Sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import noteObject.Tab;

import Utils.Meta;
import Utils.SQLiteUtils;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class SmartyActivity extends ActivityGroup {
	/** Called when the activity is first created. */
	ImageButton addTabButton;
	static TabHost tabHostMain;
	int width;
	int heigth;
	int backUptimes = 0;
	long firstUpTime;
	long secondUpTime;
	TabSpec tabSpec;
	RelativeLayout relativeLayoutMain;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		findViews();
		tabHostMain.setup(this.getLocalActivityManager());
		bindListener(this);
		tabSpec = tabHostMain
				.newTabSpec("All")
				.setIndicator(createIndicatorView(this, tabHostMain, "All", 0))
				.setContent(
						new Intent(SmartyActivity.this, GridviewActivity.class));
		tabHostMain.addTab(tabSpec);
		initView(this);
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

	private void initView(Context context) {

		List<Tab> tabs = SQLiteUtils.getTabList(context);
		for (int i = 0; i < tabs.size(); i++) {
			Intent intent = new Intent(context, GridviewActivity.class);
			intent.putExtra(Meta.TABLE_NOTE_COLUMN_TABID, tabs.get(i)
					.getTabID());
			tabSpec = tabHostMain
					.newTabSpec(tabs.get(i).getTabName())
					.setIndicator(
							createIndicatorView(context, tabHostMain,
									tabs.get(i).getTabName(), tabs.get(i)
											.getTabID())).setContent(intent);
			tabHostMain.addTab(tabSpec);
		}

	}

	private void bindListener(final Context context) {
		addTabButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				View addTabView = View.inflate(context,
						R.layout.add_tab_dialogue, null);
				final EditText editText = (EditText) addTabView
						.findViewById(R.id.tabNameEditText);
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Please input the tab's name:");
				builder.setView(addTabView);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								String tabNameString = editText.getText()
										.toString();
								if (tabNameString != null
										&& !tabNameString.equals("")) {
									Tab tab = new Tab(tabNameString, null);
									int tabID = SQLiteUtils
											.addTab(context, tab);
									Intent intent = new Intent(
											SmartyActivity.this,
											GridviewActivity.class);
									intent.putExtra(
											Meta.TABLE_NOTE_COLUMN_TABID, tabID);
									tabSpec = tabHostMain
											.newTabSpec(tabNameString)
											.setIndicator(
													createIndicatorView(
															context,
															tabHostMain,
															tabNameString,
															tabID))
											.setContent(intent);
									tabHostMain.addTab(tabSpec);

								} else {
									Toast.makeText(context,
											"Tab name can't be empty!",
											Toast.LENGTH_SHORT).show();
								}

							}
						});
				builder.setNegativeButton("Cancel", null);
				AlertDialog addTabDialog = builder.create();
				addTabDialog.show();
			}
		});

	}

	private void findViews() {
		relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLay_main);
		tabHostMain = (TabHost) findViewById(android.R.id.tabhost);
		addTabButton = (ImageButton) findViewById(R.id.addTab);
	}

	private View createIndicatorView(Context context, TabHost tabHost,
			String title, int tabID) {
		View indicatorView = LayoutInflater.from(context).inflate(
				R.layout.tab_item, null);
		MyAdapter myAdapter = new MyAdapter(context, title, tabID);
		ViewPager viewPager = (ViewPager) indicatorView
				.findViewById(R.id.pager);
		viewPager.setAdapter(myAdapter);
		viewPager.setCurrentItem(1);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				System.out.println("onPageSelected:" + arg0);
				switch (arg0) {
				case 0:
					Toast.makeText(getApplicationContext(),
							"Click to rename this tab!", 1000).show();
					break;
				case 2:
					Toast.makeText(getApplicationContext(),
							"Click to delete this tab!", 1000).show();
					break;
				}
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});
		return indicatorView;
	}

	public static class MyAdapter extends PagerAdapter {

		String tabName;
		Context context;
		private ArrayList<View> views;
		int tabID;
		ViewPager pager;

		public MyAdapter(Context contextC, String tabNameC, int tabIDC) {
			this.tabName = tabNameC;
			this.context = contextC;
			this.tabID = tabIDC;
			View view0 = View.inflate(context, R.layout.button_delete, null);
			Button deleteButton = (Button) view0
					.findViewById(R.id.deleteTabButton);
			deleteButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					try {
						SQLiteUtils.deleteTabByTabID(context, tabID);
						View view = (View) pager.getParent();
						view.setVisibility(View.GONE);
					} catch (Exception e) {
						System.out.println();
					}

				}
			});
			View view1 = View.inflate(context, R.layout.text_tabname, null);
			final TextView textView = (TextView) view1
					.findViewById(R.id.textView1);
			textView.setText(tabName);
			textView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					System.out.println("pager'parrent:"
							+ pager.getParent().toString());
					System.out.println("pager'parrent'parrent:"
							+ pager.getParent().getParent().toString());
					tabHostMain.setCurrentTabByTag(tabName);
				}
			});
			View view2 = View.inflate(context, R.layout.button_rename, null);
			Button renameTabButton = (Button) view2
					.findViewById(R.id.renameTabButton);
			renameTabButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					showRenameDialog();
				}

				private void showRenameDialog() {
					View addTabView = View.inflate(context,
							R.layout.add_tab_dialogue, null);
					final EditText editText = (EditText) addTabView
							.findViewById(R.id.tabNameEditText);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setTitle("Please input new tab's name:");
					builder.setView(addTabView);
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface arg0,
										int arg1) {
									String newTabNameString = editText
											.getText().toString();
									if (!newTabNameString.equals("")) {
										Tab tab = new Tab(tabID,
												newTabNameString, null);
										SQLiteUtils.updateTab(context, tab);
										textView.setText(newTabNameString);
										tabName = newTabNameString;
										pager.setCurrentItem(1);

									} else {
										Toast.makeText(context,
												"Tab name can't be empty!",
												Toast.LENGTH_SHORT).show();
									}

								}
							});
					builder.setNegativeButton("Cancel", null);
					builder.show();

				}
			});
			views = new ArrayList<View>();
			views.add(view2);
			views.add(view1);
			views.add(view0);
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public void startUpdate(View container) {
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			View v = views.get(position);
			pager = (ViewPager) container;
			pager.addView(v);
			return v;
		}

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_UP) {
			backUptimes++;
			if (backUptimes % 2 == 1) {
				Toast.makeText(getApplicationContext(),
						"One more back will exit!", Toast.LENGTH_LONG).show();
				firstUpTime = event.getEventTime();
			} else {
				secondUpTime = event.getEventTime();
				if ((secondUpTime - firstUpTime) > 3000) {

					Toast.makeText(getApplicationContext(),
							"One more back will exit!", Toast.LENGTH_SHORT)
							.show();
					backUptimes = 1;
					firstUpTime = event.getEventTime();
				} else {
					SmartyActivity.this.getLocalActivityManager()
							.getCurrentActivity().finish();
					SmartyActivity.this.finish();
				}
			}

		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addNote:
			Intent intent = new Intent(SmartyActivity.this,
					NoteEditActivity.class);
			intent.putExtra("msg", "");
			SmartyActivity.this.startActivity(intent);
			SmartyActivity.this.finish();
			break;
		case R.id.about:

			AlertDialog.Builder builder = new AlertDialog.Builder(
					SmartyActivity.this);
			builder.setTitle("About");
			builder.setPositiveButton("OK", null);
			builder.setMessage(Meta.ABOUT);
			builder.show();

			break;
		default:
			break;
		}
		return false;
	}
}