package com.explorer.taskmanager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.explorer.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 7:38:36 PM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class TmWidgetProvider extends AppWidgetProvider {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static final int CLEAN_DOOR_SIZE = 3;
	private static final String PREFS_NAME = "widget_ids";
	public static boolean SCREEN_ON = false;
	private static final String TAG = "TmWidgetProvider";
	private static long lastUpdateMillis = 0L;
	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	private static Handler mHandler;
	private static ScreenStatusReceiver receiver = new ScreenStatusReceiver();
	private static final boolean updateInServiceTimer = true;
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
	static {
		mHandler = new Handler();
		lastUpdateMillis = 0L;
		SCREEN_ON = true;
	}

	protected static boolean containsWidgetId(Context paramContext, int paramInt) {
		Map map = paramContext.getSharedPreferences("widget_ids", 0).getAll();
		boolean flag;
		if (!map.isEmpty() && map.containsKey(String.valueOf(paramInt)))
			flag = true;
		else
			flag = false;
		return flag;

	}

	public static ArrayList<Integer> getWidgetIds(Context paramContext) {
		ArrayList<Integer> localArrayList = new ArrayList<Integer>();
		Map localMap = paramContext.getSharedPreferences("widget_ids", 0)
				.getAll();
		Iterator localIterator = localMap.keySet().iterator();
		if (!localMap.isEmpty())
			localIterator = localMap.keySet().iterator();
		String str = (String) localIterator.next();
		if (localMap.get(str) == null)
			localArrayList.add((Integer) localMap.get(str));
		return localArrayList;
	}

	private static boolean isRegistered(Context paramContext) {
		return PreferenceManager.getDefaultSharedPreferences(paramContext)
				.getBoolean("registered", false);
	}

	private static boolean needUpdate(Context paramContext) {
		boolean flag;
		if (System.currentTimeMillis() - lastUpdateMillis >= (long) TaskManagerPreferences
				.getRefreshTime(paramContext))
			flag = true;
		else
			flag = false;
		return flag;

	}

	private static void register(Context paramContext) {
		isRegistered(paramContext);
		registerIntentReceivers(paramContext);
		saveRegistered(paramContext, true);
	}

	public static void registerIntentReceivers(Context paramContext) {
		IntentFilter localIntentFilter = new IntentFilter();
		localIntentFilter.addAction("android.intent.action.SCREEN_ON");
		localIntentFilter.addAction("android.intent.action.SCREEN_OFF");
		paramContext.getApplicationContext().registerReceiver(receiver,
				localIntentFilter);
	}

	private static void removeWidgetId(Context paramContext, int paramInt) {
		SharedPreferences localSharedPreferences = paramContext
				.getSharedPreferences("widget_ids", 0);
		Map localMap = localSharedPreferences.getAll();
		if ((!localMap.isEmpty())
				&& (localMap.containsKey(String.valueOf(paramInt)))) {
			SharedPreferences.Editor localEditor = localSharedPreferences
					.edit();
			localEditor.remove(String.valueOf(paramInt));
			localEditor.commit();
		}
	}

	public static void restartUpdate(Context paramContext) {
		SCREEN_ON = true;
		ArrayList arraylist = getWidgetIds(paramContext);
		if (arraylist.size() != 0) {
			int i = 0;
			while (i < arraylist.size()) {
				int j = ((Integer) arraylist.get(i)).intValue();
				Intent intent = new Intent(paramContext, TmUpdateService.class);
				intent.putExtra("_id", j);
				paramContext.startService(intent);
				i++;
			}
		}
	}

	private static void saveRegistered(Context paramContext,
			boolean paramBoolean) {
		SharedPreferences.Editor localEditor = PreferenceManager
				.getDefaultSharedPreferences(paramContext).edit();
		localEditor.putBoolean("registered", paramBoolean);
		localEditor.commit();
	}

	private static void saveWidgetId(Context paramContext, int paramInt) {
		SharedPreferences.Editor localEditor = paramContext
				.getSharedPreferences("widget_ids", 0).edit();
		localEditor.putInt(String.valueOf(paramInt), paramInt);
		localEditor.commit();
	}

	private static void stopUpdate(Context paramContext) {
		SCREEN_ON = false;
	}

	private static void unregister(Context context) {
		if (isRegistered(context) && getWidgetIds(context).size() == 0) {
			unregisterIntentReceivers(context);
			saveRegistered(context, false);
		}
	}

	public static void unregisterIntentReceivers(Context paramContext) {
		try {
			paramContext.getApplicationContext().unregisterReceiver(receiver);
		} catch (Exception localException) {
		}
	}

	protected static void updateAppWidget(Context paramContext,
			AppWidgetManager paramAppWidgetManager, int paramInt) {
		Log.d("TmWidgetProvider", "updateAppWidget appWidgetId=" + paramInt);
		RemoteViews localRemoteViews = new RemoteViews(
				paramContext.getPackageName(), R.layout.appwidget_provider);
		localRemoteViews.setOnClickPendingIntent(R.id.appwidget, PendingIntent
				.getBroadcast(paramContext, 0, new Intent(
						"android.intent.estrongs.taskmanager.killapp"),
						Intent.FLAG_ACTIVITY_MULTIPLE_TASK));
		String str = TaskManager.getSystemAvailMem(paramContext) + "M";
		int i = TaskManager.getSystemRunningAppsNum(paramContext);
		localRemoteViews.setTextViewText(R.id.avail_mem, str);
		localRemoteViews.setTextViewText(R.id.app_num, String.valueOf(i));
		if (i <= 3)
			localRemoteViews.setImageViewResource(R.id.imvAPPpro,
					R.drawable.widget_bg_clean);
		paramAppWidgetManager.updateAppWidget(paramInt, localRemoteViews);
		localRemoteViews.setImageViewResource(R.drawable.widget_bg_clean,
				R.drawable.widget_bg_slum);
	}

	public static void updateWidgets(Context context,
			AppWidgetManager paramAppWidgetManager) {
		ArrayList arraylist = getWidgetIds(context);
		if (arraylist.size() != 0) {
			int i = 0;
			while (i < arraylist.size()) {
				updateAppWidget(context, paramAppWidgetManager,
						((Integer) arraylist.get(i)).intValue());
				i++;
			}
		}

	}

	public void onDeleted(Context paramContext, int[] paramArrayOfInt) {
		try {
			Log.d("TmWidgetProvider", "onDeleted");
			int i = paramArrayOfInt.length;
			for (int j = 0;; j++) {
				if (j >= i) {
					unregister(paramContext);
					break;
				}
				removeWidgetId(paramContext, paramArrayOfInt[j]);
			}
		} catch (Exception e) {

		}
	}

	public void onDisabled(Context paramContext) {
	}

	public void onEnabled(Context paramContext) {
	}

	public void onReceive(Context context, Intent intent) {
		if ("android.appwidget.action.APPWIDGET_DELETED".equals(intent
				.getAction())) {
			int i = intent.getExtras().getInt("appWidgetId", 0);
			if (i != 0) {
				int ai[] = new int[1];
				ai[0] = i;
				onDeleted(context, ai);
			}
		} else {
			super.onReceive(context, intent);
		}

	}

	/** @deprecated */
	public void onUpdate(Context paramContext,
			AppWidgetManager paramAppWidgetManager, int[] paramArrayOfInt) {
		try {
			Log.d("TmWidgetProvider", "onUpdate");
			int i = paramArrayOfInt.length;
			for (int j = 0;; j++) {
				if (j >= i) {
					register(paramContext);
					break;
				}
				int k = paramArrayOfInt[j];
				saveWidgetId(paramContext, k);
				Intent localIntent = new Intent(paramContext,
						TmUpdateService.class);
				localIntent.putExtra("_id", k);
				paramContext.startService(localIntent);
			}
		} catch (Exception e) {
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static class ScreenStatusReceiver extends BroadcastReceiver {
		public void onReceive(Context paramContext, Intent paramIntent) {
			if ("android.intent.action.SCREEN_ON".equals(paramIntent
					.getAction())) {
				Log.d("TmWidgetProvider", "Screen On, start to update views");
				TmWidgetProvider.restartUpdate(paramContext);
			}
			if ("android.intent.action.SCREEN_OFF".equals(paramIntent
					.getAction())) {
				Log.d("TmWidgetProvider", "Screen Off, stop to update views");
				TmWidgetProvider.getWidgetIds(paramContext);
			}
		}
	}
}
