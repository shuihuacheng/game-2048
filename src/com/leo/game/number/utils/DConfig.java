package com.leo.game.number.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class DConfig {
	public static boolean DEBUG = true;

	public final static String _AppFloder = "/dApp/"; // 工作路径

	public static final String _Preference_Key_MAXSCORE = "_2014_maxScore";
	public static final String _Preference_Key_BESTSTEP = "_2014_bestStep";
	public static final String _Preference_Key_FeatureStep = "_2014_curStep";
	public static final String _Preference_Key_FeatureCells = "_2014_FeatureCells";

	public static final class Preference {
		public static void setLongPref(Context context, String key, Long value) {
			if (context == null) {
				return;
			}
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			if (pref != null) {
				Editor editor = pref.edit();
				if (editor != null) {
					editor.putLong(key, value);
					editor.commit();
				}
			}
		}

		public static long getLongPref(Context context, String key, long defaultValue) {
			if (context == null) {
				return defaultValue;
			}
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			if (pref != null) {
				return pref.getLong(key, defaultValue);
			}
			return defaultValue;
		}

		public static void setStringPref(Context context, String key, String value) {
			if (context == null) {
				return;
			}
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			if (pref != null) {
				Editor editor = pref.edit();
				if (editor != null) {
					editor.putString(key, value);
					editor.commit();
				}
			}
		}

		public static String getStringPref(Context context, String key, String defaultValue) {
			if (context == null) {
				return defaultValue;
			}
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			if (pref != null) {
				return pref.getString(key, defaultValue);
			}
			return defaultValue;
		}

		public static boolean getBooleanPref(Context context, String key, boolean defaultValue) {
			if (context == null) {
				return defaultValue;
			}
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			if (pref != null) {
				return pref.getBoolean(key, defaultValue);
			}
			return defaultValue;
		}

		public static void setBooleanPref(Context context, String key, boolean value) {
			if (context == null) {
				return;
			}
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			if (pref != null) {
				Editor editor = pref.edit();
				if (editor != null) {
					editor.putBoolean(key, value);
					editor.commit();
				}
			}
		}

		public static int getIntPref(Context context, String key, int defaultValue) {
			if (context == null) {
				return defaultValue;
			}
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			if (pref != null) {
				return pref.getInt(key, defaultValue);
			}
			return defaultValue;
		}

		public static void setIntPref(Context context, String key, int value) {
			if (context == null) {
				return;
			}
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			if (pref != null) {
				Editor editor = pref.edit();
				if (editor != null) {
					editor.putInt(key, value);
					editor.commit();
				}
			}
		}

		public static void removePref(Context context, String key) {
			if (context == null) {
				return;
			}
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			if (pref != null) {
				Editor editor = pref.edit();
				if (editor != null) {
					editor.remove(key);
					editor.commit();
				}
			}
		}

	}

}
