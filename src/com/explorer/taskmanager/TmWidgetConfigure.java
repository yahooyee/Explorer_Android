package com.explorer.taskmanager;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 7:37:26 PM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class TmWidgetConfigure extends Activity {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	int mAppWidgetId = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Getter & Setter
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ----------------------------------------------------------------------------------------------
	// Methods for/from SuperClass/Interfaces
	// ----------------------------------------------------------------------------------------------
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setResult(0);
		Bundle localBundle = getIntent().getExtras();
		if (localBundle != null)
			this.mAppWidgetId = localBundle.getInt("appWidgetId", 0);
		if (this.mAppWidgetId == 0)
			finish();
		AppWidgetManager.getInstance(this).getAppWidgetInfo(this.mAppWidgetId).updatePeriodMillis = TaskManagerPreferences
				.getRefreshTime(this);
		Intent localIntent = new Intent();
		localIntent.putExtra("appWidgetId", this.mAppWidgetId);
		setResult(-1, localIntent);
		finish();
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
