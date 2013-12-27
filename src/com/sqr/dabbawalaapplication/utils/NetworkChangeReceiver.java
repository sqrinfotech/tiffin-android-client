package com.sqr.dabbawalaapplication.utils;

import com.sqr.dabbawalaapplication.App;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {

		int conn = NetworkUtil.getConnectivityStatus(context);
		
		if(conn==NetworkUtil.TYPE_NOT_CONNECTED)
		{
			/*AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("No Network Connection").setMessage("Please find an Internet Connection").setCancelable(
					false).setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();*/
			if(App.getIsAppInForeground()){
				Toast.makeText(App.getContext(), "Internet connection Lost!", Toast.LENGTH_LONG).show();
			}
			
			
		}
		
	}
}
