package com.explorer.taskmanager;

import java.util.ArrayList;
import java.util.HashMap;

import com.explorer.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
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
 * @since 1:44:07 AM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class KillOnlyList extends Activity {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	private KillOnlyListAdapter adapter;
	TextView header;
	private HashMap<String, Drawable> icons;
	private Handler mHandler = new Handler();
	private HashMap<String, String> map;

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
	public final class KillOnlyListAdapter extends BaseAdapter {
		private ArrayList<String> list;
		private LayoutInflater mInflater;
		private PackageManager pm;

		public KillOnlyListAdapter(ArrayList<String> arg2) {
			Context localContext = KillOnlyList.this;
			this.mInflater = LayoutInflater.from(localContext);
			this.list = arg2;
			this.pm = localContext.getPackageManager();
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

		public ArrayList<String> getList() {
			return this.list;
		}

		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			KillOnlyList.ViewHolder localViewHolder = new ViewHolder();
			if (paramView == null) {
				paramView = this.mInflater.inflate(
						R.layout.taskmanager_list_item_view_simple, null);
				localViewHolder = new KillOnlyList.ViewHolder();
				localViewHolder.icon = ((ImageView) paramView
						.findViewById(R.id.imvIgnoreList));
				localViewHolder.label = ((TextView) paramView
						.findViewById(R.id.tvTest));
				paramView.setTag(localViewHolder);
			}
			String str = (String) this.list.get(paramInt);
			try {
				Drawable localDrawable = (Drawable) KillOnlyList.this.icons
						.get(str);
				if (localDrawable == null)
					localDrawable = this.pm.getApplicationIcon(str);
				localViewHolder.icon.setImageDrawable(localDrawable);
				localViewHolder.label
						.setText(this.pm.getApplicationLabel(this.pm
								.getApplicationInfo(str, 0)));
				localViewHolder = (KillOnlyList.ViewHolder) paramView.getTag();
			} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			}
			return paramView;
		}
	}

	private static class ViewHolder {
		ImageView icon;
		TextView label;
	}
}
