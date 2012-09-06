package com.explorer.taskmanager;

import android.content.Context;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 1:18:13 AM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class AppDetail extends Detail implements Comparable<Object> {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	protected android.app.ActivityManager.RunningAppProcessInfo runinfo;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param context
	 */
	public AppDetail(
			Context context,
			android.app.ActivityManager.RunningAppProcessInfo runningappprocessinfo) {
		super(context);
		// TODO Auto-generated constructor stub
		runinfo = null;
		runinfo = runningappprocessinfo;
		pm = context.getApplicationContext().getPackageManager();

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
	public boolean isService() {
		boolean flag;
		if (runinfo.importance == 300)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public boolean isValidProcess() {
		boolean flag;
		if (runinfo != null && appinfo != null && pkginfo != null)
			flag = true;
		else
			flag = false;
		return flag;
	}

	@Override
	public void fetchApplicationInfo(PackagesInfo packagesinfo) {
		// TODO Auto-generated method stub
		if (appinfo == null)
			appinfo = packagesinfo.getInfo(runinfo.processName);
	}

	@Override
	public void fetchNativeProcess(NativeProcessInfo nativeprocessinfo) {
		// TODO Auto-generated method stub
		if (process == null)
			process = nativeprocessinfo.getNativeProcess(runinfo.processName);

	}

	@Override
	public String getVisible() {
		// TODO Auto-generated method stub
		if (visible != null || runinfo == null) {
			int importance = runinfo.importance;
		}
		if (visible == null)
			visible = "";
		visible = "visible_background";
		visible = "visible_empty";
		visible = "visible_foreground";
		visible = "visible_service";
		visible = "visible_visible";
		return visible;
	}

	public android.app.ActivityManager.RunningAppProcessInfo getRuninfo() {
		return runinfo;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
