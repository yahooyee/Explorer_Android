package com.explorer.adapter;

import java.util.ArrayList;

import com.explorer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ControlAdapter extends ArrayAdapter<String> {
	Context context;
	int resouce;
	ArrayList<String> arrayList;

	public ControlAdapter(Context context, int textViewResourceId,
			ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		resouce = textViewResourceId;
		arrayList = objects;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_control, null);
		}
		String s = arrayList.get(position);
		TextView text = (TextView) v.findViewById(R.id.tvItemControl);
		text.setText(s);
		return v;
	}
}
