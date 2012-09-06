package com.explorer.taskmanager;

import com.explorer.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 12:05:43 AM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class MenuHandler {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static final int MENU_DETAIL = 4;
	public static final int MENU_IGNORE = 0;
	public static final int MENU_KILL = 2;
	public static final int MENU_SELECTALL = 5;
	public static final int MENU_SETKILLONLY = 6;
	public static final int MENU_SORT_BY_CPU_ASC = 1;
	public static final int MENU_SORT_BY_CPU_DSC = 0;
	public static final int MENU_SORT_BY_MEM_ASC = 3;
	public static final int MENU_SORT_BY_MEM_DSC = 2;
	public static final int MENU_SWITCH = 1;
	public static final int MENU_UNINSTALL = 3;

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
	public static PackageInfo getPackageInfo(
			PackageManager paramPackageManager, String paramString) {
		PackageInfo localObject = null;
		try {
			PackageInfo localPackageInfo = paramPackageManager.getPackageInfo(
					paramString, 1);
			localObject = localPackageInfo;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
		}
		return localObject;
	}

	public static AlertDialog.Builder getTaskMenuDialog(
			final TaskManager paramTaskManager, final Detail paramDetail) {
		String str = paramDetail.getTitle();
		paramDetail.getIcon();
		AlertDialog.Builder builder = new AlertDialog.Builder(paramTaskManager);
		builder.setIcon(R.drawable.icon);
		builder.setTitle("menu_task_operation");
		builder.setPositiveButton("ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				case MENU_IGNORE:

					break;
				case MENU_SWITCH:
					Intent localIntent3 = paramDetail.getIntent();
					if (localIntent3 == null) {
						Toast.makeText(paramTaskManager, "message_switch_fail",
								Toast.LENGTH_SHORT).show();
					}
					try {
						paramTaskManager.startActivity(localIntent3);
					} catch (Exception localException2) {
						Toast.makeText(paramTaskManager,
								localException2.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
					break;
				case MENU_KILL:
					if ((paramTaskManager.getPackageName()
							.equals("com.estrongs.android.taskmanager"))
							&& (TmWidgetProvider.getWidgetIds(
									paramTaskManager.getApplicationContext())
									.size() != 0)) {
						Toast.makeText(
								paramTaskManager.getApplicationContext(),
								"taskmanager_kill_self_warning", 1).show();
					}
					if ((paramDetail instanceof AppDetail)) {
						TaskManager.stopApp(paramTaskManager,
								paramDetail.getPackageName());
						if (paramDetail.getPackageName().equals(
								paramTaskManager.getPackageName()))
							paramTaskManager.refresh();
					}
					TaskManager.stopApp(paramTaskManager,
							paramDetail.getPackageName());
					paramTaskManager.refresh();
					if (paramDetail.getPackageName().equals(
							paramTaskManager.getPackageName()))
						;
					Intent localIntent2 = new Intent(
							"android.intent.action.DELETE", Uri.fromParts(
									"package", paramDetail.getPackageName(),
									null));
					try {
						paramTaskManager.startActivity(localIntent2);
					} catch (Exception localException1) {
						Toast.makeText(paramTaskManager,
								localException1.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
					Intent localIntent1 = new Intent();
					int i;
					if (OSInfo.sdkVersion() >= 8) {
						i = 1;
						localIntent1.setClassName("com.android.settings",
								"com.android.settings.InstalledAppDetails");
						if (i != 0)
							localIntent1.putExtra(
									"com.android.settings.ApplicationPkgName",
									paramDetail.getPackageName());
					}
					while (true) {
						paramTaskManager.startActivity(localIntent1);
						i = 0;
						localIntent1.putExtra("pkg",
								paramDetail.getPackageName());
					}
					// String str2 = paramTaskManager.getPackageName();
					// String s = paramDetail.getPackageName();
					// if (TaskManager.ignoreListApp(
					// paramTaskManager.getApplicationContext(), s)) {
					// TaskManagerPreferences.removeFromSpecialList(
					// paramTaskManager.getApplicationContext(), s, 1);
					// paramTaskManager.refresh();
					// }
					// paramTaskManager.addToIgnoreList(s,
					// paramDetail.getTitle());

					// break;
				case MENU_UNINSTALL:

					break;
				case MENU_DETAIL:

					break;
				case MENU_SELECTALL:
					paramTaskManager.selectAll();
					break;
				case MENU_SETKILLONLY:
					String str1 = paramTaskManager.getPackageName();
					paramTaskManager.addToKillOnlyList(str1,
							paramDetail.getTitle());
					break;

				default:
					break;
				}
			}
		});
		builder.create();
		builder.show();
		return builder;
	}

	public static AlertDialog.Builder getSortMenuDialog(
			TaskManager paramTaskManager) {
		AlertDialog.Builder builder = new AlertDialog.Builder(paramTaskManager);
		builder.setTitle("sort_by");
		builder.setPositiveButton("ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				case MENU_SORT_BY_CPU_ASC:

					break;
				case MENU_SORT_BY_CPU_DSC:
					break;
				case MENU_SORT_BY_MEM_ASC:
					break;
				case MENU_SORT_BY_MEM_DSC:
					break;
				default:
					break;
				}
			}
		});
		builder.show();
		builder.create();
		return builder;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
