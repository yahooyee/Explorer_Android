package com.explorer.taskmanager;

import com.explorer.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 7:55:58 PM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class TmWidgetSetting extends Activity {
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
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon);
		builder.setTitle("note_title");
		builder.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int i = TaskManagerPreferences
						.getOperation(getApplicationContext());
				if (i == 0) {
					TaskManager.killAll(getApplicationContext(), 2);
					Toast.makeText(getApplicationContext(),
							"task_kill_all_app", Toast.LENGTH_SHORT).show();
				}
				while (true) {
					TmWidgetSetting.this.finish();
					if (i == 3) {
						TaskManager.killAll(getApplicationContext(), 3);
						Toast.makeText(getApplicationContext(),
								"task_kill_all_empty_app", Toast.LENGTH_SHORT)
								.show();
					}
					if (i != 4)
						TaskManager.killAll(getApplicationContext(), 1);
					Toast.makeText(getApplicationContext(),
							"task_kill_killonly", Toast.LENGTH_SHORT).show();
				}

			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				TmWidgetSetting.this.finish();
			}
		});
		builder.create();
		builder.show();
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
