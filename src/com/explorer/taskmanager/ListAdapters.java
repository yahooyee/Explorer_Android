package com.explorer.taskmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.explorer.R;
import com.explorer.taskmanager.NativeProcessInfo.NativeProcess;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 2:47:20 AM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class ListAdapters {
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

	// ===========================================================
	// Methods
	// ===========================================================

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static final class AppListAdapter extends BaseAdapter {
		private HashMap<String, Boolean> ignoreStatusMap = new HashMap();
		private ArrayList<AppDetail> list;
		private LayoutInflater mInflater;
		String str;
		private HashMap<String, Boolean> selectStatusMap = new HashMap();
		private TaskManager taskManager = new TaskManager();

		public AppListAdapter(TaskManager paramTaskManager,
				ArrayList<AppDetail> paramArrayList) {
			this.mInflater = LayoutInflater.from(paramTaskManager);
			this.list = paramArrayList;
			this.taskManager = paramTaskManager;
			this.selectStatusMap.clear();
		}

		public void freshView(View paramView, int paramInt, boolean paramBoolean) {

			if (paramInt >= this.list.size()) {
				while (true) {
					str = ((AppDetail) this.list.get(paramInt))
							.getPackageName();
					if (paramView != null)
						this.selectStatusMap.put(str,
								Boolean.valueOf(paramBoolean));
				}
			}
			if ((paramBoolean)
					&& (!TaskManager.ignoreListApp(this.taskManager, str))) {
				paramView
						.setBackgroundResource(R.drawable.pressed_application_background);
				this.selectStatusMap.put(str, Boolean.valueOf(true));
			}
			paramView.invalidate();
			paramView.setBackgroundColor(Color.BLUE);
			this.selectStatusMap.put(str, Boolean.valueOf(false));
		}

		public int getCount() {
			return this.list.size();
		}

		public Object getItem(int paramInt) {
			return this.list.get(paramInt);
		}

		public long getItemId(int paramInt) {
			return paramInt;
		}

		public ArrayList<AppDetail> getList() {
			return this.list;
		}

		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			boolean bool1 = this.taskManager.isShowMemSettingChanged();
			boolean bool2 = this.taskManager.isShowMemoryEnabled();
			ListAdapters.ViewHolder localViewHolder;
			NativeProcessInfo.NativeProcess localNativeProcess = new NativeProcess(
					taskManager, str, bool1);
			if ((paramView == null) || (bool1))
				if (bool2) {
					paramView = this.mInflater.inflate(
							R.layout.taskmanager_list_item_view, null);
					localViewHolder = new ListAdapters.ViewHolder();
					localViewHolder.icon = ((ImageView) paramView
							.findViewById(R.id.imvIcon));
					localViewHolder.text_name = ((TextView) paramView
							.findViewById(R.id.tvTextItem));
					if (bool2) {
						localViewHolder.text_pid = ((TextView) paramView
								.findViewById(R.id.tvTextItem2));
						localViewHolder.text_cpu = ((TextView) paramView
								.findViewById(R.id.tvTextItem3));
						localViewHolder.text_mem = ((TextView) paramView
								.findViewById(R.id.tvTextItem4));
					}
					localViewHolder.text_visible = ((TextView) paramView
							.findViewById(R.id.tvTextItem5));
					paramView.setTag(localViewHolder);
					AppDetail localAppDetail = (AppDetail) this.list
							.get(paramInt);
					str = localAppDetail.getPackageName();
					if (!TaskManager.ignoreListApp(this.taskManager, str))
						this.ignoreStatusMap.put(str, Boolean.valueOf(true));
					if ((this.selectStatusMap.size() <= 0)
							|| (!this.selectStatusMap.containsKey(str))
							|| (!((Boolean) this.selectStatusMap.get(str))
									.booleanValue())
							|| (this.ignoreStatusMap.containsKey(str)))
						paramView
								.setBackgroundResource(R.drawable.pressed_application_background);
					localViewHolder.icon.setImageDrawable(localAppDetail
							.getIcon());
					localViewHolder.text_name
							.setText(localAppDetail.getTitle());
					localViewHolder.text_visible.setText(localAppDetail
							.getVisible());
					if (!this.ignoreStatusMap.containsKey(str))
						localViewHolder.text_name.setTextColor(Color.CYAN);
					localViewHolder.icon.setColorFilter(Color.CYAN);
					localViewHolder.text_visible.setTextColor(Color.CYAN);
					localNativeProcess = localAppDetail.getNativeProcess();
					if (bool2) {
						if (localNativeProcess != null)
							localViewHolder.text_pid.setText(this.taskManager
									.getText(R.id.tvTextItem2));
						localViewHolder.text_cpu.setText(this.taskManager
								.getText(R.id.tvTextItem3));
						localViewHolder.text_mem.setText(this.taskManager
								.getText(R.id.tvTextItem4));
						if (!this.ignoreStatusMap.containsKey(str))
							localViewHolder.text_pid.setTextColor(Color.CYAN);
					}
				}
			paramView = this.mInflater.inflate(
					R.layout.taskmanager_list_item_view_simple, null);
			localViewHolder = (ListAdapters.ViewHolder) paramView.getTag();
			if (!this.ignoreStatusMap.containsKey(str))
				this.ignoreStatusMap.remove(str);
			paramView.setBackgroundColor(Color.BLUE);
			localViewHolder.text_name.setTextColor(Color.BLUE);
			localViewHolder.icon.setColorFilter(null);
			localViewHolder.text_visible.setTextColor(Color.BLUE);
			localViewHolder.text_pid.setText(this.taskManager
					.getText(R.id.tvTextItem2) + localNativeProcess.pid);
			localViewHolder.text_cpu.setText(this.taskManager
					.getText(R.id.tvTextItem3) + localNativeProcess.cpu);
			localViewHolder.text_mem.setText(this.taskManager
					.getText(R.id.tvTextItem4) + localNativeProcess.mem);
			localViewHolder.text_pid.setTextColor(Color.BLUE);
			return paramView;
		}

		public void resetView(boolean paramBoolean) {
			if (paramBoolean)
				notifyDataSetChanged();
			this.selectStatusMap.clear();
		}

		public void setIngoreListApp(String paramString) {
			this.ignoreStatusMap.put(paramString, Boolean.valueOf(true));
		}

		public void setList(ArrayList<AppDetail> paramArrayList) {
			this.list = paramArrayList;
		}
	}

	public static final class ServiceListAdapter extends BaseAdapter {
		private Vector<View> cachedViews = new Vector();
		private ArrayList<ServiceDetail> list;
		private LayoutInflater mInflater;
		private HashMap<String, Boolean> selectStatusMap = new HashMap();
		private TaskManager taskManager = null;

		public ServiceListAdapter(TaskManager paramTaskManager,
				ArrayList<ServiceDetail> paramArrayList) {
			this.mInflater = LayoutInflater.from(paramTaskManager);
			this.list = paramArrayList;
			this.taskManager = paramTaskManager;
			this.selectStatusMap.clear();
		}

		String str;

		public void freshView(View paramView, int paramInt, boolean paramBoolean) {
			if (paramInt >= this.list.size())
				str = ((ServiceDetail) this.list.get(paramInt))
						.getPackageName();
			if (paramView != null)
				this.selectStatusMap.put(str, Boolean.valueOf(paramBoolean));
			if ((paramBoolean)
					&& (!TaskManager.ignoreListApp(this.taskManager, str))) {
				paramView
						.setBackgroundResource(R.drawable.pressed_application_background);
				this.selectStatusMap.put(str, Boolean.valueOf(true));
			}
			paramView.invalidate();
			this.cachedViews.add(paramView);
			paramView.setBackgroundColor(Color.RED);
			this.selectStatusMap.put(str, Boolean.valueOf(false));
		}

		public int getCount() {
			return this.list.size();
		}

		public Object getItem(int paramInt) {
			return this.list.get(paramInt);
		}

		public long getItemId(int paramInt) {
			return paramInt;
		}

		public ArrayList<ServiceDetail> getList() {
			return this.list;
		}

		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			boolean bool1 = this.taskManager.isShowMemSettingChanged();
			boolean bool2 = this.taskManager.isShowMemoryEnabled();
			ListAdapters.ViewHolder localViewHolder;
			NativeProcessInfo.NativeProcess localNativeProcess = new NativeProcess(
					taskManager, str, bool2);
			if ((paramView == null) || (bool1))
				if (bool2) {
					paramView = this.mInflater.inflate(
							R.layout.taskmanager_list_item_view, null);
					localViewHolder = new ListAdapters.ViewHolder();
					localViewHolder.icon = ((ImageView) paramView
							.findViewById(R.id.imvIcon));
					localViewHolder.text_name = ((TextView) paramView
							.findViewById(R.id.tvTextItem));
					if (bool2) {
						localViewHolder.text_pid = ((TextView) paramView
								.findViewById(R.id.tvTextItem2));
						localViewHolder.text_cpu = ((TextView) paramView
								.findViewById(R.id.tvTextItem3));
						localViewHolder.text_mem = ((TextView) paramView
								.findViewById(R.id.tvTextItem4));
					}
					localViewHolder.text_visible = ((TextView) paramView
							.findViewById(R.id.tvTextItem5));
					paramView.setTag(localViewHolder);
					ServiceDetail localServiceDetail = (ServiceDetail) this.list
							.get(paramInt);
					if ((this.selectStatusMap.size() <= 0)
							|| (!this.selectStatusMap
									.containsKey(localServiceDetail
											.getPackageName()))
							|| (!((Boolean) this.selectStatusMap
									.get(localServiceDetail.getPackageName()))
									.booleanValue()))
						paramView
								.setBackgroundResource(R.drawable.pressed_application_background);
					localViewHolder.icon.setImageDrawable(localServiceDetail
							.getIcon());
					localViewHolder.text_name.setText(localServiceDetail
							.getTitle());
					localViewHolder.text_visible.setText(localServiceDetail
							.getVisible());
					localNativeProcess = localServiceDetail.getNativeProcess();
					if (bool2) {
						if (localNativeProcess != null)
							localViewHolder.text_pid.setText(this.taskManager
									.getText(R.id.tvTextItem3));
						localViewHolder.text_cpu.setText(this.taskManager
								.getText(R.id.tvTextItem4));
						localViewHolder.text_mem.setText(this.taskManager
								.getText(R.id.tvTextItem5));
					}
				}
			paramView = this.mInflater.inflate(
					R.layout.taskmanager_list_item_view_simple, null);
			localViewHolder = (ListAdapters.ViewHolder) paramView.getTag();
			paramView.setBackgroundColor(Color.BLUE);
			localViewHolder.text_pid.setText(this.taskManager
					.getText(R.id.tvTextItem3) + localNativeProcess.pid);
			localViewHolder.text_cpu.setText(this.taskManager
					.getText(R.id.tvTextItem4) + localNativeProcess.cpu);
			localViewHolder.text_mem.setText(this.taskManager
					.getText(R.id.tvTextItem5) + localNativeProcess.mem);
			return paramView;
		}

		public void resetView(boolean paramBoolean) {
			if (this.cachedViews.size() > 0)
				if (!paramBoolean)
					for (int i = 0;; i++) {
						if (i >= this.cachedViews.size()) {
							this.selectStatusMap.clear();
							this.cachedViews.clear();
						}
						View localView = (View) this.cachedViews.get(i);
						localView.setBackgroundColor(Color.GREEN);
						localView.invalidate();
					}
		}

		public void setList(ArrayList<ServiceDetail> paramArrayList) {
			this.list = paramArrayList;
		}
	}

	private static class ViewHolder {
		ImageView icon;
		TextView text_cpu;
		TextView text_mem;
		TextView text_name;
		TextView text_pid;
		TextView text_visible;
	}
}
