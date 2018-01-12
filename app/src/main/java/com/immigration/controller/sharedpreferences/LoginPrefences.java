package com.immigration.controller.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginPrefences {

    private static LoginPrefences instance = null;

    private LoginPrefences() {}


    public static LoginPrefences getInstance() {
        if (instance == null) {
            instance = new LoginPrefences();
        }
        return instance;
    }

    public static final String PRE_NAME = "login_gmc";

    public static final String PRE_Mobile = "mobile";
    public static final String PRE_pass = "pass";

    SharedPreferences preferences;

    public void addData(Context context, String mobile, String password) {
        this.preferences = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(PRE_Mobile, mobile.trim());
        editor.putString(PRE_pass, password.trim());
        editor.commit();
    }


    public void removeData(SharedPreferences preferences) {

        SharedPreferences.Editor editor = preferences.edit().clear();
        editor.apply();
    }


    public SharedPreferences getLoginPreferences(Context context) {
        this.preferences = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        return preferences;
    }


    public boolean hasValue(SharedPreferences preferences) {
        if ((preferences.getString(PRE_Mobile, "").equalsIgnoreCase("")) && preferences.getString(PRE_pass, "").equalsIgnoreCase("")) {
            return true;
        } else {
            return false;
        }
    }


   /* public String getUID(SharedPreferences preferences) {
        if (preferences.contains(PRE_USER_ID)) {
            return preferences.getString(PRE_USER_ID, "");
        } else {
            return null;
        }
    }*/

}