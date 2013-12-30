package com.sqr.dabbawalaapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.sqr.dabbawalaapplication.App;
import com.sqr.dabbawalaapplication.user.UserCredentials;

public class Utilities {
	
	public static final String SESSION_PREFERENCE = "sessionPreference";
	public static final String USER_CREDENTIALS = "userCredentials";
	
	
	public static UserCredentials getCurrentUser(Context context){
		SharedPreferences preferences = context.getSharedPreferences(Utilities.SESSION_PREFERENCE, 0);
		String userCredentials = preferences.getString(Utilities.USER_CREDENTIALS, null);
		
		Gson gson = new Gson();
		return (gson.fromJson(userCredentials, UserCredentials.class));
		
	}
}
