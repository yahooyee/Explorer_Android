package com.explorer;

import android.graphics.Bitmap;

public class FileFolder {
	private Bitmap imvBitmap;
	private String name;
	private double length;
	private long modified;
	private int imv;
	private int numberItems;

	public int getNumberItems() {
		return numberItems;
	}

	public void setNumberItems(int numberItems) {
		this.numberItems = numberItems;
	}

	public int getImv() {
		return imv;
	}

	public Bitmap getImvBitmap() {
		return imvBitmap;
	}

	public void setImvBitmap(Bitmap imvBitmap) {
		this.imvBitmap = imvBitmap;
	}

	public void setImv(int imv) {
		this.imv = imv;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public long getModified() {
		return modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}

	public FileFolder(Bitmap imv, String name, double length, long modified) {
		super();
		this.imvBitmap = imv;
		this.name = name;
		this.length = length;
		this.modified = modified;
	}

	public FileFolder(int imv, String name, double length, long modified) {
		super();
		this.imv = imv;
		this.name = name;
		this.length = length;
		this.modified = modified;
	}
}
