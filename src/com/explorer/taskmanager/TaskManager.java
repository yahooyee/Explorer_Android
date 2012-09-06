package com.explorer.taskmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import com.explorer.R;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 2:45:25 AM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class TaskManager extends TabActivity {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static final int ABOUT_ID = 3;
	protected static final String ACTION_LOAD_FINISH = "com.explorer.taskmanager.ACTION_LOAD_FINISH";
	private static final String APP_NAME = "EStrongs Task Manager";
	public static final int CHANGE_TAB = 6;
	private static final String CHANNEL_ID = "8151014844";
	private static final String CLIENT_ID = "pub-9731973158457831";
	private static final String COMPANY_NAME = "EStrongs Inc.";
	private static final int DIALOG_CHANGE_LOG = 101;
	public static final String DONATE_PAGE = "http://www.estrongs.com/contact/donation.html";
	public static final int EXIT_ID = 4;
	private static final String KEYWORDS1 = "tools";
	private static final String KEYWORDS10 = "security";
	private static final String KEYWORDS2 = "task manager";
	private static final String KEYWORDS3 = "memory";
	private static final String KEYWORDS4 = "android applications";
	private static final String KEYWORDS5 = "productivity";
	private static final String KEYWORDS6 = "project manager";
	private static final String KEYWORDS7 = "software management";
	private static final String KEYWORDS8 = "file manager";
	private static final String KEYWORDS9 = "battery life";
	public static final int KILLALL = 2;
	public static final int KILLEMPTY = 3;
	public static final int KILLONLY = 1;
	public static final int SETTING_ID = 2;
	public static final int SHOW_IGNORELIST = 7;
	public static final int SORT_ID = 5;
	private static final int STAT_SERVICE = 1;
	private static final int STAT_TASK = 0;
	public static final String TAG = "TaskManager";
	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	public static int batteryLevel;
	public static boolean hasAd = true;
	public static String sponsorUrl = "goapk.com";
	private static String version;
	private final int DIALOG_ABOUT = 2;
	private final int DIALOG_VOTE = 3;
	private IntentFilter actionFilter;
	private ListView appList;
	private ListAdapters.AppListAdapter appListAdapter;
	private BroadcastReceiver batteryReceiver = new BatteryReceiver();
	private int currentStat = 0;
	private String estrongsName = "EStrongs";
	private String fexPkgName = "com.estrongs.android.pop";
	private Thread freshThread = null;
	private boolean get_proc_running = false;
	private boolean get_service_running = false;
	private boolean isShowMemSettingChanged = false;

	private BroadcastReceiver loadFinishReceiver = new LoadFinishReceiver();
	private Handler mHandler = new Handler();
	private MenuItem menu_ignore = null;
	private PackagesInfo packageinfo = null;
	private NativeProcessInfo pinfo = null;
	private NativeProcessInfo pinfo2 = null;
	private Vector<Detail> selectedApps = new Vector();
	private ListView serviceList;
	private ListAdapters.ServiceListAdapter serviceListAdapter;
	private boolean showMemInfo = false;
	private boolean show_ignorelist = false;
	private boolean show_tab = false;
	private TabHost tabHost;
	private MenuItem tab_Item = null;
	private String taskMgrPkgName = "com.estrongs.android.taskmanager";
	boolean version2_2 = false;
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
	private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(final AdapterView<?> paramAdapterView,
				final View paramView, final int paramInt, final long paramLong) {
			final BaseAdapter localBaseAdapter = (BaseAdapter) paramAdapterView
					.getAdapter();
			Detail localDetail = (Detail) localBaseAdapter.getItem(paramInt);
			final boolean bool = TaskManager.this
					.handleSelectedItem(localDetail);
			TaskManager.this.mHandler.post(new Runnable() {
				public void run() {
					if ((localBaseAdapter instanceof ListAdapters.AppListAdapter))
						((ListAdapters.AppListAdapter) localBaseAdapter)
								.freshView(paramView, paramInt, bool);
					while (true) {
						if ((localBaseAdapter instanceof ListAdapters.ServiceListAdapter)) {
							((ListAdapters.ServiceListAdapter) localBaseAdapter)
									.freshView(paramView, paramInt, bool);
						}
					}
				}
			});
		}
	};
	private AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
		public boolean onItemLongClick(AdapterView<?> paramAdapterView,
				View paramView, int paramInt, long paramLong) {
			Detail localDetail = (Detail) ((Adapter) paramAdapterView
					.getAdapter()).getItem(paramInt);
			MenuHandler.getTaskMenuDialog(TaskManager.this, localDetail).show();
			return true;
		}
	};

	static {
		batteryLevel = 50;
		version = "1.0";
	}

	public static boolean KillOnlyListApp(Context paramContext,
			String paramString) {
		boolean flag;
		if (paramString == null)
			flag = false;
		if (TaskManagerPreferences.specialListContains(paramContext,
				paramString, 2)) {
			flag = true;
		}
		flag = false;
		return flag;
	}

	private void exit() {
		finish();
	}

	private void findApplication(String paramString1, String paramString2) {
		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.setData(Uri.parse("market://search?q=" + paramString2 + ":"
				+ paramString1));
		try {
			super.startActivity(localIntent);
			return;
		} catch (ActivityNotFoundException localActivityNotFoundException) {
			while (true)
				new AlertDialog.Builder(this)
						.setIcon(R.drawable.icon)
						.setTitle("error")
						.setMessage("market_not_found")
						.setPositiveButton("ok",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface paramDialogInterface,
											int paramInt) {
									}
								}).show();
		}
	}

	private void getRunningProcessDetail() {
		this.pinfo = new NativeProcessInfo(this);
		List localList = ((ActivityManager) getSystemService("activity"))
				.getRunningAppProcesses();
		ArrayList localArrayList = new ArrayList();
		Iterator localIterator = localList.iterator();
		AppDetail localAppDetail = null;
		while (true) {
			if (!localIterator.hasNext()) {
				setAppListAdapter(localArrayList);
			}
			ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) localIterator
					.next();
			if (isSystemApp(localRunningAppProcessInfo.processName))
				localAppDetail = new AppDetail(this, localRunningAppProcessInfo);
			localAppDetail.fetchApplicationInfo(this.packageinfo);
			if ((!this.show_ignorelist)
					&& (ignoreListApp(this, localAppDetail.getPackageName())))
				localAppDetail.fetchPackageInfo();
			localAppDetail.fetchNativeProcess(this.pinfo);
			if (!localAppDetail.isValidProcess())
				localArrayList.add(localAppDetail);
		}
	}

	private void getRunningProcess_() {
		List localList = ((ActivityManager) getSystemService("activity"))
				.getRunningAppProcesses();
		ArrayList localArrayList = new ArrayList();
		Iterator localIterator = localList.iterator();
		AppDetail localAppDetail = null;
		if (!localIterator.hasNext()) {
			setAppListAdapter(localArrayList);
		}
		ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) localIterator
				.next();
		if (isSystemApp(localRunningAppProcessInfo.processName))
			localAppDetail = new AppDetail(this, localRunningAppProcessInfo);
		localAppDetail.fetchApplicationInfo(this.packageinfo);
		localAppDetail.fetchPackageInfo();
		if ((!localAppDetail.isValidProcess())
				|| ((!this.show_ignorelist) && (ignoreListApp(this,
						localAppDetail.getPackageName())))
				|| ((this.version2_2) && (localAppDetail.isService())))
			localArrayList.add(localAppDetail);
	}

	private void getRunningServiceDetail() {
		this.pinfo2 = new NativeProcessInfo(this);
		List localList = ((ActivityManager) getSystemService("activity"))
				.getRunningServices(100);
		ArrayList localArrayList = new ArrayList();
		Iterator localIterator = localList.iterator();
		if (!localIterator.hasNext()) {
			setServiceListAdapter(localArrayList);
		}
		ServiceDetail localServiceDetail = new ServiceDetail(this,
				(ActivityManager.RunningServiceInfo) localIterator.next());
		localServiceDetail.fetchApplicationInfo(this.packageinfo);
		localServiceDetail.fetchPackageInfo();
		localServiceDetail.fetchNativeProcess(this.pinfo2);
		if ((!localServiceDetail.isValidProcess())
				|| (listContained(localArrayList, localServiceDetail)))
			localArrayList.add(localServiceDetail);
	}

	private void getRunningService_() {
		List localList = ((ActivityManager) getSystemService("activity"))
				.getRunningServices(100);
		ArrayList localArrayList = new ArrayList();
		Iterator localIterator = localList.iterator();
		if (!localIterator.hasNext()) {
			setServiceListAdapter(localArrayList);
		}
		ServiceDetail localServiceDetail = new ServiceDetail(this,
				(ActivityManager.RunningServiceInfo) localIterator.next());
		localServiceDetail.fetchApplicationInfo(this.packageinfo);
		localServiceDetail.fetchPackageInfo();
		if ((!localServiceDetail.isValidProcess())
				|| (listContained(localArrayList, localServiceDetail)))
			localArrayList.add(localServiceDetail);
	}

	public static int getSystemAvailMem(Context paramContext) {
		ActivityManager localActivityManager = (ActivityManager) paramContext
				.getSystemService("activity");
		ActivityManager.MemoryInfo localMemoryInfo = new ActivityManager.MemoryInfo();
		localActivityManager.getMemoryInfo(localMemoryInfo);
		return (int) localMemoryInfo.availMem / 1048576;
	}

	public static int getSystemRunningAppsNum(Context paramContext) {
		int i = 0;
		ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = null;
		String str = "";
		Iterator localIterator = ((ActivityManager) paramContext
				.getSystemService("activity")).getRunningAppProcesses()
				.iterator();
		if (!localIterator.hasNext())
			localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) localIterator
					.next();
		if ((localRunningAppProcessInfo.processName == null)
				|| (isSystemApp(localRunningAppProcessInfo.processName)))
			str = isValidApp(paramContext, localRunningAppProcessInfo);
		if ((str == null) || (ignoreListApp(paramContext, str)))
			i++;
		return i;
	}

	private String getVersionLabel() {
		String s = "";
		try {
			PackageInfo localPackageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 8192);
			if (localPackageInfo != null)
				s = localPackageInfo.versionName;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
		}
		return s;
	}

	private boolean handleSelectedItem(Detail paramDetail) {
		String str = paramDetail.getPackageName();
		int i = 0;
		boolean j = false;
		if (i >= this.selectedApps.size()) {
			if (ignoreListApp(this, str))
				TaskManagerPreferences.addKilledApps(this, str);
			this.selectedApps.add(paramDetail);
			j = true;
		}
		if (((Detail) this.selectedApps.get(i)).getPackageName().equals(str)) {
			TaskManagerPreferences.removeKilledApps(this, str);
			this.selectedApps.remove(i);
			j = false;
		}
		return j;
	}

	public static boolean ignoreListApp(Context paramContext, String paramString) {
		boolean i = false;
		if (paramString == null)
			i = false;
		if (TaskManagerPreferences.specialListContains(paramContext,
				paramString, 1)) {
			i = true;
		}
		return i;
	}

	private void initIgnoreList() {
		if ((!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				"ignore_self", false))
				&& (!ignoreListApp(this, getPackageName()))) {
			addToIgnoreList("com.android.alarmclock", "Alarm Clock");
			addToIgnoreList("com.android.inputmethod.latin", "Latin IME");
			addToIgnoreList("com.android.inputmethod.pinyin", "Chinese IME");
			addToIgnoreList("com.google.android.inputmethod.pinyin",
					"Google Pinyin");
			addToIgnoreList("com.google.android.providers.gmail",
					"Gmail Storage");
			addToIgnoreList("com.google.android.apps.gtalkservice",
					"Gtalk Service");
			addToIgnoreList("com.google.android.googleapps", "GoogleApps");
			addToIgnoreList("com.google.process.gapps", "GoogleApps");
			addToIgnoreList("com.google.android.talk", "Google Talk");
			addToIgnoreList("com.google.android.gm", "Gmail");
			addToIgnoreList("com.android.providers.media", "Media Storage");
			addToIgnoreList("com.timsu.astrid", "Astrid");
			addToIgnoreList("com.android.mms", "MMS");
			addToIgnoreList("com.android.deskclock", "Desk Clock");
			addToIgnoreList("com.weather.Weather", "Weather");
			addToIgnoreList("com.android.calendar", "Calendar");
			addToIgnoreList("com.android.voicedialer", "Voice Dialer");
			addToIgnoreList("android.process.media", "media");
			addToIgnoreList("com.android.providers.calendar",
					"Provider Calendar");
			addToIgnoreList("com.android.clock", "clock");
			addToIgnoreList("com.android.providers.telephony", "Telephony");
			addToIgnoreList("com.android.qualcomm", "qualcomm");
			addToIgnoreList("com.htc.AddProgramWidget", "Rosie Utility");
			addToIgnoreList("jp.aplix.midp", "midp");
			addToIgnoreList("jp.aplix.midp.factory", "midp factory");
			addToIgnoreList("jp.aplix.midp.tools", "midp tools");
			addToIgnoreList("com.svox.pico", "pico");
			addToIgnoreList("com.android.heroled", "heroled");
			addToIgnoreList("com.tmobile.myfaves", "myfaves");
			addToIgnoreList("com.android.music", "music");
			addToIgnoreList("com.htc.android.worldclock", "worldclock");
			addToIgnoreList("com.htc.photo.widgets", "photo widget");
			addToIgnoreList("com.htc.music", "htc music");
			addToIgnoreList("com.htc.album", "htc album");
			addToIgnoreList("com.htc.android.mail", "mail");
			addToIgnoreList("com.htc.elroy.Weather", "weather");
			addToIgnoreList("com.htc.calendar", "calendar");
			addToIgnoreList("com.htc.htctwitter", "twitter");
			addToIgnoreList("com.htc.socialnetwork.accountmanager", "sns");
			addToIgnoreList("com.mclaughlin.HeroLED", "heroLed");
			addToIgnoreList("com.android.vending", "vending");
			addToIgnoreList("com.android.wallpaper", "wallpaper");
			addToIgnoreList("com.android.bluetooth", "bluetooth");
			addToIgnoreList("com.android.calendar", "calendar");
			addToIgnoreList("com.google.android.apps.uploader", "uploader");
			addToIgnoreList("com.google.android.apps.maps:FriendService",
					"friendservice");
			addToIgnoreList("com.motorola.widgetapp.weather", "weather");
			addToIgnoreList("com.motorola.batterymonitor", "batterymonitor");
			addToIgnoreList("com.motorola.android.audioeffect", "audioeffect");
			addToIgnoreList("com.motorola.firewall", "firewall");
			addToIgnoreList("com.motorola.widget.apncontrol", "apncontrol");
			addToIgnoreList("com.motorola.thumbnailservice", "thumbnailservice");
			addToIgnoreList("com.motorola.usb", "usb");
			addToIgnoreList("com.motorola.atcmd", "atcmd");
			addToIgnoreList("com.motorola.android.motophoneportal.androidui",
					"androidui");
			addToIgnoreList(getPackageName(), "EStrongs Task Manager");
			addToIgnoreList("com.estrongs.android.safer",
					"EStrongs Security Manager");
			SharedPreferences.Editor localEditor = PreferenceManager
					.getDefaultSharedPreferences(this).edit();
			localEditor.putBoolean("ignore_self", true);
			localEditor.commit();
		}
	}

	private boolean initShowMemInfo() {
		boolean bool = this.showMemInfo;
		this.showMemInfo = isShowMemoryEnabled();
		return bool ^ this.showMemInfo;
	}

	private void initStatusIcon() {
		if (TaskManagerPreferences.isTMStatusIconEnabled(this)) {
			PreferencesActivity.changeStatusIcon(this, true);
		} else {
			PreferencesActivity.changeStatusIcon(this, false);
		}
	}

	/** @deprecated */
	private void invalidateListView() {
		try {
			this.mHandler.post(new Runnable() {
				public void run() {
					TaskManager.this.currentStat = TaskManager.this.tabHost
							.getCurrentTab();
					if (TaskManager.this.currentStat == 0)
						if (TaskManager.this.appListAdapter != null)
							while (true) {
								if (TaskManager.this.appList.getAdapter() == null) {
									TaskManager.this.appList
											.setAdapter(TaskManager.this.appListAdapter);
									TaskManager.this.appList
											.setOnItemClickListener(TaskManager.this.itemClickListener);
									TaskManager.this.appList
											.setOnItemLongClickListener(TaskManager.this.itemLongClickListener);
									if (TaskManager.this.showMemInfo)
										TaskManager.this.refresh();
								}
								TaskManager.this.selectLastKilledApps();
								TaskManager.this.appListAdapter
										.notifyDataSetChanged();
								int i;
								while (true) {
									i = TaskManager
											.getSystemAvailMem(TaskManager.this);
									if (!TaskManagerPreferences
											.isTMShowBattery(TaskManager.this))
										TaskManager.this.setTitle("avail_mem"
												+ " " + i + "M" + " | "
												+ "avail_battery" + " "
												+ TaskManager.batteryLevel
												+ "%");
									if (TaskManager.this.serviceListAdapter == null)
										if (TaskManager.this.serviceList
												.getAdapter() == null) {
											TaskManager.this.serviceList
													.setAdapter(TaskManager.this.serviceListAdapter);
											if (TaskManager.this.showMemInfo)
												TaskManager.this.refresh();
										}
									TaskManager.this.serviceListAdapter
											.notifyDataSetChanged();
								}
								// TaskManager.this.setTitle(TaskManager.this
								// .getText(2131099656).toString()
								// + " "
								// + i + "M");
							}
				}
			});
		} catch (Exception e) {

		}
	}

	private static boolean isEmptyProcess(
			ActivityManager.RunningAppProcessInfo paramRunningAppProcessInfo) {
		boolean flag;
		if (paramRunningAppProcessInfo.importance == 500)
			flag = true;
		else
			flag = false;
		return flag;
	}

	private static boolean isSystemApp(String paramString) {
		boolean flag;
		if ((paramString.equals("system"))
				|| (paramString.equals("com.android.phone"))
				|| (paramString.equals("android.process.acore"))
				|| (paramString.equals("com.android.inputmethod.latin"))
				|| (paramString.equals("com.htc.launcher"))
				|| (paramString.equals("com.android.launcher2"))
				|| (paramString.equals("com.android.launcher"))
				|| (paramString.equals("com.htc.android.htcime"))
				|| (paramString.equals("com.htc.android.cime"))
				|| (paramString.equals("com.htc.provider"))
				|| (paramString.equals("com.htc.bgp"))
				|| (paramString.equals("com.motorola.process.system")))
			flag = true;
		else
			flag = false;
		return flag;
	}

	private static String isValidApp(Context paramContext,
			ActivityManager.RunningAppProcessInfo paramRunningAppProcessInfo) {
		ApplicationInfo localApplicationInfo = PackagesInfo.getInstance(
				paramContext).getInfo(paramRunningAppProcessInfo.processName);
		String str = localApplicationInfo.packageName;
		return str;
	}

	public static void killAll(Context paramContext, int paramInt) {
		TmWidgetProvider.restartUpdate(paramContext);
		Iterator localIterator = ((ActivityManager) paramContext
				.getSystemService("activity")).getRunningAppProcesses()
				.iterator();
		ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = null;
		String str = "";
		if (!localIterator.hasNext())
			localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) localIterator
					.next();
		if ((localRunningAppProcessInfo.processName == null)
				|| (isSystemApp(localRunningAppProcessInfo.processName)))
			str = isValidApp(paramContext, localRunningAppProcessInfo);
		if ((str == null)
				|| (((paramInt == 2) || (paramInt == 3)) && ((ignoreListApp(
						paramContext, str))
						|| (paramContext.getPackageName().equals(str))
						|| ((paramInt == 3) && (!isEmptyProcess(localRunningAppProcessInfo))) || ((paramInt == 1) && (!KillOnlyListApp(
						paramContext, str))))))
			stopApp(paramContext, str);
	}

	private boolean listContained(ArrayList<ServiceDetail> paramArrayList,
			ServiceDetail paramServiceDetail) {
		boolean flag;
		if ((paramArrayList.size() == 0)
				|| (paramServiceDetail.getPackageName() == null)) {
			flag = false;
		}
		String str = paramServiceDetail.getPackageName();
		for (int j = 0;; j++) {
			if (j >= paramArrayList.size()) {
				flag = false;
			}
			if (!((ServiceDetail) paramArrayList.get(j)).getPackageName()
					.equals(str))
				flag = true;
		}
	}

	private void registerFilters() {
		this.actionFilter = new IntentFilter(
				"com.estrongs.android.taskmanager.ACTION_LOAD_FINISH");
		registerReceiver(this.loadFinishReceiver, this.actionFilter);
		registerBattery(this);
	}

	private void resetView() {
		this.selectedApps.clear();
		this.mHandler.post(new Runnable() {
			public void run() {
				TaskManager.this.currentStat = TaskManager.this.tabHost
						.getCurrentTab();
				if ((TaskManager.this.currentStat == 0)
						&& (TaskManager.this.appListAdapter != null))
					TaskManager.this.appListAdapter.resetView(true);
				if (TaskManager.this.serviceListAdapter != null) {
					TaskManager.this.serviceListAdapter.resetView(true);
				}
			}
		});
	}

	private void selectLastKilledApps() {
		if (!TaskManagerPreferences.isRestoreSelectStatus(this))
			if (this.selectedApps.size() > 0)
				resetView();
		ArrayList localArrayList1;
		do {
			while (this.tabHost.getCurrentTab() != 0)
				this.selectedApps.clear();
			localArrayList1 = TaskManagerPreferences
					.getRecordedKilledApps(this);
		} while (localArrayList1.size() <= 0);
		ListAdapters.AppListAdapter localAppListAdapter = (ListAdapters.AppListAdapter) this.appList
				.getAdapter();
		ArrayList localArrayList2 = localAppListAdapter.getList();
		int i = 0;
		int j;
		AppDetail localAppDetail = null;
		if (i < localArrayList2.size()) {
			j = i;
			localAppDetail = (AppDetail) localArrayList2.get(j);
		}
		if ((localArrayList1.contains(localAppDetail.getPackageName()))
				&& (!ignoreListApp(this, localAppDetail.getPackageName())))

			this.selectedApps.add(localAppDetail);
		this.mHandler.post(new Runnable() {
			public void run() {
				// TaskManager.this.serviceListAdapter.freshView(
				// TaskManager.this.appList.getChildAt(localAppDetail),
				// 2, true);
			}
		});
	}

	private void setAppListAdapter(final ArrayList<AppDetail> paramArrayList) {
		Collections.sort(paramArrayList);
		this.mHandler.post(new Runnable() {
			public void run() {
				if (TaskManager.this.appListAdapter == null)
					TaskManager.this.appListAdapter = new ListAdapters.AppListAdapter(
							TaskManager.this, paramArrayList);
				TaskManager.this.appListAdapter.setList(paramArrayList);
			}
		});
	}

	private void setProgressBarShown(final boolean paramBoolean) {
		this.mHandler.post(new Runnable() {
			public void run() {
				TaskManager.this
						.setProgressBarIndeterminateVisibility(paramBoolean);
			}
		});
	}

	private void setServiceListAdapter(
			final ArrayList<ServiceDetail> paramArrayList) {
		Collections.sort(paramArrayList);
		this.mHandler.post(new Runnable() {
			public void run() {
				if (TaskManager.this.serviceListAdapter == null)
					TaskManager.this.serviceListAdapter = new ListAdapters.ServiceListAdapter(
							TaskManager.this, paramArrayList);
				TaskManager.this.serviceListAdapter.setList(paramArrayList);
			}
		});
	}

	private void setTabVisibility(boolean paramBoolean) {
		TabWidget localTabWidget = (TabWidget) this.tabHost
				.findViewById(16908307);
		FrameLayout localFrameLayout = (FrameLayout) this.tabHost
				.findViewById(16908305);
		if (!paramBoolean) {
			localTabWidget.setVisibility(8);
			localFrameLayout.setPadding(0, 0, 0, 0);
		}
		localTabWidget.setVisibility(0);
	}

	private void showSettingPage() {
		startActivity(new Intent(this, PreferencesActivity.class));
	}

	private void sort() {
		resetView();
		MenuHandler.getSortMenuDialog(this).show();
	}

	public static void stopApp(Context paramContext, String paramString) {
		((ActivityManager) paramContext.getSystemService("activity"))
				.restartPackage(paramString);
	}

	private void unregisterFilters() {
		unregisterReceiver(this.loadFinishReceiver);
		unregisterReceiver(this.batteryReceiver);
	}

	private void updateWidgets() {
		TmWidgetProvider
				.updateWidgets(this, AppWidgetManager.getInstance(this));
	}

	public void addToIgnoreList(String paramString1, String paramString2) {
		TaskManagerPreferences.addToSpecialList(this, paramString1,
				paramString2, 1);
		refresh();
	}

	public void addToKillOnlyList(String paramString1, String paramString2) {
		TaskManagerPreferences.addToSpecialList(this, paramString1,
				paramString2, 2);
		refresh();
	}

	public boolean findModule() {
		boolean flag;
		try {
			ApplicationInfo localApplicationInfo = getPackageManager()
					.getApplicationInfo(this.fexPkgName, 0);
			if (localApplicationInfo != null)
				flag = true;
			else
				flag = false;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			flag = false;
		}
		return flag;
	}

	public PackagesInfo getPackageInfo() {
		return this.packageinfo;
	}

	public boolean isNewVersion() {
		return PreferenceManager.getDefaultSharedPreferences(this).contains(
				"tm_version");
	}

	public boolean isShowMemSettingChanged() {
		return this.isShowMemSettingChanged;
	}

	public boolean isShowMemoryEnabled() {
		return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				"show_mem", false);
	}

	public void killProcesses(boolean paramBoolean) {
		Vector localVector = new Vector();
		ArrayList localArrayList = null;
		if (!paramBoolean) {
			localVector = this.selectedApps;
			if ((paramBoolean) || (this.selectedApps.size() != 0))
				Toast.makeText(this, "kill_process_empty", Toast.LENGTH_SHORT)
						.show();
		}
		ListAdapters.AppListAdapter localAppListAdapter = (ListAdapters.AppListAdapter) this.appList
				.getAdapter();
		if (localAppListAdapter == null)
			localArrayList = localAppListAdapter.getList();
		for (int i = 0; i < localArrayList.size(); i++) {
			AppDetail localAppDetail = (AppDetail) localArrayList.get(i);
			if (ignoreListApp(this, localAppDetail.getPackageName()))
				localVector.add(localAppDetail);
		}
		setProgressBarIndeterminateVisibility(true);
		int j = 0;
		int k = 0;
		if (k >= localVector.size()) {
			localVector.clear();
			if (paramBoolean)
				this.selectedApps.clear();
		}
		if (j == 0)
			if (TmWidgetProvider.getWidgetIds(getBaseContext()).size() != 0)
				stopApp(this, getPackageName());
		Detail localDetail = (Detail) localVector.get(k);
		if (localDetail.getPackageName().equals(getPackageName()))
			j = 1;
		k++;
		stopApp(this, localDetail.getPackageName());
		Toast.makeText(getBaseContext(), "task_kill_all_app",
				Toast.LENGTH_SHORT).show();
		if (paramBoolean)
			Toast.makeText(getBaseContext(), "taskmanager_kill_self_warning",
					Toast.LENGTH_SHORT).show();
		this.appListAdapter.resetView(false);
		refresh();
	}

	public void onConfigurationChanged(Configuration paramConfiguration) {
		super.onConfigurationChanged(paramConfiguration);
	}

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		requestWindowFeature(5);
		version = getVersionLabel();
		System.out.println("Truong 1");
		if (!TaskManagerPreferences.getVersion(this).equals(version)) {
			// showDialog(101);
			setVersion();
			System.out.println("Truong 2");
		}
		
		this.tabHost = getTabHost();
		this.tabHost.setup();
		LayoutInflater.from(this).inflate(R.layout.taskmanager_main,
				this.tabHost.getTabContentView(), true);
//		this.tabHost.addTab(this.tabHost
//				.newTabSpec("Applications")
//				.setIndicator(getResources().getText(R.id.tv1),
//						getResources().getDrawable(R.drawable.ic_launcher))
//				.setContent(2131165201));
//		this.tabHost.addTab(this.tabHost
//				.newTabSpec("Services")
//				.setIndicator(getResources().getString(R.id.tv2),
//						getResources().getDrawable(R.drawable.image_browser))
//				.setContent(2131165205));
		// setAds();
		if (OSInfo.sdkVersion() >= 8)
			this.version2_2 = true;
		else
			this.version2_2 = false;
		registerFilters();
		System.out.println("Truong 3");
		((Button) findViewById(R.id.btKillSelect))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View paramView) {
						TaskManager.this.killProcesses(false);
					}
				});
		((Button) findViewById(R.id.btRefresh))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View paramView) {
						TaskManager.this.refresh();
					}
				});
		((Button) findViewById(R.id.btKillAll))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View paramView) {
						TaskManager.this.killProcesses(true);
						TaskManager.this.finish();
					}
				});
		this.tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String paramString) {
				if (TaskManager.this.tabHost.getCurrentTab() == 0)
					TaskManager.this.currentStat = 0;
				TaskManager.this.refresh();
				if (TaskManager.this.tabHost.getCurrentTab() != 1)
					TaskManager.this.currentStat = 1;
			}
		});
		this.appList = ((ListView) findViewById(R.id.listTaskApp));
//		this.serviceList = ((ListView) findViewById(R.id.ll1));
		View localView1 = findViewById(R.id.listTaskApp);
//		View localView2 = findViewById(R.id.ll2);
		this.appList.setEmptyView(localView1);
//		this.serviceList.setEmptyView(localView2);
		initStatusIcon();
		initIgnoreList();
		this.show_tab = PreferenceManager.getDefaultSharedPreferences(this)
				.getBoolean("show_tab", false);
		this.show_ignorelist = PreferenceManager.getDefaultSharedPreferences(
				this).getBoolean("show_ignorelist", false);
		setTabVisibility(this.show_tab);
		refresh();
		updateWidgets();
		System.out.println("truong 4");
		return;
	}

	protected AlertDialog onCreateDialog(int paramInt) {
		AlertDialog localAlertDialog;
		switch (paramInt) {
		default:
		case 2:
			View localView;
			for (localAlertDialog = null;; localAlertDialog = new AlertDialog.Builder(
					this)
					.setIcon(17301659)
					.setTitle(2131099648)
					.setView(localView)
					.setPositiveButton(2131099703,
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramDialogInterface,
										int paramInt) {
									TaskManager.this.findApplication(
											TaskManager.this.taskMgrPkgName,
											"pname");
								}
							})
					.setNegativeButton(2131099704,
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramDialogInterface,
										int paramInt) {
									TaskManager.this.findApplication(
											TaskManager.this.estrongsName,
											"pub");
								}
							}).create()) {
				localView = ((LayoutInflater) getSystemService("layout_inflater"))
						.inflate(2130903040, null);
				((ImageView) localView.findViewById(2131165186))
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View paramView) {
								Intent localIntent = new Intent(
										"android.intent.action.VIEW",
										Uri.parse("http://"
												+ TaskManager.sponsorUrl));
								TaskManager.this.startActivity(localIntent);
							}
						});
				((TextView) localView.findViewById(2131165185))
						.setText(sponsorUrl);
				TextView localTextView = (TextView) localView
						.findViewById(2131165184);
				String str3 = getVersionLabel();
				if (str3 == null)
					str3 = "1.x";
				localTextView.setText(getText(2131099651) + " " + str3);
			}
		case 1:
			String str1 = getString(2131099698);
			String str2 = null;
			if (!findModule())
				str2 = str1 + getString(2131099742);
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(this)
					.setTitle(getString(2131099651) + ":" + version)
					.setMessage(str2)
					.setPositiveButton(2131099740,
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramDialogInterface,
										int paramInt) {
									TaskManager.this.findApplication(
											TaskManager.this.estrongsName,
											"pub");
								}
							})
					.setNegativeButton(2131099741,
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramDialogInterface,
										int paramInt) {
								}
							});
			localBuilder = new AlertDialog.Builder(this)
					.setTitle(getString(2131099651) + ":" + version)
					.setMessage(str1)
					.setPositiveButton(2131099705,
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramDialogInterface,
										int paramInt) {
								}
							});
			localAlertDialog = localBuilder.create();
			break;
		}
		return localAlertDialog;
	}

	public boolean onCreateOptionsMenu(Menu paramMenu) {
		paramMenu.add(0, 2, 0, getText(2131099650)).setShortcut('1', 's')
				.setIcon(R.drawable.image_browser);
		paramMenu.add(0, 5, 0, getText(2131099666)).setShortcut('4', 't')
				.setIcon(R.drawable.image_browser);
		this.tab_Item = paramMenu.add(0, 6, 0, getText(2131099663))
				.setShortcut('5', 'h').setIcon(R.drawable.image_browser);
		this.menu_ignore = paramMenu.add(0, 7, 0, getText(2131099717))
				.setShortcut('6', 'i').setIcon(R.drawable.image_browser);
		paramMenu.add(0, 3, 0, getText(2131099649)).setShortcut('2', 'a')
				.setIcon(R.drawable.image_browser);
		paramMenu.add(0, 4, 0, getText(2131099691)).setShortcut('3', 'e')
				.setIcon(R.drawable.image_browser);
		return true;
	}

	protected void onDestroy() {
		unregisterFilters();
		if ((this.freshThread != null) && (this.freshThread.isAlive()))
			this.freshThread.interrupt();
		super.onDestroy();
	}

	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		default:
		case 2:
			break;
		case 3:
			break;
		case 4:
			showSettingPage();
			showDialog(2);
			exit();
			break;
		case MenuHandler.MENU_SWITCH:
			if (this.showMemInfo)
				sort();
			sortType(0);
			break;
		case 6:
			if (this.tabHost.getCurrentTab() == 1) {
				this.currentStat = 0;
				this.tab_Item.setTitle("Tab Host");
				this.tab_Item.setIcon(R.drawable.about);
				this.tabHost.setCurrentTab(0);
			}
			refresh();
			if (this.tabHost.getCurrentTab() != 0)
				if (!this.version2_2) {
					this.currentStat = 1;
					this.tab_Item.setTitle("Tab Host");
					this.tab_Item.setIcon(R.drawable.about);
					this.tabHost.setCurrentTab(1);
				}
			Intent localIntent = new Intent();
			localIntent.setClassName("com.android.settings",
					"com.android.settings.RunningServices");
			startActivity(localIntent);
			break;
		case MenuHandler.MENU_IGNORE:
			if (this.show_ignorelist)
				this.show_ignorelist = true;
			else
				this.show_ignorelist = false;
			SharedPreferences.Editor localEditor = PreferenceManager
					.getDefaultSharedPreferences(this).edit();
			localEditor.putBoolean("show_ignorelist", this.show_ignorelist);
			localEditor.commit();
			refresh();
			break;
		}
		return true;
	}

	public boolean onPrepareOptionsMenu(Menu paramMenu) {
		super.onPrepareOptionsMenu(paramMenu);
		if (this.tabHost.getCurrentTab() == 0) {
			this.currentStat = 0;
			this.tab_Item.setTitle("tab_label_service");
			this.tab_Item.setIcon(R.drawable.ic_launcher);
			if (!this.show_ignorelist)
				this.menu_ignore.setTitle("menu_hide_ignorelist");
		}
		while (true) {
			refresh();
			if (this.tabHost.getCurrentTab() != 1)
				this.currentStat = 1;
			this.tab_Item.setTitle("tab_label_app");
			this.tab_Item.setIcon(R.drawable.taskmanager_icon_small);
			this.menu_ignore.setTitle("menu_show_ignorelist");
		}
	}

	protected void onResume() {
		super.onResume();
		this.isShowMemSettingChanged = initShowMemInfo();
		refresh();
	}

	/** @deprecated */
	public void refresh() {
		try {
			this.currentStat = this.tabHost.getCurrentTab();
			if (this.currentStat == 0) {
				boolean bool = this.get_proc_running;
				if (!bool)
					;
			}
			while (true) {
				if ((this.currentStat == 1) && (this.get_service_running))
					setProgressBarShown(true);
				this.freshThread = new Thread(new Runnable() {
					public void run() {
						TaskManager.this.currentStat = TaskManager.this.tabHost
								.getCurrentTab();
						if (TaskManager.this.packageinfo == null)
							TaskManager.this.packageinfo = PackagesInfo
									.getInstance(TaskManager.this);
						if (TaskManager.this.currentStat == 0) {
							TaskManager.this.get_proc_running = true;
							if ((TaskManager.this.appList.getAdapter() == null)
									|| (!TaskManager.this.showMemInfo))
								TaskManager.this.getRunningProcess_();
							while (true) {
								TaskManager.this.get_proc_running = false;
								Intent localIntent = new Intent(
										"com.estrongs.android.taskmanager.ACTION_LOAD_FINISH");
								TaskManager.this.sendBroadcast(localIntent);
								TaskManager.this.getRunningProcessDetail();
							}
						}
						TaskManager.this.get_service_running = true;
						if ((TaskManager.this.serviceList.getAdapter() == null)
								|| (!TaskManager.this.showMemInfo))
							TaskManager.this.getRunningService_();
						while (true) {
							TaskManager.this.get_service_running = false;
							TaskManager.this.getRunningServiceDetail();
						}
					}
				});
				this.freshThread.start();
			}
		} catch (Exception e) {

		}
	}

	public void registerBattery(Context paramContext) {
		IntentFilter localIntentFilter = new IntentFilter(
				"android.intent.action.BATTERY_CHANGED");
		registerReceiver(this.batteryReceiver, localIntentFilter);
	}

	public void selectAll() {
		if (this.tabHost.getCurrentTab() == 1)
			while (true) {
				this.selectedApps.clear();
				ListAdapters.AppListAdapter localAppListAdapter = (ListAdapters.AppListAdapter) this.appList
						.getAdapter();
				ArrayList localArrayList = localAppListAdapter.getList();
				for (int i = 0; i < localArrayList.size(); i++) {
					int j = i;
					AppDetail localAppDetail = (AppDetail) localArrayList
							.get(j);
					if (!ignoreListApp(this, localAppDetail.getPackageName())) {
						this.selectedApps.add(localAppDetail);
						TaskManagerPreferences.addKilledApps(this,
								localAppDetail.getPackageName());
					}
					// this.mHandler.post(new Runnable() {
					// public void run() {
					// this.freshView(TaskManager.this.appList
					// .getChildAt(1), this,
					// true);
					// }
					// });
				}
			}
	}

	public void setAdSense(boolean paramBoolean) {
		Uri localUri = Uri.parse("content://"
				+ "com.estrongs.android.provider.taskmanager"
				+ "/infoTable/50109D");
		ContentResolver localContentResolver = getContentResolver();
		ContentValues localContentValues = new ContentValues();
		if (paramBoolean)
			localContentValues.put("value", "true");
		while (true) {
			localContentResolver.update(localUri, localContentValues, null,
					null);
			localContentValues.put("value", "false");
		}
	}

	// public void setAds() {
	// GoogleAdView localGoogleAdView = (GoogleAdView) findViewById(2131165202);
	// AdSenseSpec localAdSenseSpec = new AdSenseSpec("pub-9731973158457831")
	// .setCompanyName("EStrongs Inc.")
	// .setAppName("EStrongs Task Manager").setChannel("8151014844")
	// .setColorBorder("#000000")
	// .setExpandDirection(AdSenseSpec.ExpandDirection.BOTTOM)
	// .setAdTestEnabled(false);
	// switch (new Random().nextInt(10)) {
	// default:
	// localAdSenseSpec.setKeywords("tools");
	// localGoogleAdView.setVisibility(8);
	// if (!hasAd)
	// setAdSense(hasAd);
	// if (!TaskManagerPreferences.isTMHasAdSense(this))
	// break;
	// localGoogleAdView.showAds(localAdSenseSpec);
	// case 1:
	// localAdSenseSpec.setKeywords("task manager");
	// break;
	// case 2:
	// localAdSenseSpec.setKeywords("memory");
	// break;
	// case 3:
	// localAdSenseSpec.setKeywords("android applications");
	// break;
	// case 4:
	// localAdSenseSpec.setKeywords("productivity");
	// break;
	// case 5:
	// localAdSenseSpec.setKeywords("project manager");
	// break;
	// case 6:
	// localAdSenseSpec.setKeywords("software management");
	// break;
	// case 7:
	// localAdSenseSpec.setKeywords("file manager");
	// break;
	// case 8:
	// localAdSenseSpec.setKeywords("battery life");
	// break;
	// case 9:
	// localAdSenseSpec.setKeywords("security");
	// break;
	// }
	// }

	public void setVersion() {
		SharedPreferences.Editor localEditor = PreferenceManager
				.getDefaultSharedPreferences(this).edit();
		localEditor.putString("tm_version", getVersionLabel());
		localEditor.commit();
	}

	public void sortType(int paramInt) {
		SharedPreferences.Editor localEditor = PreferenceManager
				.getDefaultSharedPreferences(this).edit();
		localEditor.putInt("taskmanager_list", paramInt);
		localEditor.commit();
		setProgressBarShown(true);
		new Thread(new Runnable() {
			public void run() {
				TaskManager.this.currentStat = TaskManager.this.tabHost
						.getCurrentTab();
				if (TaskManager.this.currentStat == 0) {
					ArrayList localArrayList2 = ((ListAdapters.AppListAdapter) TaskManager.this.appList
							.getAdapter()).getList();
					TaskManager.this.setAppListAdapter(localArrayList2);
				}
				while (true) {
					Intent localIntent = new Intent(
							"com.explorer.taskmanager.ACTION_LOAD_FINISH");
					TaskManager.this.sendBroadcast(localIntent);
					ArrayList localArrayList1 = ((ListAdapters.ServiceListAdapter) TaskManager.this.serviceList
							.getAdapter()).getList();
					TaskManager.this.setServiceListAdapter(localArrayList1);
				}
			}
		}).start();
	}

	public void sendBroadcast(Intent localIntent) {
		// TODO Auto-generated method stub

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private class BatteryReceiver extends BroadcastReceiver {
		private BatteryReceiver() {
		}

		public void onReceive(Context paramContext, Intent paramIntent) {
			int i = 0;
			if (OSInfo.sdkVersion() < 5)
				i = paramIntent.getIntExtra("level", -1);
			for (int j = paramIntent.getIntExtra("scale", -1);; j = paramIntent
					.getIntExtra("scale", -1)) {
				if ((i >= 0) && (j > 0))
					TaskManager.batteryLevel = i * 100 / j;
				i = paramIntent.getIntExtra("level", -1);
			}
		}
	}

	private class LoadFinishReceiver extends BroadcastReceiver {
		private LoadFinishReceiver() {
		}

		public void onReceive(Context paramContext, Intent paramIntent) {
			TaskManager.this.setProgressBarShown(false);
			TaskManager.this.invalidateListView();
		}
	}

}
