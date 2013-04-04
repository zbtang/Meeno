package Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import noteObject.Tab;
import noteObject.Note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteUtils {

	public static List<Tab> getTabList(Context context) {
		List<Tab> tabs = new ArrayList<Tab>();
		MySQliteHelper helper = new MySQliteHelper(context, Meta.DATABASE_NAME);
		SQLiteDatabase database = helper.getReadableDatabase();

		Cursor cursor = database.query(Meta.TABLE_TAB_NAME, null, null, null,
				null, null, null);

		if (cursor.moveToFirst()) {
			Tab tab = null;
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				tab = new Tab(cursor.getInt(cursor
						.getColumnIndex(Meta.TABLE_TAB_COLUMN_ID)),
						cursor.getString(cursor
								.getColumnIndex(Meta.TABLE_TAB_COLUMN_NAME)),
						cursor.getString(cursor
								.getColumnIndex(Meta.TABLE_TAB_COLUMN_KEYWORD)));
				tabs.add(tab);
			}

		}
		cursor.close();
		database.close();
		helper.close();

		return tabs;

	}

	public static List<Note> getNoteList(Context context) {

		List<Note> notes = new ArrayList<Note>();
		Cursor cursor;
		MySQliteHelper helper = new MySQliteHelper(context, Meta.DATABASE_NAME);
		SQLiteDatabase database = helper.getReadableDatabase();
		cursor = database.query(Meta.TABLE_NOTE_NAME, null, null, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			Note note = null;
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				if (cursor.getInt(cursor
						.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ISREMINDER)) == 1) {

					note = new Note(
							cursor.getInt(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ID)),
							cursor.getString(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_CONTENT)),

							cursor.getInt(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ISONDESK)) == 1,
							true,
							cursor.getInt(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_TABID)),
							new Date(
									cursor.getLong(cursor
											.getColumnIndex(Meta.TABLE_NOTE_COLUMN_TIME))));

				} else {
					note = new Note(
							cursor.getInt(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ID)),
							cursor.getString(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_CONTENT)),
							cursor.getInt(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ISONDESK)) == 1,
							false,
							cursor.getInt(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_TABID)),
							null);
				}
				notes.add(note);
			}

		}
		cursor.close();
		database.close();
		helper.close();
		return notes;

	}

	public static List<Note> getNoteListBytabID(Context context, int tabID) {
		List<Note> notes = new ArrayList<Note>();
		Cursor cursor = null;
		MySQliteHelper helper = new MySQliteHelper(context, Meta.DATABASE_NAME);
		SQLiteDatabase database = helper.getReadableDatabase();
		if (tabID >= 0) {

			cursor = database.query(Meta.TABLE_NOTE_NAME, null, "tabID=?",
					new String[] { String.valueOf(tabID) }, null, null, null);
		}

		if (cursor != null && cursor.moveToFirst()) {
			Note note = null;
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				if (cursor.getInt(cursor
						.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ISREMINDER)) == 1) {

					note = new Note(
							cursor.getInt(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ID)),
							cursor.getString(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_CONTENT)),

							cursor.getInt(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ISONDESK)) == 1,
							true,
							cursor.getInt(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_TABID)),
							new Date(
									cursor.getLong(cursor
											.getColumnIndex(Meta.TABLE_NOTE_COLUMN_TIME))));

				} else {
					note = new Note(
							cursor.getInt(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ID)),
							cursor.getString(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_CONTENT)),
							cursor.getInt(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ISONDESK)) == 1,
							false,
							cursor.getInt(cursor
									.getColumnIndex(Meta.TABLE_NOTE_COLUMN_TABID)),
							null);
				}
				notes.add(note);
			}

		}
		cursor.close();
		database.close();
		helper.close();
		return notes;

	}

	public static List<String> getAllTabName(Context context) {
		List<String> tabNames = new ArrayList<String>();
		MySQliteHelper helper = new MySQliteHelper(context, Meta.DATABASE_NAME);
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.query(Meta.TABLE_TAB_NAME,
				new String[] { Meta.TABLE_TAB_COLUMN_NAME }, null, null, null,
				null, null);

		if (cursor.moveToFirst()) {
			String tabName;
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				tabName = cursor.getString(cursor
						.getColumnIndex(Meta.TABLE_TAB_COLUMN_NAME));
				tabNames.add(tabName);

			}
		}
		cursor.close();
		database.close();
		helper.close();
		return tabNames;
	}

	public static List<String> getAllNoteContent(Context context) {
		List<String> noteContents = new ArrayList<String>();
		MySQliteHelper helper = new MySQliteHelper(context, Meta.DATABASE_NAME);
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.query(Meta.TABLE_NOTE_NAME,
				new String[] { Meta.TABLE_NOTE_COLUMN_CONTENT }, null, null,
				null, null, null);

		if (cursor.moveToFirst()) {
			String noteContent;
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				noteContent = cursor.getString(cursor
						.getColumnIndex(Meta.TABLE_NOTE_COLUMN_CONTENT));
				noteContents.add(noteContent);

			}
		}
		cursor.close();
		database.close();
		helper.close();
		return noteContents;
	}

	public static List<String> getAllNoteContentBytabID(Context context,
			int tabID) {
		List<String> noteContents = new ArrayList<String>();
		MySQliteHelper helper = new MySQliteHelper(context, Meta.DATABASE_NAME);
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.query(Meta.TABLE_NOTE_NAME,
				new String[] { Meta.TABLE_NOTE_COLUMN_CONTENT }, "tabID=?",
				new String[] { String.valueOf(tabID) }, null, null, null);

		if (cursor.moveToFirst()) {
			String noteContent;
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				noteContent = cursor.getString(cursor
						.getColumnIndex(Meta.TABLE_NOTE_COLUMN_CONTENT));
				noteContents.add(noteContent);

			}
		}
		cursor.close();
		database.close();
		helper.close();
		return noteContents;
	}

	public static int addTab(Context context, Tab tab) {
		int tabID;
		MySQliteHelper helper = new MySQliteHelper(context, Meta.DATABASE_NAME);
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(Meta.TABLE_TAB_COLUMN_NAME, tab.getTabName());
		contentValues.put(Meta.TABLE_TAB_COLUMN_KEYWORD, tab.getTabKeyword());
		tabID = (int) database.insert(Meta.TABLE_TAB_NAME, null, contentValues);
		database.close();
		helper.close();
		return tabID;

	}

	public static Note getNoteByContent(Context context, String content) {
		Note note = null;
		Cursor cursor = null;
		MySQliteHelper helper = new MySQliteHelper(context, Meta.DATABASE_NAME);
		SQLiteDatabase database = helper.getReadableDatabase();
		if (content != null) {

			cursor = database.query(Meta.TABLE_NOTE_NAME, null,
					Meta.TABLE_NOTE_COLUMN_CONTENT + "=?",
					new String[] { content }, null, null, null);
		}
		if (cursor != null && cursor.moveToFirst()) {

			if (cursor.getInt(cursor
					.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ISREMINDER)) == 1) {

				note = new Note(
						cursor.getInt(cursor
								.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ID)),
						cursor.getString(cursor
								.getColumnIndex(Meta.TABLE_NOTE_COLUMN_CONTENT)),

						cursor.getInt(cursor
								.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ISONDESK)) == 1,
						true, cursor.getInt(cursor
								.getColumnIndex(Meta.TABLE_NOTE_COLUMN_TABID)),
						new Date(cursor.getLong(cursor
								.getColumnIndex(Meta.TABLE_NOTE_COLUMN_TIME))));

			} else {
				note = new Note(
						cursor.getInt(cursor
								.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ID)),
						cursor.getString(cursor
								.getColumnIndex(Meta.TABLE_NOTE_COLUMN_CONTENT)),
						cursor.getInt(cursor
								.getColumnIndex(Meta.TABLE_NOTE_COLUMN_ISONDESK)) == 1,
						false, cursor.getInt(cursor
								.getColumnIndex(Meta.TABLE_NOTE_COLUMN_TABID)),
						null);
			}
		}
		cursor.close();
		database.close();
		helper.close();
		return note;
	}

	public static void updateNote(Context context, Note note) {
		MySQliteHelper helper = new MySQliteHelper(context, Meta.DATABASE_NAME);
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(Meta.TABLE_NOTE_COLUMN_CONTENT, note.getContent());
		contentValues.put(Meta.TABLE_NOTE_COLUMN_ISONDESK,
				note.getIsRemindedOndesk());
		contentValues.put(Meta.TABLE_NOTE_COLUMN_ISREMINDER,
				note.getIsReminder());
		contentValues.put(Meta.TABLE_NOTE_COLUMN_TABID, note.getTabID());
		if (note.getTimeToRemind() != null) {
			contentValues.put(Meta.TABLE_NOTE_COLUMN_TIME, note
					.getTimeToRemind().getTime());
		}

		database.update(Meta.TABLE_NOTE_NAME, contentValues,
				Meta.TABLE_NOTE_COLUMN_ID + "=?",
				new String[] { String.valueOf(note.getNoteID()) });
		database.close();
		helper.close();
	}

	public static int addNote(Context context, Note note) {
		int noteID;
		MySQliteHelper helper = new MySQliteHelper(context, Meta.DATABASE_NAME);
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(Meta.TABLE_NOTE_COLUMN_CONTENT, note.getContent());
		contentValues.put(Meta.TABLE_NOTE_COLUMN_ISONDESK,
				note.getIsRemindedOndesk());
		contentValues.put(Meta.TABLE_NOTE_COLUMN_ISREMINDER,
				note.getIsReminder());
		contentValues.put(Meta.TABLE_NOTE_COLUMN_TABID, note.getTabID());
		if (note.getTimeToRemind() != null) {
			contentValues.put(Meta.TABLE_NOTE_COLUMN_TIME, note
					.getTimeToRemind().getTime());
		}

		noteID = (int) database.insert(Meta.TABLE_NOTE_NAME, null,
				contentValues);
		database.close();
		helper.close();
		return noteID;
	}

	public static void deleteTabByTabID(Context context, int tabID) {
		if (tabID > 0) {
			MySQliteHelper helper = new MySQliteHelper(context,
					Meta.DATABASE_NAME);
			SQLiteDatabase database = helper.getWritableDatabase();
			database.delete(Meta.TABLE_TAB_NAME, Meta.TABLE_TAB_COLUMN_ID
					+ "=?", new String[] { String.valueOf(tabID) });
			database.close();
			helper.close();
		}

	}

	public static void updateTab(Context context, Tab tab) {
		int tabID = tab.getTabID();
		if (tabID > 0) {

			MySQliteHelper helper = new MySQliteHelper(context,
					Meta.DATABASE_NAME);
			SQLiteDatabase database = helper.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put(Meta.TABLE_TAB_COLUMN_NAME, tab.getTabName());
			contentValues.put(Meta.TABLE_TAB_COLUMN_KEYWORD,
					tab.getTabKeyword());
			database.update(Meta.TABLE_TAB_NAME, contentValues,
					Meta.TABLE_TAB_COLUMN_ID + "=?",
					new String[] { String.valueOf(tabID) });
			database.close();
			helper.close();
		}
	}

	public static void deleteNoteById(Context context, int noteId) {
		if (noteId > 0) {
			MySQliteHelper helper = new MySQliteHelper(context,
					Meta.DATABASE_NAME);
			SQLiteDatabase database = helper.getWritableDatabase();
			database.delete(Meta.TABLE_NOTE_NAME, Meta.TABLE_NOTE_COLUMN_ID
					+ "=?", new String[] { String.valueOf(noteId) });
			database.close();
			helper.close();
		}
	}
}
