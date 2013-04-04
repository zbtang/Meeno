package com.Sms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import noteObject.Note;
import noteObject.Tab;

import myUI.myEditText;
import Utils.Meta;
import Utils.SQLiteUtils;
import android.R.bool;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class NoteEditActivity extends Activity {
	ImageButton setTimeButton;
	LinearLayout timePickerLinear;
	TextView dateText;
	TextView timeText;
	myEditText myEditText;
	Button deleteNoteButton;
	Button cancelTimerButton;
	ImageButton saveButton;
	private static final String BC_ACTION = "com.Sms.action.BC_ACTION";
	String noteString;
	int tabID;
	Note note = null;
	Date date = null;
	public static final int DELAY_MINUTE = 20;

	int deleteTimes = 0;
	long firstDeleteTime;
	long secondDeleteTime;

	int backUptimes = 0;
	long firstUpTime;
	long secondUpTime;
	boolean isNewNote = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		noteString = getIntent().getStringExtra("msg");
		tabID = getIntent().getIntExtra(Meta.TABLE_NOTE_COLUMN_TABID, 0);
		if (tabID != 0) {
			note = (Note) getIntent().getSerializableExtra(Meta.KEY_NOTE);
			isNewNote = false;

		}
		setContentView(R.layout.note_edit);
		findViews();
		initView();
		bindListener(this);
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

	private void findViews() {
		setTimeButton = (ImageButton) findViewById(R.id.imageButton1);
		timePickerLinear = (LinearLayout) findViewById(R.id.dateLinearLay);
		dateText = (TextView) findViewById(R.id.dateText);
		timeText = (TextView) findViewById(R.id.timeText);
		myEditText = (myUI.myEditText) findViewById(R.id.editText_noteEdit);
		saveButton = (ImageButton) findViewById(R.id.saveButton);
		deleteNoteButton = (Button) findViewById(R.id.deleteNoteBtn);
		cancelTimerButton = (Button) findViewById(R.id.cancelTimer);

	}

	private void bindListener(final Context context) {
		cancelTimerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setTimeButton.setVisibility(View.VISIBLE);
				timePickerLinear.setVisibility(View.GONE);
			}
		});
		setTimeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setTimeButton.setVisibility(View.GONE);
				timePickerLinear.setVisibility(View.VISIBLE);
				date = new Date(System.currentTimeMillis() + 1000 * 60
						* DELAY_MINUTE);
				dateText.setText((date.getYear() + 1900) + "年"
						+ (date.getMonth() + 1) + "月" + date.getDate() + "日");
				timeText.setText(date.getHours() + "时" + date.getMinutes()
						+ "分");
			}
		});
		dateText.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(
						context, new DatePickerDialog.OnDateSetListener() {

							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								dateText.setText(year + "年" + (monthOfYear + 1)
										+ "月" + dayOfMonth + "日");
								date.setYear(year - 1900);
								date.setMonth(monthOfYear);
								date.setDate(dayOfMonth);
							}
						}, date.getYear() + 1900, date.getMonth(), date
								.getDate());
				datePickerDialog.show();
			}
		});
		timeText.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				TimePickerDialog timePickerDialog = new TimePickerDialog(
						context, new TimePickerDialog.OnTimeSetListener() {

							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								timeText.setText(hourOfDay + "时" + minute + "分");
								date.setHours(hourOfDay);
								date.setMinutes(minute);
							}
						}, date.getHours(), date.getMinutes(), false);
				timePickerDialog.show();
			}
		});
		saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String contentString = myEditText.getText().toString();
				System.out.println(contentString);
				if (!contentString.equals("") && contentString != null) {
					if (tabID == 0) {
						showSaveDialog(context, v);
					} else {
						setNote(tabID);
						SQLiteUtils.updateNote(context, note);
						Toast.makeText(context, "saved!", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"The content can't be empty!", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		deleteNoteButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				deleteTimes++;
				if (deleteTimes % 2 == 1) {
					Toast.makeText(getApplicationContext(),
							"One more click will delete this note!", 1000)
							.show();
					firstDeleteTime = System.currentTimeMillis();
				} else {
					secondDeleteTime = System.currentTimeMillis();
					if ((secondDeleteTime - firstDeleteTime) > 3000) {

						Toast.makeText(getApplicationContext(),
								"One more click will delete this note!", 1000)
								.show();
						deleteTimes = 1;
						firstDeleteTime = System.currentTimeMillis();
					} else {
						SQLiteUtils.deleteNoteById(context, note.getNoteID());
						Intent intent = new Intent(NoteEditActivity.this,
								SmartyActivity.class);
						NoteEditActivity.this.startActivity(intent);
						NoteEditActivity.this.finish();
					}
				}
			}
		});
	}

	private void initView() {
		if (tabID != 0 && note != null && note.getIsReminder() == true) {
			setTimeButton.setVisibility(View.GONE);
			timePickerLinear.setVisibility(View.VISIBLE);
			date = note.getTimeToRemind();
			if (date != null) {
				dateText.setText(date.getYear() + 1900 + "年"
						+ (date.getMonth() + 1) + "月" + date.getDate() + "日");
				timeText.setText(date.getHours() + "时" + date.getMinutes()
						+ "分");
			}
			myEditText.setText(noteString);
		} else {
			setTimeButton.setVisibility(View.VISIBLE);
			timePickerLinear.setVisibility(View.GONE);
			if (noteString != null) {
				myEditText.setText(noteString);
			}
		}
		if (isNewNote) {
			deleteNoteButton.setVisibility(View.GONE);
		} else {
			deleteNoteButton.setVisibility(View.VISIBLE);
		}

	}

	private void setRepeat(Context context, int interval, String msg) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(ALARM_SERVICE);
		Intent intent = new Intent();
		intent.setAction(BC_ACTION);
		intent.putExtra("msg", msg);

		final PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, intent, 0);
		final long time = System.currentTimeMillis();

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, interval,
				pendingIntent);
	}

	private void setTimeToRemind(Context context, long timeToSet, String msg) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(ALARM_SERVICE);

		Intent intent = new Intent();
		intent.setAction(BC_ACTION);
		intent.putExtra("msg", msg);
		final PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, intent, 0);
		alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSet, pendingIntent);
	}

	private void showSaveDialog(Context context, View v) {

		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
		final List<Tab> tabs = SQLiteUtils.getTabList(context);

		for (int i = 0; i < tabs.size(); i++) {
			HashMap<String, String> tempHashMap = new HashMap<String, String>();
			tempHashMap.put("tabName", tabs.get(i).getTabName());
			arrayList.add(tempHashMap);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(context, arrayList,
				R.layout.save_popview_item, new String[] { "tabName" },
				new int[] { R.id.save_popview_text });
		final Dialog dialog = new Dialog(NoteEditActivity.this,
				R.style.dialog_stytle);

		View saveDialogView = View.inflate(getApplicationContext(),
				R.layout.save_popview, null);
		ListView listView = (ListView) saveDialogView
				.findViewById(android.R.id.list);
		listView.setAdapter(simpleAdapter);

		Window window = dialog.getWindow();
		window.setGravity(Gravity.LEFT | Gravity.TOP);
		android.view.WindowManager.LayoutParams layoutParams = window
				.getAttributes();
		layoutParams.x = v.getLeft();
		layoutParams.y = ((View) v.getParent()).getTop() - tabs.size() * 31;
		window.setAttributes(layoutParams);
		dialog.setContentView(saveDialogView, layoutParams);
		dialog.show();
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(), "saved!",
						Toast.LENGTH_SHORT).show();
				note = new Note(myEditText.getText().toString());
				setNote(tabs.get(position).getTabID());
				SQLiteUtils.addNote(getApplicationContext(), note);
				dialog.cancel();
			}
		});

	}

	private void setNote(int tabID) {
		if (date == null || timePickerLinear.VISIBLE == View.GONE) {
			note.setTabID(tabID);
			note.setContent(myEditText.getText().toString());
			note.setIsReminder(false);
			note.setTimeToRemind(null);
		} else {
			note.setTabID(tabID);
			note.setContent(myEditText.getText().toString());
			note.setIsReminder(true);
			note.setTimeToRemind(date);
			setTimeToRemind(getApplicationContext(), note.getTimeToRemind()
					.getTime(), note.getContent());
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
					Intent intent = new Intent(NoteEditActivity.this,
							SmartyActivity.class);
					NoteEditActivity.this.startActivity(intent);
					NoteEditActivity.this.finish();
				}
			}

		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onBackPressed() {
	}
}
