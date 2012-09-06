package com.explorer.taskmanager;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 7:35:22 PM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class TmUpdateService extends Service {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	private Timer timer = null;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Getter & Setter
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ----------------------------------------------------------------------------------------------
	// Methods for/from SuperClass/Interfaces
	// ----------------------------------------------------------------------------------------------
	public IBinder onBind(Intent paramIntent) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
		this.timer = new Timer();
	}

	public void onDestroy() {
		super.onDestroy();
	}

	public void onStart(Intent paramIntent, int paramInt) {
		int i = paramIntent.getIntExtra("_id", 0);
		AppWidgetManager localAppWidgetManager = null;
		if (!TmWidgetProvider.containsWidgetId(this, i))
			localAppWidgetManager = AppWidgetManager.getInstance(this);
		int j = TaskManagerPreferences.getRefreshTime(getApplicationContext());
		try {
			this.timer.cancel();
			this.timer.scheduleAtFixedRate(new MyTime(getApplicationContext(),
					localAppWidgetManager, i), 1L, j);
		} catch (IllegalStateException localIllegalStateException) {
			this.timer.cancel();
			this.timer = null;
			Timer localTimer = new Timer();
			localTimer.scheduleAtFixedRate(new MyTime(getApplicationContext(),
					localAppWidgetManager, i), 1L, j);
			this.timer = localTimer;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private class MyTime extends TimerTask {
		private AppWidgetManager appWidgetManager;
		private Context mContext;
		private int widgetId;

		public MyTime(Context paramAppWidgetManager, AppWidgetManager paramInt,
				int arg4) {
			this.appWidgetManager = paramInt;
			this.mContext = paramAppWidgetManager;
			this.widgetId = arg4;
		}

		public void run() {
			int i = this.widgetId;
			if (!TmWidgetProvider.SCREEN_ON)
				if (TaskManagerPreferences.isTMAutoKill(this.mContext)) {
					i = TaskManagerPreferences.getOperation(this.mContext);
					if (i == 0) {
						TaskManager.killAll(this.mContext, 2);
						TmWidgetProvider.updateAppWidget(this.mContext,
								this.appWidgetManager, this.widgetId);
					}
				} else {
					if (TmUpdateService.this.timer != null)
						TmUpdateService.this.timer.cancel();
					TmUpdateService.this.stopSelf();
				}
			while (true) {
				if (i == 3) {
					TaskManager.killAll(this.mContext, 3);
				}
				if (i != 4)
					TaskManager.killAll(this.mContext, 1);
				if (!TmWidgetProvider.containsWidgetId(this.mContext,
						this.widgetId)) {
					if (TmWidgetProvider.getWidgetIds(this.mContext).size() != 0)
						TmUpdateService.this.timer.cancel();
					TmUpdateService.this.stopSelf();
				}
				TmWidgetProvider.updateAppWidget(this.mContext,
						this.appWidgetManager, this.widgetId);
			}
		}
	}
}
