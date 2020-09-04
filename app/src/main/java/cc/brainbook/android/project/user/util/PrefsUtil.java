package cc.brainbook.android.project.user.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class PrefsUtil {

    public static int getInt(Context context, String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        final SharedPreferences defPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = defPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static Boolean getBoolean(Context context, String key, Boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, Boolean value) {
        final SharedPreferences defPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = defPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

    public static void putString(Context context, String key, String value) {
        final SharedPreferences defPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = defPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static Object getFromJson(Context context, String key, Object defaultValue, Class<?> classOfT) {
        final Gson gson = new Gson();
        final String defaultJson = new Gson().toJson(defaultValue);
        final String json = PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultJson);
        return gson.fromJson(json, classOfT);
    }

    public static void putToJson(Context context, String key, Object value) {
        final SharedPreferences defPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = defPrefs.edit();
        final Gson gson = new Gson();
        final String json = gson.toJson(value);
        editor.putString(key, json);
        editor.apply();
    }

    /**
     * <p><code>PrefsUtil</code> instances should NOT be constructed.</p>
     */
    private PrefsUtil() {}

}
