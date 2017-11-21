package com.slp.com.uberclone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.lang.UCharacter;
import android.preference.PreferenceManager;

import com.slp.com.uberclone.LoginActivity;

/**
 * Created by slaks on 21/11/2017.
 */

public class PreferenceUtils implements UberConstants {
    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setDriver(Context context, boolean isDriver) {
        getPreferences(context).edit().putBoolean(DRIVER, isDriver).apply();
    }

    public static boolean isDriver(Context context) {
        return getPreferences(context).getBoolean(DRIVER, false);
    }
}
