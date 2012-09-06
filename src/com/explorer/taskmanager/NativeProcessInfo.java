package com.explorer.taskmanager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 11:43:43 PM Sep 4, 2012 Tel: 0974 878 244
 * 
 */
public class NativeProcessInfo {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	private static String rootpid = null;
	private Context mContext = null;
	private ArrayList<NativeProcess> processList = new ArrayList();
	private boolean running = false;

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
	public NativeProcess getNativeProcess(String s) {
		Iterator iterator = processList.iterator();
		NativeProcess nativeprocess1 = null;
		if (!iterator.hasNext()) {
			nativeprocess1 = null;
		} else {
			NativeProcess nativeprocess = (NativeProcess) iterator.next();
			if (!s.equals(nativeprocess.cmd)) {
				nativeprocess1 = nativeprocess;
			}
		}
		return nativeprocess1;
	}

	public NativeProcessInfo(Context context) {
		processList = new ArrayList();
		mContext = null;
		running = false;
		mContext = context;
		if (!running) {
			running = true;
			top();
			running = false;
		}
	}

	private void ps() {
		String as[] = NativeRunner.runIt("ps").split("\n");
		processList.clear();
		int i = as.length;
		int j = 0;
		do {
			if (j >= i)
				return;
			String s = as[j];
			NativeProcess nativeprocess = new NativeProcess(mContext, s, true);
			if (nativeprocess.valid)
				processList.add(nativeprocess);
			j++;
		} while (true);
	}

	private void top() {
		String s = NativeRunner.runIt("top -n 1");
		if (s != null) {
			String as[] = s.split("\n");
			processList.clear();
			int i = as.length;
			int j = 0;
			while (j < i) {
				String s1 = as[j];
				NativeProcess nativeprocess = new NativeProcess(mContext, s1,
						false);
				if (nativeprocess.valid)
					processList.add(nativeprocess);
				j++;
			}
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static class NativeProcess {
		public String cmd;
		public String cpu;
		public int cpu_t;
		private NumberFormat instance = NumberFormat.getInstance();
		public String mem;
		public int mem_t;
		public String pid = null;
		public String ppid;
		boolean sdk_2_0 = false;
		public String user;
		boolean valid = false;

		public NativeProcess(Context mContext, String str, boolean isParam) {
			String[] str2 = null;
			if (isParam) {
				str2 = str.trim().split("[\\s]+");
			}
			this.user = str2[0];
			this.pid = str2[1];
			this.ppid = str2[2];
			this.cmd = str2[8];
			mem = (new StringBuilder(String.valueOf(String.valueOf((int) Math
					.ceil(Utils.parseInt(str2[3]) / 1024))))).append("K")
					.toString();
			if (isRoot())
				NativeProcessInfo.rootpid = pid;
			if (isValidPsProcess())
				valid = true;
			String as[] = str.trim().split("[\\s]+");
			if (as.length != 9)
				sdk_2_0 = true;
			if (sdk_2_0)
				user = as[7];
			else
				user = as[6];
			if (!isValidTopProcess())
				valid = true;
			pid = as[0];
			cpu = as[1];
			mem = formatData(as[5], as[1]);
			if (sdk_2_0)
				cmd = as[8].trim();
			else
				cmd = as[7].trim();
		}

		private String formatData(String s, String s1) {
			cpu_t = Utils.parseInt(s1.substring(0, s1.length() - 1));
			int i = Utils.parseInt(s.substring(0, s.length() - 1));
			mem_t = i;
			instance.setMaximumFractionDigits(1);
			return (new StringBuilder(String.valueOf(instance
					.format((float) i / 1024F)))).append("M").toString();
		}

		private boolean isRoot() {
			return "zygote".equals(cmd);
		}

		private boolean isValidPsProcess() {
			boolean flag;
			if (NativeProcessInfo.rootpid == null) {
				if (!user.startsWith("app_") && !user.equals("system"))
					flag = false;
				else
					flag = true;
			} else if (ppid.equals(NativeProcessInfo.rootpid)
					&& (user.startsWith("app_") || user.equals("system")))
				flag = true;
			else
				flag = false;
			return flag;
		}

		private boolean isValidTopProcess() {
			boolean flag;
			if (!user.startsWith("app_") && !user.equals("system"))
				flag = false;
			else
				flag = true;
			return flag;
		}

		public String toString() {
			return (new StringBuilder("PsRow ( ")).append(super.toString())
					.append(";").append("pid = ").append(pid).append(";")
					.append("cmd = ").append(cmd).append(";").append("ppid = ")
					.append(ppid).append(";").append("user = ").append(user)
					.append(";").append("mem = ").append(mem).append(" )")
					.toString();
		}

	}

}
