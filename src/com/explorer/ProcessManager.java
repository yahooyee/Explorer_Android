package com.explorer;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * (c) D09CN2 - PTIT - Ha Noi 
 * (c) DROIDSX
 *
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 *	@since 11:48:56 PM  Aug 28, 2012
 *	Tel: 0974 878 244
 * 
 */
public class ProcessManager extends ListActivity{
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private final int CONVERT = 1024;
	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	private PackageManager pk;
	private List<RunningAppProcessInfo> display_process;
	private ActivityManager activity_man;
	private MyListAdapter delegate;
	private TextView availMem_label, numProc_label;
	// ===========================================================
	// Constructors
	// ===========================================================

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Getter & Setter
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	//----------------------------------------------------------------------------------------------
	// Methods for/from SuperClass/Interfaces
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.manager_layout);		
		pk = getPackageManager();
		
		availMem_label = (TextView)findViewById(R.id.available_mem_label);
		numProc_label = (TextView)findViewById(R.id.num_processes_label);
		activity_man = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		
		display_process = new ArrayList<RunningAppProcessInfo>();
		update_list();
		
		delegate = new MyListAdapter();
		setListAdapter(delegate);
	}
	
	@Override
	protected void onListItemClick(ListView parent, View view, int position, long id) {
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		CharSequence[] options = {"Details", "Launch"};
		final int index = position;
		
		builder.setTitle("Process options");
		
		try {
			builder.setIcon(pk.getApplicationIcon(display_process.get(position).processName));
			
		} catch (NameNotFoundException e) {
			builder.setIcon(R.drawable.processinfo);
		}
		
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int choice) {
				
				switch (choice) {				
					case 0:
						Toast.makeText(ProcessManager.this, display_process.get(index).processName,
								   Toast.LENGTH_SHORT).show();
						break;
						
					case 1:
						Intent i = pk.getLaunchIntentForPackage(display_process.get(index).processName);
						
						if(i != null)
							startActivity(i);
						else
							Toast.makeText(ProcessManager.this, "Could not launch", Toast.LENGTH_SHORT).show();	
						
						break;
				}
			}
		});
		
		dialog = builder.create();
		dialog.show();
	}
	// ===========================================================
	// Methods
	// ===========================================================
	private void update_labels() {
		MemoryInfo mem_info;
		double mem_size;
		
		mem_info = new ActivityManager.MemoryInfo();
		activity_man.getMemoryInfo(mem_info);
		mem_size = (mem_info.availMem / (CONVERT * CONVERT));		
		
		availMem_label.setText(String.format("Available memory:\t %.2f Mb", mem_size));
		numProc_label.setText("Number of processes:\t " + display_process.size());
	}
	
	private void update_list() {
		List<RunningAppProcessInfo> total_process = activity_man.getRunningAppProcesses();
		int len;
		
		total_process = activity_man.getRunningAppProcesses();
		len = total_process.size();
		
		for (int i = 0; i < len; i++){
			if(total_process.get(i).importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
			    total_process.get(i).importance != RunningAppProcessInfo.IMPORTANCE_SERVICE)
				display_process.add(total_process.get(i));
		}
		
		update_labels();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
private class MyListAdapter extends ArrayAdapter<RunningAppProcessInfo> {
		
		public MyListAdapter() {
			super(ProcessManager.this, R.layout.tablerow, display_process);
		}
		
		private String parse_name(String pkgName) {
			String[] items = pkgName.split("\\.");
			String name = "";
			int len = items.length;
			
			for (int i = 0; i < len; i++){
				if(!items[i].equalsIgnoreCase("com") && !items[i].equalsIgnoreCase("android") &&
				   !items[i].equalsIgnoreCase("google") && !items[i].equalsIgnoreCase("process") &&
				   !items[i].equalsIgnoreCase("htc") && !items[i].equalsIgnoreCase("coremobility"))
					name = items[i];
			}		
			return name;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			String pkg_name = display_process.get(position).processName;
				
			if(view == null) {
				LayoutInflater inflater = getLayoutInflater();
				view = inflater.inflate(R.layout.tablerow, parent, false);
			}
				
			TextView bottom_label = (TextView)view.findViewById(R.id.bottom_view);
			TextView top_label = (TextView)view.findViewById(R.id.top_view);
			ImageView icon = (ImageView)view.findViewById(R.id.row_image);
			icon.setAdjustViewBounds(true);
			icon.setMaxHeight(40);
			
			top_label.setText(parse_name(pkg_name));
			bottom_label.setText(String.format("%s, pid: %d",
							display_process.get(position).processName, display_process.get(position).pid));
			
			try {
				icon.setImageDrawable(pk.getApplicationIcon(pkg_name));
				
			} catch (NameNotFoundException e) {
				icon.setImageResource(R.drawable.processinfo);
			}
			
			return view;			
		}
	}
}
