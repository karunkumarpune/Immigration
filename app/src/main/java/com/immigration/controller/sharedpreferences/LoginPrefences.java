package com.immigration.controller.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginPrefences {

    private static LoginPrefences instance = null;

    private LoginPrefences() {
    }


    public static LoginPrefences getInstance() {
        if (instance == null) {
            instance = new LoginPrefences();
        }
        return instance;
    }

    public static final String PRE_NAME = "login_gmc";

    public static final String PRE_AccessToken= "accessToken";
    public static final String PRE_UserId= "userId";
    public static final String PRE_CountryCode= "countryCode";
    public static final String PRE_Mobile = "mobile";

    public static final String PRE_pass = "pass";
    public static final String PRE_Email = "email";
    public static final String PRE_Fname = "fname";
    public static final String PRE_Lname = "lname";
    public static final String PRE_ProfilePic = "profilePic";

    SharedPreferences preferences;

    public void addData(Context context, String accessToken, String userId, String countryCode, String mobile, String password, String email, String firstName, String lastName, String profilePic) {
        this.preferences = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(PRE_AccessToken,accessToken);
        editor.putString(PRE_UserId, userId);
        editor.putString(PRE_CountryCode, countryCode);
        editor.putString(PRE_Mobile, mobile);
        editor.putString(PRE_pass, password);
        editor.putString(PRE_Email, email);
        editor.putString(PRE_Fname, firstName);
        editor.putString(PRE_Lname, lastName);
        editor.putString(PRE_ProfilePic, profilePic);
        editor.apply();
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


    public String getAccessToken(SharedPreferences preferences) {
        if (preferences.contains(PRE_AccessToken)) {
            return preferences.getString(PRE_AccessToken, "");
        } else {
            return null;
        }
    }

    public String getUserId(SharedPreferences preferences) {
        if (preferences.contains(PRE_UserId)) {
            return preferences.getString(PRE_UserId, "");
        } else {
            return null;
        }
    }
    public String getCountryCode(SharedPreferences preferences) {
        if (preferences.contains(PRE_CountryCode)) {
            return preferences.getString(PRE_CountryCode, "");
        } else {
            return null;
        }
    }

    public String getMobile(SharedPreferences preferences) {
        if (preferences.contains(PRE_Mobile)) {
            return preferences.getString(PRE_Mobile, "");
        } else {
            return null;
        }
    }

    public String getEmail(SharedPreferences preferences) {
        if (preferences.contains(PRE_Email)) {
            return preferences.getString(PRE_Email, "");
        } else {
            return null;
        }
    }
    public String getFName(SharedPreferences preferences) {
        if (preferences.contains(PRE_Fname)) {
            return preferences.getString(PRE_Fname, "");
        } else {
            return null;
        }
    }
    public String getLName(SharedPreferences preferences) {
        if (preferences.contains(PRE_Lname)) {
            return preferences.getString(PRE_Lname, "");
        } else {
            return null;
        }
    }

     public String getProfilePic(SharedPreferences preferences) {
        if (preferences.contains(PRE_ProfilePic)) {
            return preferences.getString(PRE_ProfilePic, "");
        } else {
            return null;
        }
    }

}