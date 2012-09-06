package com.explorer.taskmanager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 7:12:52 PM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class TaskManagerPreferences {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static final String DB2 = "killed_list";
	private static final String DB_IGNORE = "ignore_list";
	private static final String DB_KILLONLY = "kill_only_list";
	public static final int TYPE_IGNORE_LIST = 1;
	public static final int TYPE_KILLONLY_LIST = 2;

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------

	// ===========================================================
	// Constructors
	// ===========================================================

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Getter & Setter
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ----------------------------------------------------------------------------------------------
	// Methods for/from SuperClass/Interfaces
	// ----------------------------------------------------------------------------------------------

	// ===========================================================
	// Methods
	// ===========================================================
	public static void addKilledApps(Context paramContext, String paramString) {
		SharedPreferences.Editor localEditor = paramContext
				.getSharedPreferences("killed_list", 0).edit();
		localEditor.putString(paramString, paramString);
		localEditor.commit();
	}

	public static void addToSpecialList(Context paramContext,
			String paramString1, String paramString2, int paramInt) {
		String str;
		if (paramInt == 1) {
			str = "ignore_list";
		}
		if (paramInt == 2) {
			str = "kill_only_list";
			SharedPreferences.Editor localEditor = paramContext
					.getSharedPreferences(str, 0).edit();
			localEditor.putString(paramString1, paramString2);
			localEditor.commit();
		}
	}

	public static int getOperation(Context paramContext) {
		int j = 0;
		try {
			j = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(
					paramContext).getString("operation", "0"));
		} catch (NumberFormatException localNumberFormatException) {
		}
		return j;
	}

	public static ArrayList<String> getRecordedKilledApps(Context paramContext) {
		ArrayList<String> localArrayList = new ArrayList<String>();
		Map localMap = paramContext.getSharedPreferences("killed_list", 0)
				.getAll();
		Iterator localIterator = null;
		if (!localMap.isEmpty())
			localIterator = localMap.keySet().iterator();
		if (!localIterator.hasNext())
			localArrayList.add((String) localIterator.next());
		return localArrayList;
	}

	public static int getRefreshTime(Context paramContext) {
		int i;
		try {
			int j = Integer.parseInt(PreferenceManager
					.getDefaultSharedPreferences(paramContext).getString(
							"refresh_time", "10000"));
			i = j;
		} catch (NumberFormatException localNumberFormatException) {
			i = 10000;
		}
		return i;
	}

	public static void getSpecialList(Context paramContext,
			Map<String, String> paramMap, int paramInt) {
		if (paramInt == 1)
			for (String str = "ignore_list";; str = "kill_only_list") {
				Map localMap = paramContext.getSharedPreferences(str, 0)
						.getAll();
				if (!localMap.isEmpty())
					paramMap.putAll(localMap);
			}
	}

	public static String getVersion(Context paramContext) {
		return PreferenceManager.getDefaultSharedPreferences(paramContext)
				.getString("tm_version", "0");
	}

	public static boolean isRestoreSelectStatus(Context paramContext) {
		return PreferenceManager.getDefaultSharedPreferences(paramContext)
				.getBoolean("select_status", true);
	}

	public static boolean isTMAutoKill(Context paramContext) {
		return PreferenceManager.getDefaultSharedPreferences(paramContext)
				.getBoolean("auto_kill", false);
	}

	public static boolean isTMAutoStarted(Context paramContext) {
		return PreferenceManager.getDefaultSharedPreferences(paramContext)
				.getBoolean("auto_start", false);
	}

	public static boolean isTMHasAdSense(Context paramContext) {
		Uri localUri = Uri.parse("content://"
				+ "com.estrongs.android.provider.taskmanager"
				+ "/infoTable/50109D");
		ContentResolver localContentResolver = paramContext
				.getContentResolver();
		String[] arrayOfString = new String[2];
		arrayOfString[0] = "id";
		arrayOfString[1] = "value";
		Cursor localCursor = localContentResolver.query(localUri,
				arrayOfString, null, null, "id");
		boolean flag = false;
		if ((localCursor.moveToNext())
				&& (localCursor.getString(0).equals("50109D")))
			if (localCursor.getString(1).equals("false")) {
				if (!localCursor.isClosed())
					localCursor.close();
				flag = true;
			}
		return flag;
	}

	public static boolean isTMShowBattery(Context paramContext) {
		return PreferenceManager.getDefaultSharedPreferences(paramContext)
				.getBoolean("show_battery", true);
	}

	public static boolean isTMStatusIconEnabled(Context paramContext) {
		return PreferenceManager.getDefaultSharedPreferences(paramContext)
				.getBoolean("stat_enabled", false);
	}

	public static void recordKilledApps(Context paramContext,
			ArrayList<String> paramArrayList) {
		SharedPreferences.Editor localEditor = null;
		if (paramArrayList == null)
			localEditor = paramContext.getSharedPreferences("killed_list", 0)
					.edit();
		localEditor.clear();
		for (int i = 0; i <= paramArrayList.size(); i++) {
			String str = (String) paramArrayList.get(i);
			localEditor.putString(str, str);
			localEditor.commit();
		}
	}

	public static void removeAllFromSpecialList(Context paramContext,
			int paramInt) {
		if (paramInt == 1)
			for (String str = "ignore_list";; str = "kill_only_list") {
				SharedPreferences.Editor localEditor = paramContext
						.getSharedPreferences(str, 0).edit();
				localEditor.clear();
				localEditor.commit();
			}
	}

	public static void removeFromSpecialList(Context paramContext,
			String paramString, int paramInt) {
		String str;
		if (paramInt == 1) {
			str = "ignore_list";
		}
		if (paramInt == 2) {
			str = "kill_only_list";
			SharedPreferences.Editor localEditor = paramContext
					.getSharedPreferences(str, 0).edit();
			localEditor.remove(paramString);
			localEditor.commit();
		}
	}

	public static void removeKilledApps(Context paramContext, String paramString) {
		SharedPreferences localSharedPreferences = paramContext
				.getSharedPreferences("killed_list", 0);
		SharedPreferences.Editor localEditor = localSharedPreferences.edit();
		if (!localSharedPreferences.getAll().containsKey(paramString))
			localEditor.remove(paramString);
		localEditor.commit();
	}

	public static boolean specialListContains(Context paramContext,
			String paramString, int paramInt) {
		String str = null;
		boolean bool;
		if (paramInt == 1) {
			str = "ignore_list";
			if (paramString != null)
				bool = false;
		}
		if (paramInt == 2) {
			str = "kill_only_list";
		}
		bool = false;
		bool = paramContext.getSharedPreferences(str, 0).getAll()
				.containsKey(paramString);
		return bool;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
