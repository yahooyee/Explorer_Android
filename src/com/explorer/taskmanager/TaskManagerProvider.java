package com.explorer.taskmanager;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 7:23:21 PM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class TaskManagerProvider extends ContentProvider {

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static final int ALL_VAL = 1;
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.estrongs.android.provider.taskmanager/infoTable");
	private static final String DATABASE_NAME = "etm_info.db";
	private static final int DATABASE_VERSION = 2;
	private static final int ID = 2;
	public static final String PROVIDER_NAME = "com.estrongs.android.provider.taskmanager";
	private static final String TABLE_NAME = "infoTable";

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	private static final UriMatcher uriMatcher = new UriMatcher(-1);
	private SQLiteDatabase mDB;

	static {
		uriMatcher.addURI("com.estrongs.android.provider.taskmanager",
				"infoTable", 1);
		uriMatcher.addURI("com.estrongs.android.provider.taskmanager",
				"infoTable/*", 2);
	}

	// ===========================================================
	// Constructors
	// ===========================================================

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Getter & Setter
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ----------------------------------------------------------------------------------------------
	// Methods for/from SuperClass/Interfaces
	// ----------------------------------------------------------------------------------------------
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri paramUri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(paramUri)) {
		case 1:
		case 2:
		default:
			throw new IllegalArgumentException("Unknown URL " + paramUri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		this.mDB = new DatabaseHelper(getContext(), "etm_info.db", null, 2)
				.getWritableDatabase();
		return true;
	}

	@Override
	public Cursor query(Uri paramUri, String[] paramArrayOfString1,
			String paramString1, String[] paramArrayOfString2,
			String paramString2) {
		SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
		if (uriMatcher.match(paramUri) == 2) {
			localSQLiteQueryBuilder.setTables("infoTable");
			localSQLiteQueryBuilder.appendWhere("id='"
					+ (String) paramUri.getPathSegments().get(1) + "'");
		}
		if ((paramString2 == null) || (paramString2 == ""))
			paramString2 = "id";
		Cursor localCursor = localSQLiteQueryBuilder.query(this.mDB,
				paramArrayOfString1, paramString1, paramArrayOfString2, null,
				null, paramString2);
		localCursor.setNotificationUri(getContext().getContentResolver(),
				paramUri);
		return localCursor;
	}

	@Override
	public int update(Uri paramUri, ContentValues paramContentValues,
			String paramString, String[] paramArrayOfString) {
		String str2;
		String str3 = null;
		if (uriMatcher.match(paramUri) == 2) {
			String str1 = (String) paramUri.getPathSegments().get(1);
			StringBuilder localStringBuilder = new StringBuilder("id='")
					.append(str1).append("'");
			if (!TextUtils.isEmpty(paramString)) {
				str2 = " AND (" + paramString + ')';
				str3 = str2;
			}
		}
		for (int i = this.mDB.update("infoTable", paramContentValues, str3,
				paramArrayOfString);; i = 0) {
			getContext().getContentResolver().notifyChange(paramUri, null);
			str2 = "";
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context paramContext, String paramString,
				SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt) {
			super(paramContext, paramString, paramCursorFactory, paramInt);
		}

		public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
			paramSQLiteDatabase
					.execSQL("CREATE TABLE infoTable (id TEXT,value TEXT);");
			try {
				paramSQLiteDatabase
						.execSQL("insert into infoTable (id,value) values('50109D','true');");
			} catch (SQLException localSQLException) {
				Log.e("ERROR", "CREATE TABLE");
			}
		}

		public void onUpgrade(SQLiteDatabase paramSQLiteDatabase,
				int paramInt1, int paramInt2) {
			paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS infoTable");
			onCreate(paramSQLiteDatabase);
		}
	}
}
