package com.explorer.taskmanager;

import com.explorer.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 9:14:37 PM Sep 2, 2012 Tel: 0974 878 244
 * 
 */
public class ShortcutCreater {
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

	// ===========================================================
	// Methods
	// ===========================================================
	public static boolean createTaskManagerIcon(Context paramContext) {
		Intent localIntent = createTaskManagerIntent(paramContext);
		localIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		paramContext.sendBroadcast(localIntent);
		return true;
	}

	private static Intent createTaskManagerIntent(Context paramContext) {
		Intent localIntent = new Intent();
		localIntent.putExtra("android.intent.extra.shortcut.INTENT",
				new Intent("android.intent.action.MAIN").setClassName(
						paramContext.getPackageName(),
						TaskManager.class.getName()));
		localIntent.putExtra("android.intent.extra.shortcut.NAME",
				"task_manager_title");
		try {
			localIntent.putExtra("android.intent.extra.shortcut.ICON",
					BitmapFactory.decodeResource(paramContext.getResources(),
							R.drawable.icon));
			localIntent.putExtra("duplicate", true);
			return localIntent;
		} catch (Exception localException) {
			while (true)
				localException.printStackTrace();
		}
	}

	public static boolean removeTaskManagerIcon(Context paramContext) {
		Intent localIntent = createTaskManagerIntent(paramContext);
		localIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
		paramContext.sendBroadcast(localIntent);
		return true;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
