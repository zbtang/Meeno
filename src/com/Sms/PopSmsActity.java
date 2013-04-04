package com.Sms;

import org.w3c.dom.Text;

import com.umeng.analytics.MobclickAgent;

import Utils.SMSnoteUtils;
import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PopSmsActity extends Activity {

	TextView contactName;
	TextView smsContent;
	ImageView contactIcon;
	ImageButton reply;
	ImageView saveAsNote;

	ImageView delete;
	EditText replyContent;
	ImageButton go;
	LinearLayout linearLayout1;
	LinearLayout opreationLayout;
	LinearLayout replyLayout;
	RelativeLayout relativeLayout;

	String smsContentString;
	String contactNameString;
	Bitmap contactIconBitmap;
	String senderNumberString;

	String replyContentString;
	int width;
	int height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pop_sms);

		smsContentString = getIntent().getStringExtra("message");
		contactNameString = getIntent().getStringExtra("senderName");
		senderNumberString = getIntent().getStringExtra("senderNumber");
		contactIconBitmap = SMSnoteUtils.getCotactIconByNumber(this,
				senderNumberString);

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

	private void initView() {
		if (contactIconBitmap != null) {
			contactIcon.setImageBitmap(contactIconBitmap);
		}
		contactName.setText(contactNameString);
		smsContent.setText(smsContentString);

	}

	private void findViews() {
		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutPopSms);
		contactName = (TextView) findViewById(R.id.contactName);
		smsContent = (TextView) findViewById(R.id.smsContent);
		contactIcon = (ImageView) findViewById(R.id.contactIcon);
		reply = (ImageButton) findViewById(R.id.smsReply);
		saveAsNote = (ImageView) findViewById(R.id.savaASnote);
		delete = (ImageView) findViewById(R.id.delete);
		replyContent = (EditText) findViewById(R.id.replyContent);
		go = (ImageButton) findViewById(R.id.go);
		opreationLayout = (LinearLayout) findViewById(R.id.linearLayout2);
		replyLayout = (LinearLayout) findViewById(R.id.linearLayout3);
	}

	private void bindListener(final Context context) {

		reply.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Animation animation = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.popup_anmi1);
				opreationLayout.startAnimation(animation);
				opreationLayout.setVisibility(View.GONE);
				animation = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.popup_anmi2);
				replyLayout.startAnimation(animation);

				replyLayout.setVisibility(View.VISIBLE);
			}
		});
		saveAsNote.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(PopSmsActity.this,
						NoteEditActivity.class);
				intent.putExtra("msg", smsContentString);
				PopSmsActity.this.startActivity(intent);
				PopSmsActity.this.finish();
			}
		});
		delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Warning");
				builder.setMessage("Are you sure to delete the conversion?");
				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								SMSnoteUtils
										.deleteLatestSMS(getApplicationContext());
								PopSmsActity.this.finish();
							}
						});
				builder.setNegativeButton("No", null);
				builder.show();

			}
		});
		go.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String sendContent = replyContent.getText().toString();
				if (sendContent != null && !sendContent.equals("")) {
					sendMessage(senderNumberString, sendContent);
					replyLayout.setVisibility(View.GONE);
					opreationLayout.setVisibility(View.VISIBLE);
					replyContent.setText("");
				} else {
					Toast.makeText(context, "The content can't be empty!",
							Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	public void sendMessage(String number, String content) {
		String SENT = "SMS_SENT";
		PendingIntent pi = PendingIntent.getBroadcast(this, 0,
				new Intent(SENT), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(
							getBaseContext(),
							"Congratulations:The message has been sent successfully!)",
							Toast.LENGTH_LONG).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(),
							"Sorry:A generic failure happened!",
							Toast.LENGTH_LONG).show();
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(),
							"Sorry:You have no access to service now!",
							Toast.LENGTH_LONG).show();
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(),
							"Sorry:Null PDU failure happened!",
							Toast.LENGTH_LONG).show();
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(),
							"Sorry:Radio off failure happened! ",
							Toast.LENGTH_LONG).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		SmsManager sm = SmsManager.getDefault();
		sm.sendTextMessage(number, null, content, pi, null);
	}
}
