package com.explorer.taskmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 12:42:32 AM Sep 5, 2012 Tel: 0974 878 244
 * 
 */
public class NativeRunner {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------
	private String command;
	private int retvalue;
	private String stderr;
	private String stdout;

	// ===========================================================
	// Constructors
	// ===========================================================
	public NativeRunner(String s) {
		command = s;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Getter & Setter
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static String runIt(String s) {
		return (new NativeRunner(s)).run();
	}

	public String getCommand() {
		return command;
	}

	public int getRetvalue() {
		return retvalue;
	}

	public String getStderr() {
		return stderr;
	}

	public String getStdout() {
		return stdout;
	}

	// ----------------------------------------------------------------------------------------------
	// Methods for/from SuperClass/Interfaces
	// ----------------------------------------------------------------------------------------------

	// ===========================================================
	// Methods
	// ===========================================================
	public String run() {
		String s;
		try {
			final Process process = Runtime.getRuntime().exec(command);
			final StringBuilder stringbuilder = new StringBuilder();
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					BufferedReader bufferedreader = new BufferedReader(
							new InputStreamReader(process.getInputStream()),
							8192);
					String s1;
					try {
						s1 = bufferedreader.readLine();
						stringbuilder.append(s1).append("\n");
						if (s1 != null)
							bufferedreader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
			});
			thread.start();
			final StringBuilder stringbuilder1 = new StringBuilder();
			Thread thread1 = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					BufferedReader bufferedreader = new BufferedReader(
							new InputStreamReader(process.getErrorStream()),
							8192);
					String s1;
					try {
						s1 = bufferedreader.readLine();
						stringbuilder1.append(s1).append("\n");
						if (s1 != null)
							bufferedreader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
			});
			thread1.start();
			retvalue = process.waitFor();
			do {
				if (!thread.isAlive()) {
					if (thread1.isAlive())
						thread1.interrupt();
					stdout = stringbuilder.toString();
					stderr = stringbuilder1.toString();
					s = (new StringBuilder(String.valueOf(stdout))).append(
							stderr).toString();
					break;
				}
				Thread.sleep(50L);
			} while (true);
		} catch (IOException ioexception) {
			s = null;
		} catch (InterruptedException interruptedexception) {
			s = null;
		} catch (Exception exception) {
			s = null;
		}
		return s;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
