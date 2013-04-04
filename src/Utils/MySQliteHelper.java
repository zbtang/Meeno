package Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQliteHelper extends SQLiteOpenHelper {
	private static final int version = 1;

	public MySQliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public MySQliteHelper(Context context, String name, int version) {

		this(context, name, null, version);
	}

	public MySQliteHelper(Context context, String name) {

		this(context, name, version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table tab(" +
				"_id INTEGER 	PRIMARY KEY AUTOINCREMENT," +
				"tabName 		varchar[40]," +
				"tabKeyword 	varchar[40])");
		db.execSQL("create table note(" +
				"_id 			INTEGER PRIMARY KEY AUTOINCREMENT," +
				"content 		varchar[250]," +
				"isRemindedOndesk boolean," +
				"isReminder 	boolean," +
				"timeToRemind 	BIGINT," +
				"tabID 			integer," +
				"FOREIGN 		KEY(tabID) REFERENCES tab(_id))");
		db.execSQL("insert into tab(tabName,tabKeyword) values('default','')");
//		db.execSQL("insert into tab(tabName,tabKeyword) values('note','meeting,note')");
//		db.execSQL("insert into note(content,tabID) values('There will be a meeting at 1:00 in D305',1)");
//		db.execSQL("insert into note(content,tabID) values('There will be a meeting at 2:00 in D444',1)");
//		db.execSQL("insert into note(content,tabID) values('There will be a meeting at 3:00 in D654',1)");
//		db.execSQL("insert into note(content,tabID) values('There will be a meeting at 4:00 in D545',1)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
