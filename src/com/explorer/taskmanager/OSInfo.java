package com.explorer.taskmanager;

import android.os.Build;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 1:49:02 AM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class OSInfo {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static final int BASE = 1;
	public static final int BASE_1_1 = 2;
	public static final int CUPCAKE = 3;
	public static final int DONUT = 4;
	public static final int ECLAIR = 5;
	public static final int ECLAIR_0_1 = 6;
	public static final int ECLAIR_MR1 = 7;
	public static final int FROYO = 8;

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
	public static int sdkVersion() {
		int i;
		try {
			int j = Integer.parseInt(Build.VERSION.SDK);
			i = j;
		} catch (NumberFormatException localNumberFormatException) {
			i = 3;
		}
		return i;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
