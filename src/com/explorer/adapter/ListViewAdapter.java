package com.explorer.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.explorer.ExplorerActivity;
import com.explorer.FileFolder;
import com.explorer.R;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends ArrayAdapter<FileFolder> {

	Context context;
	ArrayList<FileFolder> arrayList;
	int resource;
	private final int KB = 1024;
	private final int MG = KB * KB;
	private final int GB = MG * KB;
	private String display_size;
	private int mColor = Color.WHITE;

	public void setTextColor(int color) {
		mColor = color;
	}

	public ListViewAdapter(Context context, int textViewResourceId,
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
			v = inflater.inflate(R.layout.item_listview, null);
		}
		FileFolder fileFolder = arrayList.get(position);
		ImageView imageView = (ImageView) v.findViewById(R.id.imvItemFolderLV);
		imageView.setBackgroundResource(fileFolder.getImv());
		imageView.setImageBitmap(fileFolder.getImvBitmap());
		TextView name = (TextView) v.findViewById(R.id.tvItemNameLV);
		name.setText(fileFolder.getName());
		TextView sizeFile = (TextView) v.findViewById(R.id.tvSize);
		TextView date = (TextView) v.findViewById(R.id.tvDateCreate);
		Date d = new Date(fileFolder.getModified());
		long year = d.getYear() + 1900;
		date.setText(String.valueOf(d.getDate() + "/" + d.getMonth() + "/"
				+ year));
		name.setTextColor(mColor);

		/************** Tinh so byte, kb, mb,gb ****************/
		double size = fileFolder.getLength();
		// if (ExplorerActivity.isCheckSize == false) {
		if (size > GB) {
			display_size = String.format("%.2f Gb ", (double) size / GB);
			sizeFile.setText(String.valueOf(display_size));
		} else if (size < GB && size > MG) {
			display_size = String.format("%.2f Mb ", (double) size / MG);
			sizeFile.setText(String.valueOf(display_size));
		} else if (size < MG && size > KB) {
			display_size = String.format("%.2f Kb ", (double) size / KB);
			sizeFile.setText(String.valueOf(display_size));
		} else if (size < KB && size > 0.0) {
			display_size = String.format("%.2f bytes ", (double) size);
			sizeFile.setText(String.valueOf(display_size));
		} else {

			display_size = String
					.format(fileFolder.getNumberItems() + " items");
			sizeFile.setText(String.valueOf(display_size));
		}
		return v;
	}
}