package com.sqr.dabbawalaapplication;

import org.json.JSONObject;

import com.sqr.dabbawalaapplication.user.UserCredentials;
import com.sqr.dabbawalaapplication.utils.Utilities;
import com.sqr.dabbawalaapplication.webservice.URLRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class ProfileActivity extends Activity {
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		dialog = new ProgressDialog(this);
		dialog.setMessage("Fetching Profile...");
		dialog.setCanceledOnTouchOutside(false);
		
		fetchProfile();
		
		
	}
	
	public void fetchProfile(){
		
		UserCredentials userCredentials = Utilities.getCurrentUser(this);
		
		SharedPreferences defaults = PreferenceManager
				.getDefaultSharedPreferences(this);
		String server_ip = defaults.getString("ipAddress",
				"192.168.4.4");
		
		String url = "http://" + server_ip + ":3000/users/"+userCredentials.getUser_id()+"/show";
		new FetchProfileTask().execute(url);
	}
	
	public void parseResponse(String response){
		
		boolean isJson = false;
		
		try{
			new JSONObject(response);
			isJson = true;
		}
		catch(Exception e){
			isJson = false;
		}
		
		if(!isJson){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setCancelable(false);
			builder.setTitle("Session Expired!");
			builder.setMessage("Your session expired. Please login again!");
			
			builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.dismiss();
					Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
					startActivity(intent);
					
				}
			});
			
			AlertDialog dialog = builder.create();
			dialog.show();
			
		}else{
			
		}
		
		
	}
	
	private class FetchProfileTask extends
	AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}
		
		@Override
		protected String doInBackground(String... urls) {
			String response = null;
		
			String url = urls[0];
		
			URLRequest req = new URLRequest();
			response = req.sendRequest(ProfileActivity.this, url, true);
		
			return response;
		}
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			Log.i("Fetch Profile Response", result);
			parseResponse(result);
		}
		
	}

}
