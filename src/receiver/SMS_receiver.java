package receiver;

import com.Sms.PopSmsActity;

import Utils.SMSnoteUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;

public class SMS_receiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			handleSMSReceieved(context, intent);
		}
	}

	private void handleSMSReceieved(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			SmsMessage[] messages = SMSnoteUtils.getMessagesFromIntent(intent);
			@SuppressWarnings("deprecation")
			String text = messages[0].getMessageBody();
			String address = messages[0].getOriginatingAddress();
			String senderName = SMSnoteUtils.getContactNameByNumber(context,
					address);

			intent.putExtra("message", text);
			intent.putExtra("senderName", senderName);
			intent.putExtra("senderNumber", address);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setClass(context, PopSmsActity.class);
			context.startActivity(intent);
		}
	}

}
