package com.explorer.taskmanager;

import com.explorer.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 1:52:49 AM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class PreferencesActivity extends PreferenceActivity {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static final String DONATE_PAGE = "http://www.estrongs.com/contact/donation.html";
	public static final String FAQ_PAGE = "http://www.estrongs.com/faqs.html";
	static final String PREF_DONATE = "donate";
	static final String PREF_FAQ = "faq";
	static final String PREF_SHORTCUT = "shortcut";
	static final String PREF_TM_AUTO_KILL = "auto_kill";
	static final String PREF_TM_AUTO_START = "auto_start";
	static final String PREF_TM_STAT_ENABLED = "stat_enabled";
	private static final int _id = 10000;
	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	CheckBoxPreference mAutoKillPreference;
	CheckBoxPreference mAutoStartPreference;
	Preference mDonatePreference;
	Preference mFAQPreference;
	CheckBoxPreference mShortCutPreference;
	CheckBoxPreference mStatEnablePreference;
	CheckBoxPreference mSuperUserPreference;

	// ===========================================================
	// Constructors
	// ===========================================================
	public PreferencesActivity() {
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Getter & Setter
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ----------------------------------------------------------------------------------------------
	// Methods for/from SuperClass/Interfaces
	// ----------------------------------------------------------------------------------------------
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		addPreferencesFromResource(R.xml.tm_preferences);
		this.mAutoKillPreference = ((CheckBoxPreference) findPreference("auto_kill"));
		if ((this.mAutoKillPreference.isChecked())
				&& (TmWidgetProvider.getWidgetIds(getBaseContext()).size() == 0)) {
			new android.app.AlertDialog.Builder(this)
					.setTitle("note_title")
					.setMessage("auto_kill_note_text")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramDialogInterface,
										int paramInt) {
								}
							}).show();
			this.mAutoKillPreference.setChecked(false);
		}
		this.mAutoKillPreference
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(
							Preference paramPreference, Object paramObject) {
						if ((((Boolean) paramObject).booleanValue())
								&& (TmWidgetProvider.getWidgetIds(
										PreferencesActivity.this
												.getBaseContext()).size() == 0))
							new android.app.AlertDialog.Builder(
									PreferencesActivity.this)
									.setTitle("note_title")
									.setMessage("auto_kill_note_text")
									.setPositiveButton(
											"Cancel",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface paramDialogInterface,
														int paramInt) {
												}
											}).show();
						return true;
					}
				});
		this.mStatEnablePreference = ((CheckBoxPreference) findPreference("stat_enabled"));
		this.mStatEnablePreference
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(
							Preference paramPreference, Object paramObject) {
						boolean bool = ((Boolean) paramObject).booleanValue();
						if (!bool) {
							PreferencesActivity.this.mAutoStartPreference
									.setChecked(false);
							PreferencesActivity.enableAutoStart(
									PreferencesActivity.this, false);
						}
						PreferencesActivity.changeStatusIcon(
								PreferencesActivity.this, bool);
						return true;
					}
				});
		changeStatusIcon(this, this.mStatEnablePreference.isChecked());
		this.mAutoStartPreference = ((CheckBoxPreference) findPreference("auto_start"));
		this.mAutoStartPreference
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(
							Preference paramPreference, Object paramObject) {
						boolean bool = ((Boolean) paramObject).booleanValue();
						if (bool) {
							PreferencesActivity.this.mStatEnablePreference
									.setChecked(true);
							PreferencesActivity.changeStatusIcon(
									PreferencesActivity.this, true);
						}
						PreferencesActivity.enableAutoStart(
								PreferencesActivity.this, bool);
						return true;
					}
				});
		this.mShortCutPreference = ((CheckBoxPreference) findPreference("shortcut"));
		this.mShortCutPreference
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(
							Preference paramPreference, Object paramObject) {
						if (((Boolean) paramObject).booleanValue())
							ShortcutCreater
									.createTaskManagerIcon(PreferencesActivity.this);
						while (true) {
							ShortcutCreater
									.removeTaskManagerIcon(PreferencesActivity.this);
						}
					}
				});
		this.mFAQPreference = findPreference("faq");
		this.mFAQPreference
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference paramPreference) {
						Intent localIntent = new Intent(
								"android.intent.action.VIEW",
								Uri.parse("http://www.estrongs.com/faqs.html"));
						PreferencesActivity.this.startActivity(localIntent);
						return true;
					}
				});
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public static void changeStatusIcon(Context context, boolean flag) {
		NotificationManager notificationmanager = (NotificationManager) context
				.getSystemService("notification");
		notificationmanager.cancel(10000);
		if (flag) {
			Notification notification = new Notification();
			notification.icon = R.drawable.taskmanager_icon_small;
			notification.flags = 4 | notification.flags;
			notification.flags = 0x20 | notification.flags;
			Intent intent = new Intent(
					"android.intent.action.PICK_TASK_MANAGER");
			intent.setFlags(R.drawable.pressed_application_background);
			PendingIntent pendingintent = PendingIntent.getActivity(context, 0,
					intent, 0);
			notification.setLatestEventInfo(context, "Task manager",
					"File manager", pendingintent);
			notification.when = System.currentTimeMillis();
			notificationmanager.notify(10000, notification);
		}
	}

	public static void enableAutoStart(Context context, boolean flag) {
		PackageManager packagemanager = context.getPackageManager();
		ComponentName componentname = new ComponentName(context,
				AutoStartReceiver.class);
		if (flag)
			packagemanager.setComponentEnabledSetting(componentname, 0, 1);
		else
			packagemanager.setComponentEnabledSetting(componentname, 2, 1);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
