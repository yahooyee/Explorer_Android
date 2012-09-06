package com.explorer.taskmanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 1:13:34 AM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class PackagesInfo {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	private static PackagesInfo instance = null;
	private PackageManager mPM;

	// ===========================================================
	// Constructors
	// ===========================================================
	private PackagesInfo(Context context) {
		mPM = context.getApplicationContext().getPackageManager();
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
	public static PackagesInfo getInstance(Context context) {
		if (instance == null)
			instance = new PackagesInfo(context);
		return instance;
	}

	public ApplicationInfo getInfo(String s) {
		ApplicationInfo applicationinfo1 = null;
		try {
			applicationinfo1 = mPM.getApplicationInfo(s, 8192);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return applicationinfo1;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
