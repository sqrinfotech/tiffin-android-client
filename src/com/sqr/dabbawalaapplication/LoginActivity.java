package com.sqr.dabbawalaapplication;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.sqr.dabbawalaapplication.user.UserCredentials;
import com.sqr.dabbawalaapplication.utils.NetworkUtil;
import com.sqr.dabbawalaapplication.utils.Utilities;
import com.sqr.dabbawalaapplication.webservice.URLRequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText edit_username;
	private EditText edit_password;
	private Button btn_login;
	private Button btn_sign_up;
	private Button btn_forgot_password;
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences preferences = getSharedPreferences(Utilities.SESSION_PREFERENCE, MODE_PRIVATE);
		boolean isUserSignedIn = preferences.contains(Utilities.USER_CREDENTIALS);
		
		if(isUserSignedIn)
		{
			String userCredentials = preferences.getString(Utilities.USER_CREDENTIALS, null);
			Log.e("Preferences", userCredentials);
			startDabbawalaActivity();
			finish();
		}
		
		setContentView(R.layout.activity_login);

		edit_username = (EditText) findViewById(R.id.edit_username);
		edit_password = (EditText) findViewById(R.id.edit_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
		btn_forgot_password = (Button) findViewById(R.id.btn_forgot_pswd);

		btn_login.setOnClickListener(this);
		btn_sign_up.setOnClickListener(this);
		btn_forgot_password.setOnClickListener(this);
		
		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("Signing In...");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		App.setIsAppInForeground(true);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		App.setIsAppInForeground(false);
		
		if(dialog.isShowing()){
			dialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		if (item.getItemId() == R.id.action_settings) {
			Intent intent = new Intent(LoginActivity.this,
					SettingsActivity.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_login) {
			// TODO: Login logic after server is ready

			if (isDataComplete()) {
				if (NetworkUtil.isNetworkAvailable(this)) {
					String username = edit_username.getText().toString();
					String password = edit_password.getText().toString();

					SharedPreferences defaults = PreferenceManager
							.getDefaultSharedPreferences(this);
					String server_ip = defaults.getString("ipAddress",
							"192.168.4.4");

					String url = "http://" + server_ip + ":3000/users/login";

					new LoginAuthenticationTask().execute(url, username,
							password);
				} else {
					Toast.makeText(this, "There is no internet connection!",
							Toast.LENGTH_SHORT).show();
				}
			}

		} else if (v.getId() == R.id.btn_sign_up) {
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
		} else if(v.getId() == R.id.btn_forgot_pswd){
			Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
			startActivity(intent);
		}

	}

	private Boolean isDataComplete() {
		boolean isDataComplete = true;

		if (edit_username.getText().length() == 0) {
			isDataComplete = false;
			edit_username.setError("Enter Username!");
		}

		if (edit_password.getText().length() == 0) {
			isDataComplete = false;
			edit_password.setError("Enter Password!");
		}

		return isDataComplete;
	}

	private void parseResponse(String result)
	{
		boolean isJSON = false;
		
		try{
			new JSONObject(result);
			isJSON = true;
		}
		catch(Exception e){
			isJSON = false;
		}
		
		if(isJSON){
			//TODO : Parse Json, save session data(userCredential object) in sharedPreferences and open new activity
			
			try {
				JSONObject userJson = new JSONObject(result);
				String user_id = userJson.getString("_id");
				String username = userJson.getString("username");
				
				UserCredentials credentials = new UserCredentials(user_id, username);
				Gson gson = new Gson();
				String credentialsJsonString = gson.toJson(credentials);
				
				SharedPreferences preferences = getSharedPreferences(Utilities.SESSION_PREFERENCE, MODE_PRIVATE);
				Editor edit = preferences.edit();
				edit.putString(Utilities.USER_CREDENTIALS, credentialsJsonString);
				edit.commit();
				
				Log.e("Preferences Saved", preferences.getString(Utilities.USER_CREDENTIALS, null));
				
				startDabbawalaActivity();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setCancelable(false);
			builder.setTitle("Login Failed!");
			builder.setMessage(result);
			
			builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.dismiss();
					
				}
			});
			
			AlertDialog dialog = builder.create();
			dialog.show();
			
			edit_username.setText(null);
			edit_password.setText(null);
		}
	}
	
	private void startDabbawalaActivity(){
		Intent intent = new Intent(LoginActivity.this, DabbawalaListActivity.class);
		startActivity(intent);
		finish();
	}
	
	private class LoginAuthenticationTask extends
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

			Log.d("URLS : ", urls[0] + " " + urls[1] + " " + urls[2]);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", urls[1]));
			params.add(new BasicNameValuePair("password", urls[2]));

			URLRequest req = new URLRequest();
			response = req.sendRequest(url, params);

			return response;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			Log.i("Login request Response", result);
			parseResponse(result);
		}

	}

}
