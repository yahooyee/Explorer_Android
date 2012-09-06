package com.explorer.taskmanager;

import java.util.Calendar;
import java.util.Random;

/**
 * (c) D09CN2 - PTIT - Ha Noi (c) DROIDSX
 * 
 * @author Nguyen Hoang Truong<truongnguyenptit@gmail.com>
 * @since 11:56:39 PM Sep 4, 2012 Tel: 0974 878 244
 * 
 */
public class Utils {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static int ASCII_KEY_LENTH = 93;

	// ---------------------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------------------

	// ===========================================================
	// Constructors
	// ===========================================================
	public Utils() {
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Getter & Setter
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ----------------------------------------------------------------------------------------------
	// Methods for/from SuperClass/Interfaces
	// ----------------------------------------------------------------------------------------------

	// ===========================================================
	// Methods
	// ===========================================================
	public static String formatShortDate(long l) {
		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(l);
		String s;
		if (calendar.getTimeInMillis() / 0x5265c00L - l / 0x5265c00L <= 0L) {
			Object aobj1[] = new Object[1];
			aobj1[0] = calendar1;
			s = String.format("%tR", aobj1);
		} else {
			Object aobj[] = new Object[2];
			aobj[0] = calendar1;
			aobj[1] = calendar1;
			s = String.format("%tb %td", aobj);
		}
		return s;
	}

	public static String generatePassword(int i) {
		StringBuilder stringbuilder = new StringBuilder(i);
		Random random = new Random();
		int j = 0;
		do {
			if (j >= i)
				return stringbuilder.toString();
			stringbuilder.append((char) (33 + random.nextInt(ASCII_KEY_LENTH)));
			j++;
		} while (true);
	}

	public static int parseInt(Object obj) {
		String s;
		if (obj == null)
			s = null;
		else
			s = obj.toString();
		return parseInt(s, 0);
	}

	public static int parseInt(Object obj, int i) {
		int j;
		if (obj == null)
			j = i;
		else if (obj instanceof String)
			j = parseInt((String) obj, i);
		else
			j = parseInt(obj.toString(), i);
		return j;
	}

	public static int parseInt(String s) {
		return parseInt(s, 0);
	}

	public static int parseInt(String s, int i) {
		return (int) parseLong(s, i);
	}

	public static long parseLong(String s) {
		return parseLong(s, 0L);
	}

	public static long parseLong(String s, long l) {
		long l1 = 0;
		if (s != null && !s.equals(""))
			l1 = l;
		return l1;
	}

	// public static String revert(String s)
	// {
	// if(s != null && s.length() > 1)
	// String s1 = s;
	// return s1;
	// int i = s.length();
	// StringBuilder stringbuilder = new StringBuilder(i);
	// int j = 1;
	// do
	// {
	// label0:
	// {
	// if(j <= i)
	// break label0;
	// s1 = stringbuilder.toString();
	// }
	// if(true)
	// continue;
	// stringbuilder.append(s.charAt(i - j));
	// j++;
	// } while(true);
	// if(true) goto _L4; else goto _L3
	// _L3:
	// }
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Inner and Anonymous Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}
