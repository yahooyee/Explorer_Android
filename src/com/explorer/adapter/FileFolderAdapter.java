package com.explorer.adapter;

import java.util.ArrayList;

import com.explorer.ExplorerActivity;
import com.explorer.FileFolder;
import com.explorer.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileFolderAdapter extends ArrayAdapter<FileFolder> {
	private int mColor = Color.WHITE;
	Context context;
	ArrayList<FileFolder> arrayList;
	int resource;

	public void setTextColor(int color) {
		mColor = color;
	}

	public FileFolderAdapter(Context context, int textViewResourceId,
			ArrayList<FileFolder> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		arrayList = objects;
		resource = textViewResourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_list, null);
		}
		FileFolder fileFolder = arrayList.get(position);
		ImageView imageView = (ImageView) v.findViewById(R.id.imvItemFolder);
		imageView.setBackgroundResource(fileFolder.getImv());
		imageView.setImageBitmap(fileFolder.getImvBitmap());
		TextView name = (TextView) v.findViewById(R.id.tvItemName);
		name.setText(fileFolder.getName());
		name.setTextColor(mColor);
		Animation animation = AnimationUtils.loadAnimation(getContext(),
				R.anim.items_list);
		if (ExplorerActivity.isAnimation == true) {
			v.startAnimation(animation);
		}
		return v;
	}
}
