package com.dbtest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	/*
	 * Insert name of your database (in 'assets') and package name here
	 */
	final static String DB_NAME = "sample.db";
	static String PACKAGE_NAME = "com.dbtest";

	// other variables
	private static String DB_PATH = Environment.getDataDirectory() + "/data/"
			+ PACKAGE_NAME + "/databases/";
	private SQLiteDatabase myDataBase = null;
	private final Context myContext;
	private DataBaseHelper myDbHelper;
	private static String TAG = "DataBaseHelper";

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		Log.e("DataBaseHelper", "Package Name: "+PACKAGE_NAME);
		
		this.myContext = context;
	}

	public DataBaseHelper createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		Log.i(TAG, "Database exists: " + dbExist);

		if (!dbExist) {
			myDbHelper = new DataBaseHelper(myContext);
			myDataBase = myDbHelper.getReadableDatabase();
			myDataBase.close();

			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}

		return this;
	}

	/**
	 * Run a query on a opened database.
	 * 
	 * @param query
	 *            a SQL statement including place holders ('?')
	 * @param args
	 *            arguments for proper (sanitized) place holders
	 * @return cursor
	 *            A cursor over the result set.
	 */
	public Cursor runQuery(String query, String[] args) {
		Log.i(TAG,"Executing statement: " + query + " with arguments: "
						+ args[0]);
		Cursor cursor = myDataBase.rawQuery(query, args);

		return cursor;
	}

	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// Transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;

		String myPath = DB_PATH + DB_NAME;

		try {
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLException e) {

			e.printStackTrace();
			return false;
		}

		if (checkDB != null) {
			checkDB.close();
		}
		return true;
		// return checkDB !=null ? true : false;
	}

	public void openDataBase() throws SQLException {

		// open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null) {
			myDataBase.close();
		}
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}