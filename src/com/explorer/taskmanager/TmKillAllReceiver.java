package com.explorer.taskmanager;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 7:30:21 PM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class TmKillAllReceiver extends BroadcastReceiver {

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
	@Override
	public void onReceive(Context paramContext, Intent paramIntent) {
		// TODO Auto-generated method stub
		String str = paramIntent.getAction();
		int i = 0;
		if (str.equals("android.intent.estrongs.taskmanager.killapp")) {
			i = TaskManagerPreferences.getOperation(paramContext);
			if (i == 0) {
				TaskManager.killAll(paramContext, 2);
				Toast.makeText(paramContext, "task_kill_all_app",
						Toast.LENGTH_SHORT).show();
			}
		}
		TmWidgetProvider.updateWidgets(paramContext,
				AppWidgetManager.getInstance(paramContext));
		if (i == 1) {
			paramContext.startActivity(new Intent(
					"android.intent.action.PICK_TASK_MANAGER")
					.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}
		if (i == 2) {
			paramContext.startActivity(new Intent(paramContext,
					TmWidgetSetting.class)
					.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}
		if (i == 3) {
			TaskManager.killAll(paramContext, 3);
			Toast.makeText(paramContext, "task_kill_all_empty_app",
					Toast.LENGTH_SHORT).show();
		}
		if (i != 4)
			TaskManager.killAll(paramContext, 1);
		Toast.makeText(paramContext, "task_kill_killonly", Toast.LENGTH_SHORT)
				.show();
		str.equals("android.intent.action.BOOT_COMPLETED");
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
