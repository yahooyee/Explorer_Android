package com.explorer.taskmanager;

import android.content.Context;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 2:35:53 AM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class ServiceDetail extends Detail implements Comparable<Object> {

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	protected ActivityManager.RunningServiceInfo runinfo = null;

	// ===========================================================
	// Constructors
	// ===========================================================
	public ServiceDetail(Context context,
			ActivityManager.RunningServiceInfo paramRunningServiceInfo) {
		super(context);
		this.runinfo = paramRunningServiceInfo;
		this.pm = context.getApplicationContext().getPackageManager();
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Getter & Setter
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public ActivityManager.RunningServiceInfo getRuninfo() {
		return this.runinfo;
	}

	// ----------------------------------------------------------------------------------------------
	// Methods for/from SuperClass/Interfaces
	// ----------------------------------------------------------------------------------------------
	@Override
	public void fetchApplicationInfo(PackagesInfo packagesinfo) {
		if (this.appinfo == null)
			this.appinfo = packagesinfo.getInfo(this.runinfo.process);
	}

	@Override
	public void fetchNativeProcess(NativeProcessInfo nativeprocessinfo) {
		if (this.process == null)
			this.process = nativeprocessinfo
					.getNativeProcess(this.runinfo.process);
	}

	@Override
	public String getVisible() {
		// TODO Auto-generated method stub
		if ((this.visible == null) && (this.runinfo != null))
			if (!this.runinfo.foreground)
				// this.visible = this.mContext.getText(2131099668).toString();
				this.visible = "visible_foreground";
		// this.visible = this.mContext.getText(2131099667).toString();
		this.visible = "visible_background";
		if (this.visible == null)
			this.visible = "";
		return this.visible;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public boolean isValidProcess() {
		boolean flag;
		if (runinfo != null && appinfo != null && pkginfo != null
				&& pkginfo.activities != null && pkginfo.activities.length > 0)
			flag = true;
		else
			flag = false;
		return flag;

	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
