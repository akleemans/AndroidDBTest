package com.dbtest;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ShowResults extends ListActivity {

	ArrayAdapter<String> adapter;
	String[] results = { "" };

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		ArrayList<String> stringArray = new ArrayList<String>();

		// Open database
		DataBaseHelper db = new DataBaseHelper(this);
		try {
			db.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		db.openDataBase();

		// Get arguments from intent
		final Bundle extras = getIntent().getExtras();
		String[] args = { extras.getString("query") };

		// Dive into database (runQuery) with a predefined query
		String query = "SELECT words FROM wordlist WHERE words LIKE ?";
		Cursor cursor = db.runQuery(query, args);

		// Gather results
		if (cursor.moveToFirst()) {
			do {
				stringArray.add(cursor.getString(0));
			} while (cursor.moveToNext());
		} else {
			stringArray.add("No results.");
		}

		// Close database
		cursor.close();
		db.close();

		results = stringArray.toArray(results);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, results);
		setListAdapter(adapter);
	}

	protected void updateAdapter() {
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, results);
		setListAdapter(adapter);
	}

	/**
	 * On item click, show some text.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	}
}
