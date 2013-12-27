package com.sqr.dabbawalaapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
	
	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;
	
	
	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork && activeNetwork.isConnected()) 
		{
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;
			
			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		} 
		return TYPE_NOT_CONNECTED;
	}
	
	public static boolean isNetworkAvailable(Context context)
	{
		int conn = NetworkUtil.getConnectivityStatus(context);
		Boolean connectivity=null;
		
		if(conn == NetworkUtil.TYPE_WIFI||conn == NetworkUtil.TYPE_MOBILE)
		{
			connectivity=true;
		}
		else if(conn == NetworkUtil.TYPE_NOT_CONNECTED)
		{
			connectivity=false;
		}
		
		return connectivity;
	}
}
