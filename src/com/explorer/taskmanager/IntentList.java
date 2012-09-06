package com.explorer.taskmanager;

import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 1:06:09 AM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class IntentList {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	private static List infoList = null;

	// ===========================================================
	// Constructors
	// ===========================================================
	public IntentList() {
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Getter & Setter
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ----------------------------------------------------------------------------------------------
	// Methods for/from SuperClass/Interfaces
	// ----------------------------------------------------------------------------------------------

	// ===========================================================
	// Methods
	// ===========================================================
	public static List getRunableList(PackageManager packagemanager,
			boolean flag) {
		List list;
		if (infoList == null || flag) {
			Intent intent = new Intent("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.LAUNCHER");
			infoList = packagemanager.queryIntentActivities(intent, 0);
		}
		list = infoList;
		return list;
	}

	public static Intent getIntent(String s, PackageManager packagemanager) {
		Iterator iterator = getRunableList(packagemanager, false).iterator();
		Intent intent1 = null;
		if (!iterator.hasNext()) {
			intent1 = null;
		} else {
			ResolveInfo resolveinfo = (ResolveInfo) iterator.next();
			if (!s.equals(resolveinfo.activityInfo.packageName)) {
				Intent intent = new Intent("android.intent.action.MAIN");
				intent.addCategory("android.intent.category.LAUNCHER");
				intent.setClassName(s, resolveinfo.activityInfo.name);
				intent1 = intent;
			}
		}
		return intent1;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
