package com.explorer;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class InforDirectory extends Activity {
	private static final int KB = 1024;
	private static final int MG = KB * KB;
	private static final int GB = MG * KB;
	private String mPathName;
	private TextView mNameLabel, mPathLabel, mDirLabel, mFileLabel, mTimeLabel,
			mTotalLabel, Storage;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infor);

		Intent i = getIntent();
		if (i != null) {
			if (i.getAction() != null
					&& i.getAction().equals(Intent.ACTION_VIEW)) {
				mPathName = i.getData().getPath();

				if (mPathName == null)
					mPathName = "";
			} else {
				mPathName = i.getExtras().getString("PATH_NAME");
			}
		}

		mNameLabel = (TextView) findViewById(R.id.name_label);
		mPathLabel = (TextView) findViewById(R.id.path_label);
		mDirLabel = (TextView) findViewById(R.id.dirs_label);
		mFileLabel = (TextView) findViewById(R.id.files_label);
		mTimeLabel = (TextView) findViewById(R.id.time_stamp);
		mTotalLabel = (TextView) findViewById(R.id.total_size);
		Storage = (TextView) findViewById(R.id.tvStorageLabel);
		 updateStorageLabel();
		/*
		 * make zip button visible and setup onclick logic to have zip button
		 */
		Button zip = (Button) findViewById(R.id.zip_button);
		zip.setVisibility(Button.GONE);

		ImageView back = (ImageView) findViewById(R.id.imvBack);
		back.setOnClickListener(new ButtonHandler());

		new BackgroundWork().execute(mPathName);

	}

	private void updateStorageLabel() {
		long total, aval;
		int kb = 1024;

		StatFs fs = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());

		total = fs.getBlockCount() * (fs.getBlockSize() / kb);
		aval = fs.getAvailableBlocks() * (fs.getBlockSize() / kb);

		Storage.setText(String.format("Total %.2f GB "
				+ "\t\tAvailable %.2f GB", (double) total / (kb * kb),
				(double) aval / (kb * kb)));
	}

	private class BackgroundWork extends AsyncTask<String, Void, Long> {
		private ProgressDialog dialog;
		private String mDisplaySize;
		private int mFileCount = 0;
		private int mDirCount = 0;

		protected void onPreExecute() {
			dialog = ProgressDialog.show(InforDirectory.this, "",
					"Calculating information...", true, true);
		}

		protected Long doInBackground(String... vals) {
			// FileManager flmg = new FileManager();
			// ExplorerActivity flm = new ExplorerActivity();
			File dir = new File(vals[0]);
			long size = 0;
			int len = 0;

			File[] list = dir.listFiles();
			if (list != null)
				len = list.length;

			for (int i = 0; i < len; i++) {
				if (list[i].isFile())
					mFileCount++;
				else if (list[i].isDirectory())
					mDirCount++;
			}

			if (vals[0].equals("/")) {
				StatFs fss = new StatFs(Environment.getRootDirectory()
						.getPath());
				size = fss.getAvailableBlocks() * (fss.getBlockSize() / KB);

				mDisplaySize = (size > GB) ? String.format("%.2f Gb ",
						(double) size / MG) : String.format("%.2f Mb ",
						(double) size / KB);

			} else if (vals[0].equals("/sdcard")) {
				StatFs fs = new StatFs(Environment
						.getExternalStorageDirectory().getPath());
				size = fs.getBlockCount() * (fs.getBlockSize() / KB);

				mDisplaySize = (size > GB) ? String.format("%.2f Gb ",
						(double) size / GB) : String.format("%.2f Gb ",
						(double) size / MG);
			} else {
				size = getDirSize(vals[0]);
				if (size > GB)
					mDisplaySize = String
							.format("%.2f Gb ", (double) size / GB);
				else if (size < GB && size > MG)
					mDisplaySize = String
							.format("%.2f Mb ", (double) size / MG);
				else if (size < MG && size > KB)
					mDisplaySize = String
							.format("%.2f Kb ", (double) size / KB);
				else
					mDisplaySize = String.format("%.2f bytes ", (double) size);
			}
			return size;
		}

		protected void onPostExecute(Long result) {
			File dir = new File(mPathName);
			mNameLabel.setText(dir.getName());
			mPathLabel.setText(dir.getAbsolutePath());
			mDirLabel.setText(mDirCount + " folders ");
			mFileLabel.setText(mFileCount + " files ");
			mTotalLabel.setText(mDisplaySize);
			mTimeLabel.setText(new Date(dir.lastModified()) + " ");
			dialog.cancel();
		}
	}

	private class ButtonHandler implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.imvBack)
				finish();
		}
	}

	private long mDirSize = 0;

	public long getDirSize(String path) {
		get_dir_size(new File(path));
		return mDirSize;
	}

	private void get_dir_size(File path) {
		File[] list = path.listFiles();
		int len;
		if (list != null) {
			len = list.length;
			for (int i = 0; i < len; i++) {
				try {
					if (list[i].isFile() && list[i].canRead()) {
						mDirSize += list[i].length();
					} else if (list[i].isDirectory() && list[i].canRead()
							&& !isSymlink(list[i])) {
						get_dir_size(list[i]);
					}
				} catch (IOException e) {
					Log.e("IOException", e.getMessage());
				}
			}
		}
	}

	private static boolean isSymlink(File file) throws IOException {
		File fileInCanonicalDir = null;
		if (file.getParent() == null) {
			fileInCanonicalDir = file;
		} else {
			File canonicalDir = file.getParentFile().getCanonicalFile();
			fileInCanonicalDir = new File(canonicalDir, file.getName());
		}
		return !fileInCanonicalDir.getCanonicalFile().equals(
				fileInCanonicalDir.getAbsoluteFile());
	}
}
