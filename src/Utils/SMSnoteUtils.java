package Utils;

import java.io.InputStream;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.Contacts.PeopleColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.gsm.SmsMessage;

public class SMSnoteUtils {

	public static final SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		if (messages == null) {
			return null;
		}
		if (messages.length == 0) {
			return null;
		}

		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}
		return msgs;
	}

	public static String getContactNameByNumber(Context context, String number) {
		String name = number;
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));
		Cursor pCur = resolver.query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (pCur.moveToFirst()) {
			name = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			pCur.close();
		}
		return name;
	}

	public static Long getPersonIdFromPhoneNumber(Context context,
			String address) {
		if (address == null)
			return null;

		Cursor cursor = context.getContentResolver().query(
				Uri.withAppendedPath(Contacts.Phones.CONTENT_FILTER_URL,
						Uri.encode(address)),
				new String[] { Contacts.Phones.PERSON_ID,
						PeopleColumns.DISPLAY_NAME }, null, null, null);

		if (cursor != null) {
			try {
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					Long contactId = cursor.getLong(0);
					return contactId;
				}
			} finally {
				cursor.close();
			}
		}

		return null;
	}

	public static Bitmap getCotactIconByNumber(Context context, String number) {
		Bitmap icon = null;
		Long id = getPersonIdFromPhoneNumber(context, number);
		if (id != null) {
			icon = getPhoto(context, id);
		}
		return icon;
	}

	public static void deleteSMS(Context context, String id) {
		try {
			ContentResolver contentResolver = context.getContentResolver();
			Uri uriSMS = Uri.withAppendedPath(Uri.parse("content://mms-sms/"),
					id);
			Cursor cursor = contentResolver.query(uriSMS, new String[] { "_id",
					"thread_id" }, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				do {
					long threadId = cursor.getLong(1);
					contentResolver.delete(
							Uri.parse("content://mms-sms/" + threadId), null,
							null);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void deleteLatestSMS(Context context) {
		String inboxContentStri = "content://sms/inbox/";
		Uri inboxUri = Uri.parse(inboxContentStri);
		Cursor cursor = context.getContentResolver().query(inboxUri, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			int id = cursor.getInt(0);
			int thread_id = cursor.getInt(1);

			context.getContentResolver().delete(
					Uri.parse("content://sms/conversations/" + thread_id),
					null, null);

		}

	}

	public static Bitmap getPhoto(Context context, long contactId) {
		ContentResolver contentResolver = context.getContentResolver();
		Uri contactPhotoUri = Uri
				.parse("content://com.android.contacts/contacts/" + contactId);
		InputStream photoDataStream = ContactsContract.Contacts
				.openContactPhotoInputStream(contentResolver, contactPhotoUri);
		Bitmap photo = BitmapFactory.decodeStream(photoDataStream);
		return photo;
	}

}
