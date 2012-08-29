package com.explorer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.prefs.Preferences;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import com.explorer.adapter.ControlAdapter;
import com.explorer.adapter.FileFolderAdapter;
import com.explorer.adapter.ListViewAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * (c) D09CN2 - PTIT - Ha Noi
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 21:21:02 05-06-2012 Tel: 0974 878 244
 * 
 */
public class ExplorerActivity extends Activity implements OnItemClickListener,
		OnItemLongClickListener, OnClickListener, OnTouchListener {

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private final static int SEARCH = 1;
	private final static int DELETE = 2;
	private final static int COPY = 3;
	private final static int ZIP = 4;
	private final static int UNZIP = 5;
	private static final int UNZIPTO = 6;
	private static final int SETTING_REQ = 7;

	private static final int BUFFER = 2048;

	private static final int THEME_HIGHTLAND = 0;
	private static final int THEME_ROMANCE = 2;
	private static final int THEME_ANGEL = 3;
	private static final int THEME_GIRL = 1;

	private static final String PREFS_NAME = "ManagerPrefsFile";
	private static final String PREFS_HIDDEN = "hidden";
	private static final String PREFS_COLOR = "color";
	private static final String PREFS_THUMBNAIL = "thumbnail";
	private static final String PREFS_SORT = "sort";
	private static final String PREFS_THEME = "theme";
	private static final String PREFS_VIEW = "View";
	private static final String PREFS_ANIM = "Anim";
	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------

	public FileFolder fileFolder;

	public FileFolderAdapter adapter;
	public ListViewAdapter adapterListView;
	public ControlAdapter controlAdapter;

	private GridView list;
	private ListView listView;
	private TextView myPath;
	private ImageView up, view, home, search, newfolder, sort, paste, about,
			manager, info, setting, help;
	private RelativeLayout theme, rlcontrol, rlLocal;
	private HorizontalScrollView HSV;

	ArrayList<FileFolder> listFileFolder;
	private ArrayList<String> control;

	private SharedPreferences mSettings;

	private boolean mReturnIntent = false;
	boolean flag = false, pasteView = true;
	private File file, f;
	boolean displayView = true, tmp = false;
	int sx = 1;
	private Context mContext;
	private boolean mShowHiddenFiles = false;
	int color;
	// has a touch press started?
	private boolean touchStarted = false;
	// co-ordinates of image
	private int x, y;
	private int choose_theme = THEME_HIGHTLAND;

	public static boolean isAnimation = true;

	private ThumbnailCreator mThumbnail;
	private boolean thumbnail_flag = true;
	public static int number_items = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Getter & Setter
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ----------------------------------------------------------------------------------------------
	// Methods for/from SuperClass/Interfaces
	// ----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		theme = (RelativeLayout) findViewById(R.id.theme);
		rlcontrol = (RelativeLayout) findViewById(R.id.rlControl);
		rlLocal = (RelativeLayout) findViewById(R.id.rlLocal);
		list = (GridView) findViewById(R.id.listGrid);
		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);
		myPath = (TextView) findViewById(R.id.tvURI);
		up = (ImageView) findViewById(R.id.imvUp);
		up.setOnClickListener(this);
		view = (ImageView) findViewById(R.id.imvView);
		view.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		listView.setVisibility(View.GONE);
		home = (ImageView) findViewById(R.id.imvHome);
		home.setOnClickListener(this);
		search = (ImageView) findViewById(R.id.imvSearch);
		search.setOnClickListener(this);
		newfolder = (ImageView) findViewById(R.id.imvNew);
		newfolder.setOnClickListener(this);
		sort = (ImageView) findViewById(R.id.imvSort);
		sort.setOnClickListener(this);
		paste = (ImageView) findViewById(R.id.imvPaste);
		paste.setOnClickListener(this);
		manager = (ImageView) findViewById(R.id.imvManager);
		manager.setOnClickListener(this);
		info = (ImageView) findViewById(R.id.imvInfo);
		info.setOnClickListener(this);
		about = (ImageView) findViewById(R.id.imvAbout);
		about.setOnClickListener(this);
		setting = (ImageView) findViewById(R.id.imvSetting);
		setting.setOnClickListener(this);
		help = (ImageView) findViewById(R.id.imvHelp);
		help.setOnClickListener(this);
		HSV = (HorizontalScrollView) findViewById(R.id.HSV);
		mContext = this;
		mSettings = getSharedPreferences(PREFS_NAME, 0);
		boolean hide = mSettings.getBoolean(PREFS_HIDDEN, false);
		boolean viewDis = mSettings.getBoolean(PREFS_VIEW, false);
		boolean isAnim = mSettings.getBoolean(PREFS_ANIM, true);
		System.out.println("boolean=" + isAnim);
		this.isAnimation = isAnim;
		displayView = viewDis;
		viewChid();
		color = mSettings.getInt(PREFS_COLOR, -1);
		int sort = mSettings.getInt(PREFS_SORT, 3);
		int getTheme = mSettings.getInt(PREFS_THEME, 3);
		choose_theme = getTheme;
		setTheme();
		setShowHiddenFiles(hide);
		System.out.println("Sort=" + sort);
		if (sort == 0) {
			sx = 1;
		} else {
			sx = sort;
		}
		getDirection("/sdcard");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SharedPreferences.Editor editor = mSettings.edit();
		boolean check;
		boolean thumbnail;
		int sort, space, setTheme;
		if (requestCode == SETTING_REQ && resultCode == RESULT_CANCELED) {
			check = data.getBooleanExtra("HIDDEN", false);
			thumbnail = data.getBooleanExtra("THUMBNAIL", true);
			color = data.getIntExtra("COLOR", -1);
			setTheme = data.getIntExtra("SORT", 0);
			sort = sx;
			space = data.getIntExtra("SPACE", View.VISIBLE);
			editor.putBoolean(PREFS_HIDDEN, check);
			editor.putBoolean(PREFS_THUMBNAIL, thumbnail);
			editor.putInt(PREFS_COLOR, color);
			editor.putInt(PREFS_SORT, sort);
			editor.putInt(PREFS_THEME, setTheme);
			// editor.putInt(PREFS_STORAGE, space);
			editor.commit();
			setShowHiddenFiles(check);
			choose_theme = setTheme;
			setTheme();
		}
		getDirection(myPath.getText().toString());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			touchStarted = true;
		} else if (action == MotionEvent.ACTION_MOVE) {
			// movement: cancel the touch press
			touchStarted = false;
			x = (int) event.getX();
			y = (int) event.getY();
			// invalidate(); // request draw
		} else if (action == MotionEvent.ACTION_UP) {
			if (touchStarted) {
				// touch press complete, show toast
				Toast.makeText(v.getContext(), "Coords: " + x + ", " + y, 1000)
						.show();
			}
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			try {
				File f = new File(myPath.getText().toString());
				getDirection(f.getParent());
			} catch (Exception e) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ExplorerActivity.this);
				builder.setTitle("Exit");
				builder.setMessage("Do you want exit?");
				builder.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								System.exit(0);
								isAnimation = true;
								SharedPreferences.Editor editor = mSettings
										.edit();
								editor.putBoolean(PREFS_ANIM, isAnimation);
								editor.commit();
							}
						});
				builder.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				builder.show();
			}
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		fileFolder = listFileFolder.get(position);
		String link = myPath.getText().toString() + "/" + fileFolder.getName();
		final File file = new File(link);
		if (file.isDirectory()) {
			if (file.canRead()) {
				try {
					getDirection(link);
				} catch (Exception e) {
				}
			} else {
				new AlertDialog.Builder(this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle(
								"[" + file.getName()
										+ "] folder can't be read!")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		} else if (file.getName().endsWith(".jpeg")
				|| file.getName().endsWith(".jpg")
				|| file.getName().endsWith(".png")
				|| file.getName().endsWith(".gif")
				|| file.getName().endsWith(".tiff")
				|| file.getName().endsWith(".JPEG")
				|| file.getName().endsWith(".JPG")
				|| file.getName().endsWith(".PNG")
				|| file.getName().endsWith(".GIF")
				|| file.getName().endsWith(".TIFF")) {
			if (file.exists()) {
				if (mReturnIntent) {
					returnIntentResults(file);
				} else {
					Intent picIntent = new Intent();
					picIntent.setAction(android.content.Intent.ACTION_VIEW);
					picIntent.setDataAndType(Uri.fromFile(file), "image/*");
					startActivity(picIntent);
				}
			}
		} else if (file.getName().endsWith("3gp")
				|| file.getName().endsWith("m4v")
				|| file.getName().endsWith("ogg")
				|| file.getName().endsWith("wmv")
				|| file.getName().endsWith("mp4")
				|| file.getName().endsWith("3GP")
				|| file.getName().endsWith("M4V")
				|| file.getName().endsWith("WMV")
				|| file.getName().endsWith("MP4")
				|| file.getName().endsWith("OGG")) {
			if (file.exists()) {
				if (mReturnIntent) {
					returnIntentResults(file);
				} else {
					Intent movieIntent = new Intent();
					movieIntent.setAction(android.content.Intent.ACTION_VIEW);
					movieIntent.setDataAndType(Uri.fromFile(file), "video/*");
					startActivity(movieIntent);
				}
			}
		} else if (file.getName().endsWith("pdf")
				|| file.getName().endsWith("PDF")) {
			if (file.exists()) {
				if (mReturnIntent) {
					returnIntentResults(file);
				} else {
					Intent pdfIntent = new Intent();
					pdfIntent.setAction(android.content.Intent.ACTION_VIEW);
					pdfIntent.setDataAndType(Uri.fromFile(file),
							"application/pdf");
					try {
						startActivity(pdfIntent);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(this,
								"Sorry, couldn't find a pdf viewer",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		} else if (file.getName().endsWith("apk")
				|| file.getName().endsWith("APK")) {
			if (file.exists()) {
				if (mReturnIntent) {
					returnIntentResults(file);
				} else {
					Intent apkIntent = new Intent();
					apkIntent.setAction(android.content.Intent.ACTION_VIEW);
					apkIntent.setDataAndType(Uri.fromFile(file),
							"application/vnd.android.package-archive");
					startActivity(apkIntent);
				}
			}
		} else if (file.getName().endsWith("html")
				|| file.getName().endsWith("HTML")) {
			if (file.exists()) {
				if (mReturnIntent) {
					returnIntentResults(file);
				} else {
					Intent htmlIntent = new Intent();
					htmlIntent.setAction(android.content.Intent.ACTION_VIEW);
					htmlIntent.setDataAndType(Uri.fromFile(file), "text/html");
					try {
						startActivity(htmlIntent);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(this,
								"Sorry, couldn't find a HTML viewer",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		} else if (file.getName().endsWith("mp3")
				|| file.getName().endsWith("wma")
				|| file.getName().endsWith("acc")
				|| file.getName().endsWith("mid")
				|| file.getName().endsWith("flac")
				|| file.getName().endsWith("wav")
				|| file.getName().endsWith("ape")
				|| file.getName().endsWith("MP3")
				|| file.getName().endsWith("WMA")
				|| file.getName().endsWith("ACC")
				|| file.getName().endsWith("MID")
				|| file.getName().endsWith("FLAC")
				|| file.getName().endsWith("WAV")
				|| file.getName().endsWith("APE")) {
			if (file.exists()) {
				if (mReturnIntent) {
					returnIntentResults(file);
				} else {
					Intent audio = new Intent();
					audio.setAction(android.content.Intent.ACTION_VIEW);
					// audio.setAction(android.content.Intent.ACTION_PICK);
					audio.setDataAndType(Uri.fromFile(file), "audio/*");
					// audio.setComponent(new ComponentName("musicplayer.main",
					// "musicplayer.main.MainPlayer"));
					try {
						startActivity(audio);
					} catch (Exception e) {
						Toast.makeText(this, "Sorry, couldn't  open file",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		} else if (file.getName().endsWith("txt")
				|| file.getName().endsWith("TXT")) {
			if (file.exists()) {
				if (mReturnIntent) {
					returnIntentResults(file);
				} else {
					Intent txtIntent = new Intent();
					txtIntent.setAction(android.content.Intent.ACTION_VIEW);
					txtIntent.setDataAndType(Uri.fromFile(file), "text/plain");
					try {
						startActivity(txtIntent);
					} catch (ActivityNotFoundException e) {
						txtIntent.setType("text/*");
						startActivity(txtIntent);
					}
				}
			}
		} else if (file.getName().endsWith("zip")
				|| file.getName().endsWith("ZIP")) {
			if (file.exists()) {
				if (mReturnIntent) {
					returnIntentResults(file);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					AlertDialog alert;
					CharSequence[] option = { "Extract here", "Extract to..." };
					builder.setTitle("Extract");
					builder.setItems(option,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										unZipFile(file.getName(), myPath
												.getText().toString());
										break;

									case 1:
										dialogextractTo(file.getName(), myPath
												.getText().toString());
										break;
									}
								}
							});
					alert = builder.create();
					alert.show();
				}
			}
		} else {
			if (file.exists()) {
				if (mReturnIntent) {
					returnIntentResults(file);
				} else {
					Intent generic = new Intent();
					generic.setAction(android.content.Intent.ACTION_VIEW);
					generic.setDataAndType(Uri.fromFile(file), "text/plain");
					try {
						startActivity(generic);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(
								this,
								"Sorry, couldn't find anything " + "to open "
										+ file.getName(), Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		fileFolder = listFileFolder.get(arg2);
		String link = myPath.getText().toString() + "/" + fileFolder.getName();
		control = new ArrayList<String>();
		file = new File(link);
		dialogControl();
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imvHelp:
			Intent in = new Intent(this, Help.class);
			startActivity(in);
			break;
		case R.id.imvSetting:
			file = new File(myPath.getText().toString());
			control = new ArrayList<String>();
			control.add("New folder");
			control.add("Paste");
			dialogControl();
			controlAdapter.notifyDataSetChanged();

			break;
		case R.id.imvAbout:
			about();
			break;
		case R.id.imvInfo:
			Intent intent = new Intent(this, InforDirectory.class);
			intent.putExtra("PATH_NAME", myPath.getText().toString());
			System.out.println("Info=" + myPath.getText().toString());
			startActivity(intent);
			break;
		case R.id.imvManager:
			Intent settings_int = new Intent(this, Setting.class);
			settings_int.putExtra("HIDDEN",
					mSettings.getBoolean(PREFS_HIDDEN, false));
			settings_int.putExtra("THUMBNAIL",
					mSettings.getBoolean(PREFS_THUMBNAIL, true));
			settings_int.putExtra("COLOR", mSettings.getInt(PREFS_COLOR, -1));
			settings_int.putExtra("SORT", mSettings.getInt(PREFS_THEME, 0));
			// settings_int.putExtra("SPACE",
			// mSettings.getInt(PREFS_STORAGE, View.VISIBLE));
			startActivityForResult(settings_int, SETTING_REQ);
			break;
		case R.id.imvPaste:
			// String s = myPath.getText().toString();
			// if (flag == true) {
			// if (f.renameTo(new File(s + "/" + f.getName()))) {
			// Toast.makeText(getBaseContext(),
			// "File is moved successful!", Toast.LENGTH_SHORT)
			// .show();
			// } else {
			// Toast.makeText(getBaseContext(), "File is failed to move!",
			// Toast.LENGTH_SHORT).show();
			// }
			// flag = false;
			// pasteView = false;
			// paste.setImageResource(R.drawable.pastepr);
			// getDirection(myPath.getText().toString());
			// } else if (tmp == true) {
			// // copy(f, s + "/" + f.getName());
			// copyFile(f.getPath(), s);
			// tmp = false;
			// pasteView = false;
			// paste.setImageResource(R.drawable.pastepr);
			// getDirection(myPath.getText().toString());
			// }
			file = new File(myPath.getText().toString());
			control = new ArrayList<String>();
			control.add("New folder");
			control.add("Paste");
			dialogControl();
			controlAdapter.notifyDataSetChanged();
			break;
		case R.id.imvSort:
			// dialogSort();
			Intent startBackup = new Intent(this, ApplicationBackup.class);
			startActivity(startBackup);
			break;
		case R.id.imvNew:
			file = new File(myPath.getText().toString());
			creatNewfolder();
			break;
		case R.id.imvSearch:
			dialogSearch();
			break;
		case R.id.imvHome:
			getDirection("/sdcard");
			break;
		case R.id.imvUp:
			try {
				File f = new File(myPath.getText().toString());
				getDirection(f.getParent());
				isAnimation = false;
			} catch (Exception e) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ExplorerActivity.this);
				builder.setTitle("Exit");
				builder.setMessage("Do you want exit?");
				builder.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SharedPreferences.Editor editor = mSettings
										.edit();
								isAnimation = true;
								editor.putBoolean(PREFS_ANIM, isAnimation);
								editor.commit();
								System.exit(0);
							}
						});
				builder.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				builder.show();
			}
			break;
		case R.id.imvView:
			viewChid();
		default:
			break;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void setShowHiddenFiles(boolean choice) {
		mShowHiddenFiles = choice;
	}

	public void getDirection(String s) {
		// SharedPreferences.Editor editor = mSettings.edit();
		// isAnimation = true;
		// editor.putBoolean(PREFS_ANIM, isAnimation);
		// editor.commit();
		File f = new File(s);
		myPath.setText(s);
		listFileFolder = new ArrayList<FileFolder>();
		File[] files = f.listFiles();
		// number_items = files.length;
		ArrayList<String> listFileName = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			listFileName.add(files[i].getName());
			File file = files[i];
			if (file.isDirectory()) {

				if (!mShowHiddenFiles) {
					if (file.getName().charAt(0) != '.') {
						fileFolder = new FileFolder(R.drawable.folder_snow,
								file.getName(), file.length(),
								file.lastModified());
						if (file.list() != null) {
							fileFolder.setNumberItems(file.list().length);
						}
						listFileFolder.add(fileFolder);
					}
				} else {
					fileFolder = new FileFolder(R.drawable.folder_snow,
							file.getName(), file.length(), file.lastModified());
					if (file.list() != null) {
						fileFolder.setNumberItems(file.list().length);
					}
					listFileFolder.add(fileFolder);
				}
			} else {
				if (file.getName().endsWith(".jpeg")
						|| file.getName().endsWith(".jpg")
						|| file.getName().endsWith(".png")
						|| file.getName().endsWith(".gif")
						|| file.getName().endsWith(".tiff")
						|| file.getName().endsWith(".JPEG")
						|| file.getName().endsWith(".JPG")
						|| file.getName().endsWith(".PNG")
						|| file.getName().endsWith(".GIF")
						|| file.getName().endsWith(".TIFF")) {
					if (mThumbnail == null) {
						mThumbnail = new ThumbnailCreator(52, 52);
					}
					if (thumbnail_flag && file.length() != 0) {
						Bitmap thumb = mThumbnail
								.isBitmapCached(file.getPath());
						if (thumb == null) {
							final Handler handle = new Handler(
									new Handler.Callback() {
										public boolean handleMessage(Message msg) {
											adapter.notifyDataSetChanged();
											return true;
										}
									});
							mThumbnail.createNewThumbnail(listFileName, s,
									handle);
							if (!mThumbnail.isAlive())
								mThumbnail.start();
						} else {

							fileFolder = new FileFolder(thumb, file.getName(),
									file.length(), file.lastModified());
							listFileFolder.add(fileFolder);
						}
					} else {
						fileFolder = new FileFolder(R.drawable.image_browser,
								file.getName(), file.length(),
								file.lastModified());
						listFileFolder.add(fileFolder);
					}

				} else if (file.getName().endsWith("3gp")
						|| file.getName().endsWith("m4v")
						|| file.getName().endsWith("ogg")
						|| file.getName().endsWith("wmv")
						|| file.getName().endsWith("mp4")
						|| file.getName().endsWith("3GP")
						|| file.getName().endsWith("M4V")
						|| file.getName().endsWith("OGG")
						|| file.getName().endsWith("WMV")
						|| file.getName().endsWith("MP4")) {
					fileFolder = new FileFolder(R.drawable.format_media,
							file.getName(), file.length(), file.lastModified());
					listFileFolder.add(fileFolder);
				} else if (file.getName().endsWith("mp3")
						|| file.getName().endsWith("wma")
						|| file.getName().endsWith("acc")
						|| file.getName().endsWith("mid")
						|| file.getName().endsWith("flag")
						|| file.getName().endsWith("wav")
						|| file.getName().endsWith("ape")
						|| file.getName().endsWith("MP3")
						|| file.getName().endsWith("WMA")
						|| file.getName().endsWith("ACC")
						|| file.getName().endsWith("MID")
						|| file.getName().endsWith("FLAG")
						|| file.getName().endsWith("WAV")
						|| file.getName().endsWith("APE")) {
					fileFolder = new FileFolder(R.drawable.format_music,
							file.getName(), file.length(), file.lastModified());
					listFileFolder.add(fileFolder);
				} else if (file.getName().endsWith("doc")
						|| file.getName().endsWith("docx")
						|| file.getName().endsWith("DOC")
						|| file.getName().endsWith("DOCX")) {
					fileFolder = new FileFolder(R.drawable.word,
							file.getName(), file.length(), file.lastModified());
					listFileFolder.add(fileFolder);
				} else if (file.getName().endsWith("pdf")
						|| file.getName().endsWith("PDF")) {
					fileFolder = new FileFolder(R.drawable.pdf, file.getName(),
							file.length(), file.lastModified());
					listFileFolder.add(fileFolder);
				} else if (file.getName().endsWith("xls")
						|| file.getName().endsWith("xlsx")
						|| file.getName().endsWith("XLS")
						|| file.getName().endsWith("XLSX")) {
					fileFolder = new FileFolder(R.drawable.excel,
							file.getName(), file.length(), file.lastModified());
					listFileFolder.add(fileFolder);
				} else if (file.getName().endsWith("chm")
						|| file.getName().endsWith("CHM")) {
					fileFolder = new FileFolder(R.drawable.chm, file.getName(),
							file.length(), file.lastModified());
					listFileFolder.add(fileFolder);
				} else if (file.getName().endsWith("apk")
						|| file.getName().endsWith("APK")) {
					String filePath = file.getPath();
					PackageInfo packageInfo = this.getPackageManager()
							.getPackageArchiveInfo(filePath,
									PackageManager.GET_ACTIVITIES);
					if (packageInfo != null) {
						ApplicationInfo appInfo = packageInfo.applicationInfo;
						if (Build.VERSION.SDK_INT >= 8) {
							appInfo.sourceDir = filePath;
							appInfo.publicSourceDir = filePath;
						}
						Drawable icon = appInfo.loadIcon(this
								.getPackageManager());
						Bitmap bmpIcon = ((BitmapDrawable) icon).getBitmap();
					}
					fileFolder = new FileFolder(R.drawable.format_app,
							file.getName(), file.length(), file.lastModified());
					listFileFolder.add(fileFolder);
				} else if (file.getName().endsWith("ppt")
						|| file.getName().endsWith("pptx")
						|| file.getName().endsWith("PPT")
						|| file.getName().endsWith("PPTX")) {
					fileFolder = new FileFolder(R.drawable.ppt, file.getName(),
							file.length(), file.lastModified());
					listFileFolder.add(fileFolder);
				} else if (file.getName().endsWith("zip")
						|| file.getName().endsWith("rar")
						|| file.getName().endsWith("ZIP")
						|| file.getName().endsWith("RAR")) {
					fileFolder = new FileFolder(R.drawable.zip_icon,
							file.getName(), file.length(), file.lastModified());
					listFileFolder.add(fileFolder);
				} else if (file.getName().endsWith("html")
						|| file.getName().endsWith("HTML")) {
					fileFolder = new FileFolder(R.drawable.html,
							file.getName(), file.length(), file.lastModified());
					listFileFolder.add(fileFolder);
				} else if (file.getName().endsWith("txt")
						|| file.getName().endsWith("TXT")) {
					fileFolder = new FileFolder(R.drawable.text,
							file.getName(), file.length(), file.lastModified());
					listFileFolder.add(fileFolder);
				} else {
					fileFolder = new FileFolder(R.drawable.file,
							file.getName(), file.length(), file.lastModified());
					listFileFolder.add(fileFolder);
				}
			}
		}
		adapter = new FileFolderAdapter(getBaseContext(), R.layout.item_list,
				listFileFolder);
		adapter.setTextColor(color);
		adapterListView = new ListViewAdapter(getBaseContext(),
				R.layout.item_listview, listFileFolder);
		adapterListView.setTextColor(color);
		switch (sx) {
		case 1:
			adapter.sort(new Comparator<FileFolder>() {
				@Override
				public int compare(FileFolder lhs, FileFolder rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}
			});
			adapterListView.sort(new Comparator<FileFolder>() {
				@Override
				public int compare(FileFolder lhs, FileFolder rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}
			});
			adapter.notifyDataSetChanged();
			adapterListView.notifyDataSetChanged();
			list.setAdapter(adapter);
			listView.setAdapter(adapterListView);
			break;
		case 2:
			adapter.sort(new Comparator<FileFolder>() {
				@Override
				public int compare(FileFolder lhs, FileFolder rhs) {
					return new Double(lhs.getLength()).compareTo(rhs
							.getLength());
				}
			});
			adapterListView.sort(new Comparator<FileFolder>() {
				@Override
				public int compare(FileFolder lhs, FileFolder rhs) {
					return new Double(lhs.getLength()).compareTo(rhs
							.getLength());
				}
			});
			adapter.notifyDataSetChanged();
			adapterListView.notifyDataSetChanged();
			list.setAdapter(adapter);
			listView.setAdapter(adapterListView);
			break;
		case 4:
			Collections.sort(listFileFolder, new sxType());
			adapter.notifyDataSetChanged();
			adapterListView.notifyDataSetChanged();
			list.setAdapter(adapter);
			listView.setAdapter(adapterListView);
			break;
		case 3:
			adapter.sort(new Comparator<FileFolder>() {
				@Override
				public int compare(FileFolder lhs, FileFolder rhs) {
					return new Long(lhs.getModified()).compareTo(rhs
							.getModified());
				}
			});
			adapterListView.sort(new Comparator<FileFolder>() {

				@Override
				public int compare(FileFolder lhs, FileFolder rhs) {
					return new Long(lhs.getModified()).compareTo(rhs
							.getModified());
				}
			});
			adapter.notifyDataSetChanged();
			adapterListView.notifyDataSetChanged();
			list.setAdapter(adapter);
			listView.setAdapter(adapterListView);
			break;
		}
	}

	private void returnIntentResults(File data) {
		mReturnIntent = false;
		Intent ret = new Intent();
		ret.setData(Uri.fromFile(data));
		setResult(RESULT_OK, ret);
		finish();
	}

	public void dialogControl() {
		final Dialog dialog = new Dialog(ExplorerActivity.this,
				android.R.style.Theme_Dialog);
		dialog.setContentView(R.layout.dialog_long_touch);
		dialog.setTitle("Control");
		ListView listControl = (ListView) dialog
				.findViewById(R.id.listController);
		control.add("Cut");
		control.add("Rename");
		control.add("Delete");
		control.add("Copy");
		control.add("Zip");
		control.add("Unzip");
		controlAdapter = new ControlAdapter(getBaseContext(),
				R.layout.item_control, control);
		listControl.setAdapter(controlAdapter);
		listControl.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				String s = control.get(arg2);
				if (s.equals("Rename")) {
					final Dialog d = new Dialog(ExplorerActivity.this,
							android.R.style.Theme_Dialog);
					d.setContentView(R.layout.rename);
					d.setTitle("Rename");
					final EditText text = (EditText) d
							.findViewById(R.id.etRename);
					text.setText(file.getName());
					Button ok = (Button) d.findViewById(R.id.btOKRename);
					ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							renameFile(file.getPath(), file.getParent() + "/"
									+ text.getText().toString());
							getDirection(myPath.getText().toString());
							d.hide();
						}
					});
					Button cancel = (Button) d
							.findViewById(R.id.btCancelRename);
					cancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							d.hide();
						}
					});
					d.show();
					dialog.hide();
				} else if (s.equals("Delete")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ExplorerActivity.this);
					builder.setTitle(" Delete " + file.getName());
					builder.setIcon(R.drawable.alert);
					builder.setMessage("Are you sure to delete?");
					builder.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									try {
										delete_file(file.getPath());
									} catch (Exception e) {
										Toast.makeText(getApplicationContext(),
												"File can't delete",
												Toast.LENGTH_SHORT);
									}
								}
							});
					builder.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					builder.show();
					dialog.hide();
				} else if (s.equals("Copy")) {
					tmp = true;
					flag = false;
					f = file;
					pasteView = true;
					paste.setImageResource(R.drawable.paste);
					dialog.hide();
				} else if (s.equals("Paste")) {

					try {
						if (flag == true) {
							if (f.renameTo(new File(file.getPath() + "/"
									+ f.getName()))) {
								Toast.makeText(getBaseContext(),
										"File is moved successful!",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getBaseContext(),
										"File is failed to move!",
										Toast.LENGTH_SHORT).show();
							}
							paste.setImageResource(R.drawable.pastepr);
							flag = false;
							dialog.hide();
						} else if (tmp == true) {
							// copy(f, file.getPath() + "/" + f.getName());
							copyFile(f.getPath(), file.getPath());
							System.out.println(f.getPath() + file.getPath());
							tmp = false;
							paste.setImageResource(R.drawable.pastepr);
							dialog.hide();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					getDirection(myPath.getText().toString());
				} else if (s.equals("New folder")) {
					creatNewfolder();
					dialog.hide();
				} else if (s.equals("Cut")) {
					f = file;
					f.isHidden();
					flag = true;
					tmp = false;
					pasteView = true;
					paste.setImageResource(R.drawable.paste);
					dialog.hide();
				} else if (s.equals("Zip")) {
					zip_file(file.getPath());
					dialog.hide();
				} else if (s.equals("Unzip")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ExplorerActivity.this);
					AlertDialog alert;
					CharSequence[] option = { "Extract here", "Extract to..." };
					builder.setTitle("Extract");
					builder.setItems(option,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										unZipFile(file.getName(), myPath
												.getText().toString());
										break;

									case 1:
										dialogextractTo(file.getName(), myPath
												.getText().toString());
										break;
									}
								}
							});
					alert = builder.create();
					alert.show();
					dialog.hide();
				}
			}
		});
		dialog.show();
	}

	public void renameFile(String file, String toFile) {
		File toBeRenamed = new File(file);
		if (!toBeRenamed.exists()) {
			System.out.println("File does not exist: " + file);
			return;
		}
		File newFile = new File(toFile);
		if (toBeRenamed.renameTo(newFile)) {
			System.out.println("File has been renamed.");
		} else {
			System.out.println("Error renmaing file");
		}
	}

	public void viewChid() {
		if (displayView == true) {
			SharedPreferences.Editor editor = mSettings.edit();
			list.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			listView.setAdapter(adapterListView);
			view.setImageResource(R.drawable.listt);
			editor.putBoolean(PREFS_VIEW, displayView);
			editor.commit();
			displayView = false;
		} else if (displayView == false) {
			SharedPreferences.Editor editor = mSettings.edit();
			listView.setVisibility(View.GONE);
			list.setAdapter(adapter);
			list.setVisibility(View.VISIBLE);
			editor.putBoolean(PREFS_VIEW, displayView);
			editor.commit();
			view.setImageResource(R.drawable.list);
			displayView = true;
		}
	}

	void copy(File src, String s) throws IOException {
		File file = null;
		FileOutputStream os;
		try {
			file = new File(s);
			boolean success = file.createNewFile();
			if (success) {
			} else {
			}
		} catch (IOException e) {
		}
		InputStream in = new FileInputStream(src);
		os = new FileOutputStream(file);
		FileDescriptor fd = os.getFD();
		byte[] data = new byte[] { (byte) 0xCA, (byte) 0xFE, (byte) 0xBA,
				(byte) 0xBE };
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			os.write(buf, 0, len);
		}
		Toast.makeText(getBaseContext(), "Copy success", Toast.LENGTH_SHORT)
				.show();
		os.write(data);
		os.flush();
		fd.sync();
		in.close();
		os.close();
	}

	public void dialogSearch() {
		final Dialog dialog = new Dialog(ExplorerActivity.this,
				android.R.style.Theme_Dialog);
		dialog.setContentView(R.layout.search);
		dialog.setTitle("Search");
		final EditText text = (EditText) dialog
				.findViewById(R.id.etSearchFileName);
		Button ok = (Button) dialog.findViewById(R.id.btOKSearch);
		Spinner spinnerPath = (Spinner) dialog.findViewById(R.id.spinerPath);
		Spinner spinnerCategory = (Spinner) dialog
				.findViewById(R.id.spinerCategory);
		File f = new File(myPath.getText().toString());
		ArrayList<String> path = new ArrayList<String>();
		ArrayList<String> category = new ArrayList<String>();
		category.add("All");
		category.add("Image");
		category.add("Audio");
		category.add("video");
		category.add("Document");
		ArrayAdapter<String> adapterCate = new ArrayAdapter<String>(
				ExplorerActivity.this, R.layout.row_path, R.id.tvRowPath,
				category);
		adapterCate.setDropDownViewResource(R.layout.row_path);
		spinnerCategory.setAdapter(adapterCate);
		File[] files = f.listFiles();
		path.add("Current Path");
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				path.add(files[i].getName());
			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				ExplorerActivity.this, R.layout.row_path, R.id.tvRowPath, path);
		adapter.setDropDownViewResource(R.layout.row_path);
		spinnerPath.setAdapter(adapter);
		spinnerPath.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (text.getText().toString().equals("")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ExplorerActivity.this);
					builder.setTitle("Info missing!");
					builder.setMessage("Please enter file name!");
					builder.setPositiveButton("Continue",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					builder.show();
				} else {
					search_file(text.getText().toString());
				}
			}
		});
		Button cancel = (Button) dialog.findViewById(R.id.btCancelSearch);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.hide();
			}
		});
		dialog.show();
	}

	public void creatNewfolder() {
		final Dialog d = new Dialog(ExplorerActivity.this,
				android.R.style.Theme_Dialog);
		d.setContentView(R.layout.rename);
		d.setTitle("New folder");
		final EditText text = (EditText) d.findViewById(R.id.etRename);
		// text.setText("New folder");
		Button ok = (Button) d.findViewById(R.id.btOKRename);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				File f = new File(file.getPath() + "/"
						+ text.getText().toString());
				if (f.exists() == false) {
					f.mkdirs();
				}
				getDirection(myPath.getText().toString());
				d.hide();
			}
		});
		Button cancel = (Button) d.findViewById(R.id.btCancelRename);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.hide();
			}
		});
		d.show();
	}

	public void dialogSort() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ExplorerActivity.this);
		builder.setTitle("Sort By");
		final String[] items = { "Name", "Size", "Type", "Modified" };
		builder.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (items[which].equals("Name")) {
							sx = 1;
							getDirection(myPath.getText().toString());
						} else if (items[which].equals("Size")) {
							sx = 2;
							getDirection(myPath.getText().toString());
						} else if (items[which].equals("Modified")) {
							sx = 3;
							getDirection(myPath.getText().toString());
						} else if (items[which].equals("Type")) {
							sx = 4;
							getDirection(myPath.getText().toString());
						}
					}
				});
		builder.setPositiveButton("Ascending",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences.Editor editor = mSettings.edit();
						editor.putInt(PREFS_SORT, sx);
						editor.commit();
					}
				});
		builder.setNegativeButton("Descending",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.show();
	}

	public void search_file(String name) {
		new Background(SEARCH).execute(name);
	}

	public void delete_file(String name) {
		new Background(DELETE).execute(name);
	}

	public void copyFile(String oldLocation, String newLocation) {
		String[] data = { oldLocation, newLocation };
		new Background(COPY).execute(data);
	}

	public void zip_file(String path) {
		new Background(ZIP).execute(path);
	}

	public void unZipFile(String file, String path) {
		new Background(UNZIP).execute(file, path);
	}

	public void unZipFileToDir(String name, String newDir, String oldDir) {
		new Background(UNZIPTO).execute(name, newDir, oldDir);
	}

	private void search_file(String dir, String fileName, ArrayList<String> n) {
		File root_dir = new File(dir);
		String[] list = root_dir.list();
		if (list != null && root_dir.canRead()) {
			int len = list.length;
			for (int i = 0; i < len; i++) {
				File check = new File(dir + "/" + list[i]);
				String name = check.getName();
				if (check.isFile()
						&& name.toLowerCase().contains(fileName.toLowerCase())) {
					n.add(check.getPath());
				} else if (check.isDirectory()) {
					if (name.toLowerCase().contains(fileName.toLowerCase()))
						n.add(check.getPath());
					else if (check.canRead() && !dir.equals("/"))
						search_file(check.getAbsolutePath(), fileName, n);
				}
			}
		}
	}

	public ArrayList<String> searchInDirectory(String dir, String pathName) {
		ArrayList<String> names = new ArrayList<String>();
		search_file(dir, pathName, names);
		return names;
	}

	public int copyToDirectory(String old, String newDir) {
		File old_file = new File(old);
		File temp_dir = new File(newDir);
		byte[] data = new byte[BUFFER];
		int read = 0;
		if (old_file.isFile() && temp_dir.isDirectory() && temp_dir.canWrite()) {
			String file_name = old
					.substring(old.lastIndexOf("/"), old.length());
			File cp_file = new File(newDir + file_name);
			try {
				BufferedOutputStream o_stream = new BufferedOutputStream(
						new FileOutputStream(cp_file));
				BufferedInputStream i_stream = new BufferedInputStream(
						new FileInputStream(old_file));
				while ((read = i_stream.read(data, 0, BUFFER)) != -1)
					o_stream.write(data, 0, read);
				o_stream.flush();
				i_stream.close();
				o_stream.close();
			} catch (FileNotFoundException e) {
				Log.e("FileNotFoundException", e.getMessage());
				return -1;
			} catch (IOException e) {
				Log.e("IOException", e.getMessage());
				return -1;
			}
		} else if (old_file.isDirectory() && temp_dir.isDirectory()
				&& temp_dir.canWrite()) {
			String files[] = old_file.list();
			String dir = newDir
					+ old.substring(old.lastIndexOf("/"), old.length());
			int len = files.length;
			if (!new File(dir).mkdir())
				return -1;
			for (int i = 0; i < len; i++)
				copyToDirectory(old + "/" + files[i], dir);
		} else if (!temp_dir.canWrite())
			return -1;
		return 0;
	}

	public void extractZipFiles(String zip_file, String directory) {
		byte[] data = new byte[BUFFER];
		String name, path, zipDir;
		ZipEntry entry;
		ZipInputStream zipstream;
		if (!(directory.charAt(directory.length() - 1) == '/'))
			directory += "/";
		if (zip_file.contains("/")) {
			path = zip_file;
			name = path.substring(path.lastIndexOf("/") + 1, path.length() - 4);
			zipDir = directory + name + "/";
		} else {
			path = directory + zip_file;
			name = path.substring(path.lastIndexOf("/") + 1, path.length() - 4);
			zipDir = directory + name + "/";
		}
		new File(zipDir).mkdir();
		try {
			zipstream = new ZipInputStream(new FileInputStream(path));
			while ((entry = zipstream.getNextEntry()) != null) {
				String buildDir = zipDir;
				String[] dirs = entry.getName().split("/");
				if (dirs != null && dirs.length > 0) {
					for (int i = 0; i < dirs.length - 1; i++) {
						buildDir += dirs[i] + "/";
						new File(buildDir).mkdir();
					}
				}
				int read = 0;
				FileOutputStream out = new FileOutputStream(zipDir
						+ entry.getName());
				while ((read = zipstream.read(data, 0, BUFFER)) != -1)
					out.write(data, 0, read);
				zipstream.closeEntry();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void zip_folder(File file, ZipOutputStream zout) throws IOException {
		byte[] data = new byte[BUFFER];
		int read;
		if (file.isFile()) {
			ZipEntry entry = new ZipEntry(file.getName());
			zout.putNextEntry(entry);
			BufferedInputStream instream = new BufferedInputStream(
					new FileInputStream(file));
			while ((read = instream.read(data, 0, BUFFER)) != -1)
				zout.write(data, 0, read);
			zout.closeEntry();
			instream.close();
		} else if (file.isDirectory()) {
			String[] list = file.list();
			int len = list.length;
			for (int i = 0; i < len; i++)
				zip_folder(new File(file.getPath() + "/" + list[i]), zout);
		}
	}

	public void createZipFile(String path) {
		File dir = new File(path);
		String[] list = dir.list();
		String name = path.substring(path.lastIndexOf("/"), path.length());
		String _path;
		if (!dir.canRead() || !dir.canWrite())
			return;
		int len = list.length;
		if (path.charAt(path.length() - 1) != '/')
			_path = path + "/";
		else
			_path = path;
		try {
			ZipOutputStream zip_out = new ZipOutputStream(
					new BufferedOutputStream(new FileOutputStream(_path + name
							+ ".zip"), BUFFER));
			for (int i = 0; i < len; i++)
				zip_folder(new File(_path + list[i]), zip_out);
			zip_out.close();
		} catch (FileNotFoundException e) {
			Log.e("File not found", e.getMessage());

		} catch (IOException e) {
			Log.e("IOException", e.getMessage());
		}
	}

	public int deleteTarget(String path) {
		File target = new File(path);

		if (target.exists() && target.isFile() && target.canWrite()) {
			target.delete();
			return 0;
		} else if (target.exists() && target.isDirectory() && target.canRead()) {
			String[] file_list = target.list();
			if (file_list != null && file_list.length == 0) {
				target.delete();
				return 0;
			} else if (file_list != null && file_list.length > 0) {
				for (int i = 0; i < file_list.length; i++) {
					File temp_f = new File(target.getAbsolutePath() + "/"
							+ file_list[i]);
					if (temp_f.isDirectory())
						deleteTarget(temp_f.getAbsolutePath());
					else if (temp_f.isFile())
						temp_f.delete();
				}
			}
			if (target.exists())
				if (target.delete())
					return 0;
		}
		return -1;
	}

	public void extractZipFilesFromDir(String zipName, String toDir,
			String fromDir) {
		if (!(toDir.charAt(toDir.length() - 1) == '/'))
			toDir += "/";
		if (!(fromDir.charAt(fromDir.length() - 1) == '/'))
			fromDir += "/";
		String org_path = fromDir + zipName;
		extractZipFiles(org_path, toDir);
	}

	public void dialogextractTo(final String name, final String oldDir) {
		final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Dialog);
		dialog.setContentView(R.layout.extract);
		dialog.setTitle("Extract file " + name);
		final EditText text = (EditText) dialog.findViewById(R.id.etExtract);
		final String s = text.getText().toString();
		Button ok = (Button) dialog.findViewById(R.id.btOKExtract);
		Button cancel = (Button) dialog.findViewById(R.id.btCancelExtract);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				unZipFileToDir(name, s, oldDir);
				dialog.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void about() {
		final Dialog dialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.about);
		RelativeLayout rl = (RelativeLayout) dialog.findViewById(R.id.rlAn);
		Animation anim = AnimationUtils.loadAnimation(mContext,
				R.anim.items_list);
		rl.startAnimation(anim);
		Button close = (Button) dialog.findViewById(R.id.btClose);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.hide();
			}
		});
		dialog.show();
	}

	public void setTheme() {
		switch (choose_theme) {
		case THEME_HIGHTLAND:
			theme.setBackgroundResource(R.drawable.background);
			rlcontrol.setBackgroundColor(Color.TRANSPARENT);
			rlLocal.setBackgroundResource(android.R.drawable.edit_text);
			break;
		case THEME_ANGEL:
			theme.setBackgroundResource(R.drawable.angel);
			rlcontrol.setBackgroundColor(Color.TRANSPARENT);
			rlLocal.setBackgroundColor(Color.TRANSPARENT);
			break;
		case THEME_ROMANCE:
			theme.setBackgroundResource(R.drawable.romance);
			rlcontrol.setBackgroundColor(Color.TRANSPARENT);
			rlLocal.setBackgroundColor(Color.TRANSPARENT);
			break;
		case THEME_GIRL:
			theme.setBackgroundResource(R.drawable.tamt);
			rlcontrol.setBackgroundColor(Color.TRANSPARENT);
			rlLocal.setBackgroundColor(Color.TRANSPARENT);
			break;
		}
	}

	public void stopThumbnailThread() {
		if (mThumbnail != null) {
			mThumbnail.setCancelThumbnails(true);
			mThumbnail = null;
		}
	}

	public void setShowThumbnails(boolean show) {
		thumbnail_flag = show;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private class Background extends AsyncTask<String, Void, ArrayList<String>> {
		private String file_name;
		private ProgressDialog pr_dialog;
		private int choose;
		private int copyDir;

		private Background(int choose) {
			this.choose = choose;
		}

		@Override
		protected void onPreExecute() {
			switch (choose) {
			case SEARCH:
				pr_dialog = ProgressDialog.show(mContext, "Searching",
						"Searching current file system...", true, true);
				break;
			case COPY:
				pr_dialog = ProgressDialog.show(mContext, "Copying",
						"Copying file...", true, false);
				break;
			case DELETE:
				pr_dialog = ProgressDialog.show(mContext, "Deleting",
						"Deleting files...", true, false);
				break;
			case ZIP:
				pr_dialog = ProgressDialog.show(mContext, "Zipping",
						"Zipping folder...", true, false);
				break;
			case UNZIP:
				pr_dialog = ProgressDialog.show(mContext, "Unzipping",
						"Unpacking zip file please wait...", true, false);
				break;
			case UNZIPTO:
				pr_dialog = ProgressDialog.show(mContext, "Unzipping",
						"Unpacking zip file please wait...", true, false);
				break;
			default:
				break;
			}

		}

		@Override
		protected ArrayList<String> doInBackground(String... params) {
			switch (choose) {
			case SEARCH:
				file_name = params[0];
				ArrayList<String> found = searchInDirectory("/sdcard/",
						file_name);
				return found;
			case COPY:
				copyDir = copyToDirectory(params[0], params[1]);
				return null;
			case UNZIP:
				extractZipFiles(params[0], params[1]);
				return null;
			case ZIP:
				createZipFile(params[0]);
				return null;
			case DELETE:
				for (int i = 0; i < params.length; i++) {
					deleteTarget(params[i]);
				}
				return null;
			case UNZIPTO:
				extractZipFilesFromDir(params[0], params[1], params[2]);
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(final ArrayList<String> file) {
			final CharSequence[] names;
			int len = file != null ? file.size() : 0;
			switch (choose) {
			case SEARCH:
				if (len == 0) {
					Toast.makeText(mContext, "Couldn't find " + file_name,
							Toast.LENGTH_SHORT).show();
				} else {
					names = new CharSequence[len];

					for (int i = 0; i < len; i++) {
						String entry = file.get(i);
						names[i] = entry.substring(entry.lastIndexOf("/") + 1,
								entry.length());
					}

					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setTitle("Found " + len + " file(s)");
					builder.setItems(names,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int position) {
									String path = file.get(position);
									System.out.println("link=" + path);
									getDirection(path.substring(0,
											path.lastIndexOf("/")));
								}
							});
					builder.setPositiveButton("Continue",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				pr_dialog.dismiss();
				break;
			case COPY:
				if (copyDir == 0) {
					Toast.makeText(mContext,
							"File successfully copied and pasted",
							Toast.LENGTH_SHORT).show();
					getDirection(myPath.getText().toString());
				} else {
					Toast.makeText(mContext, "Copy pasted failed",
							Toast.LENGTH_SHORT).show();
				}
				pr_dialog.dismiss();
				break;
			case DELETE:
				getDirection(myPath.getText().toString());
				pr_dialog.dismiss();
				break;
			case ZIP:
				getDirection(myPath.getText().toString());
				pr_dialog.dismiss();
				break;
			case UNZIP:
				getDirection(myPath.getText().toString());
				pr_dialog.dismiss();
				break;
			case UNZIPTO:
				getDirection(myPath.getText().toString());
				pr_dialog.dismiss();
				break;
			default:
				break;
			}
		}
	}

	public class sxType implements Comparator<FileFolder> {

		@Override
		public int compare(FileFolder lhs, FileFolder rhs) {
			String s1 = null;
			String s2 = null;
			int i;
			try {
				s1 = lhs.getName()
						.substring(lhs.getName().lastIndexOf(".") + 1,
								lhs.getName().length()).toLowerCase();
				s2 = rhs.getName()
						.substring(rhs.getName().lastIndexOf(".") + 1,
								rhs.getName().length()).toLowerCase();
			} catch (IndexOutOfBoundsException e) {
				return 0;
			}
			i = s1.compareTo(s2);
			if (i == 0) {
				lhs.getName().toLowerCase()
						.compareTo(rhs.getName().toLowerCase());
			}
			return i;
		}
	}

}