package com.Sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import noteObject.Note;

import Utils.Meta;
import Utils.MySQliteHelper;
import Utils.SQLiteUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class GridviewActivity extends Activity {
	GridView gridView;
	int tabID;
	ArrayList<HashMap<String, String>> lstLayoutItem = new ArrayList<HashMap<String, String>>(
			1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subview);

		tabID = this.getIntent().getIntExtra("tabID", -1);
		gridView = (GridView) findViewById(R.id.gridView);

		initArrayList(GridviewActivity.this, tabID);

		SimpleAdapter saImageItems = new SimpleAdapter(this, lstLayoutItem,
				R.layout.gridview_item, new String[] { "ItemText" },
				new int[] { R.id.sumarrText });
		gridView.setOnItemClickListener(new ItemClickListener());
		gridView.setAdapter(saImageItems);

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

	private void initArrayList(Context context, int ID) {
		List<String> noteContents = new ArrayList<String>();

		if (ID == -1) {
			noteContents = SQLiteUtils.getAllNoteContent(context);
		} else {
			noteContents = SQLiteUtils.getAllNoteContentBytabID(context, ID);
		}
		for (int i = 0; i < noteContents.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ItemText", noteContents.get(i));
			lstLayoutItem.add(map);
		}
	}

	class ItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			LinearLayout linearLayout = (LinearLayout) parent
					.getChildAt(position);
			TextView textView = (TextView) linearLayout
					.findViewById(R.id.sumarrText);
			String textString = textView.getText().toString();
			Intent intent = new Intent(GridviewActivity.this,
					NoteEditActivity.class);

			Note note = SQLiteUtils.getNoteByContent(getApplicationContext(),
					textString);
			intent.putExtra("msg", textString);
			intent.putExtra(Meta.TABLE_NOTE_COLUMN_TABID, note.getTabID());
			Bundle bundle = new Bundle();
			bundle.putSerializable(Meta.KEY_NOTE, note);
			intent.putExtras(bundle);
			GridviewActivity.this.startActivity(intent);
			GridviewActivity.this.finish();
		}
	}

	@Override
	public void onBackPressed() {

	}
}
