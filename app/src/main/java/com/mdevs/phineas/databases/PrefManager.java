package com.mdevs.phineas.databases;

import android.content.Context;
import android.content.SharedPreferences;

import com.mdevs.phineas.classes.User;

public class PrefManager {

    public static final String USER_TYPE = "userType";
    private static final String PREF_NAME = "com.mdevs.phineas";
    private static final String ID = "userID";
    private static final String IS_LOGGED_IN = "loggedIn";
    private static PrefManager prefInstance;
    private static Context mCtx;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public PrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized PrefManager getInstance(Context context) {
        if (prefInstance == null) {
            prefInstance = new PrefManager(context);
        }
        return prefInstance;
    }

    public void setUserLogin(User user) {

        sharedPreferences = mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString(ID, user.getId());
        editor.putString(USER_TYPE, user.getUserType());
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.apply();
    }


    public boolean isLoggedIn() {

        sharedPreferences = mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);

    }

    public String UserID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ID, null);
    }


    public String UserType() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_TYPE, null);
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
