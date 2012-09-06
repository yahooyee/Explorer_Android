package com.explorer.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 11:42:12 PM Sep 4, 2012 Tel: 0974 878 244
 * 
 */
public abstract class Detail implements Comparable<Object> {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	protected ApplicationInfo appinfo = null;
	protected Drawable icon = null;
	protected Intent intent = null;
	protected Context mContext = null;
	protected PackageInfo pkginfo = null;
	protected PackageManager pm;
	protected NativeProcessInfo.NativeProcess process = null;
	protected String title = null;
	protected String visible = null;

	// ===========================================================
	// Constructors
	// ===========================================================
	public Detail(Context context) {
		process = null;
		appinfo = null;
		pkginfo = null;
		title = null;
		intent = null;
		icon = null;
		visible = null;
		mContext = null;
		mContext = context;
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
	public int compareTo(Object obj) {
		int i, j = 0;
		Detail detail;
		i = PreferenceManager.getDefaultSharedPreferences(mContext).getInt(
				"taskmanager_list", -1);
		detail = (Detail) obj;
		if (i != -1)
			if (detail == null)
				j = getTitle().compareTo(detail.getTitle());
		if (process == null || detail == null || detail.process == null) {
			j = -1;
		}
		if (i == 0) {
			if (process.cpu_t < detail.process.cpu_t) {
				j = 1;
			}
			if (process.cpu_t > detail.process.cpu_t) {
				j = -1;
			}
			if (process.cpu_t == detail.process.cpu_t) {
				j = 0;
			}
		} else if (i == 1) {
			if (process.cpu_t > detail.process.cpu_t) {
				j = 1;
			}
			if (process.cpu_t < detail.process.cpu_t) {
				j = -1;
			}
			if (process.cpu_t == detail.process.cpu_t) {
				j = 0;
			}
		} else if (i == 2) {
			if (process.mem_t < detail.process.mem_t) {
				j = 1;
			}
			if (process.mem_t > detail.process.mem_t) {
				j = -1;
			}
			if (process.mem_t == detail.process.mem_t) {
				j = 0;
			}
		} else if (i == 3) {
			if (process.mem_t > detail.process.mem_t) {
				j = 1;
			}
			if (process.mem_t < detail.process.mem_t) {
				j = -1;
			}
			if (process.mem_t == detail.process.mem_t) {
				j = 0;
			}
		}
		return j;
	}

	public abstract void fetchApplicationInfo(PackagesInfo packagesinfo);

	public abstract void fetchNativeProcess(NativeProcessInfo nativeprocessinfo);

	public void fetchPackageInfo() {
		if (pkginfo == null && appinfo != null)
			pkginfo = MenuHandler.getPackageInfo(pm, appinfo.packageName);
	}

	public ApplicationInfo getAppinfo() {
		return appinfo;
	}

	public String getBaseActivity() {
		return pkginfo.activities[0].name;
	}

	public Drawable getIcon() {
		if (icon == null && appinfo != null)
			icon = appinfo.loadIcon(pm);
		return icon;
	}

	public Intent getIntent() {
		Intent intent1 = null;
		if (intent1 == null) {
			intent1 = intent;
		}
		try {
			intent = pm.getLaunchIntentForPackage(pkginfo.packageName);
			if (intent != null) {
				intent = intent.cloneFilter();
				intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				intent1 = intent;
			}
			if (pkginfo.activities.length == 1) {
				intent = new Intent("android.intent.action.MAIN");
				intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				intent.setClassName(pkginfo.packageName,
						pkginfo.activities[0].name);
				intent1 = intent;
			}
			intent = IntentList.getIntent(pkginfo.packageName, pm);
			if (intent != null) {
				intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
				intent1 = intent;
			}
		} catch (Exception exception) {
			intent1 = null;
		}
		return intent1;
	}

	public NativeProcessInfo.NativeProcess getNativeProcess() {
		return process;
	}

	public String getPackageName() {
		String s;
		if (appinfo == null)
			s = null;
		else
			s = appinfo.packageName;
		return s;
	}

	public String getTitle() {
		if (title == null && appinfo != null)
			title = appinfo.loadLabel(pm).toString();
		return title;
	}

	public abstract String getVisible();

	public boolean isValidProcess() {
		boolean flag;
		if (appinfo != null && pkginfo != null && pkginfo.activities != null
				&& pkginfo.activities.length > 0)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public void setNativeProcess(NativeProcessInfo.NativeProcess nativeprocess) {
		process = nativeprocess;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
