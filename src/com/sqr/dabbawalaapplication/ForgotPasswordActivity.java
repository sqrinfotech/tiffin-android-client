package com.sqr.dabbawalaapplication;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.sqr.dabbawalaapplication.utils.NetworkUtil;
import com.sqr.dabbawalaapplication.webservice.URLRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends Activity implements OnClickListener{
	
	private EditText edit_username;
	private EditText edit_email_id;
	private Button btn_send;
	
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_pswd);
		
		edit_username = (EditText) findViewById(R.id.edit_username);
		edit_email_id = (EditText) findViewById(R.id.edit_email);
		btn_send = (Button) findViewById(R.id.btn_send);
		
		btn_send.setOnClickListener(this);
		
		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("Sending request...");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btn_send){
			
			
			
			if(edit_email_id.getText().length() == 0 && edit_username.getText().length() == 0){
				Toast.makeText(this, "Enter either username or email ID", Toast.LENGTH_SHORT).show();
			}
			else{
				String urlParameter = null;
				
				if(edit_email_id.getText().length() != 0){
					String email = edit_email_id.getText().toString();
					
					if(isEmailFormatValid(email)){
						urlParameter = "email=" + email;
					}
					else{
						if(edit_username.getText().length() != 0){
							urlParameter = "username=" + edit_username.getText().toString();
						}
						else{
							edit_email_id.setError("Email format is invalid!");
						}
					}
					
				}
				else{
					urlParameter = "username=" + edit_username.getText().toString();
				}
				
				if(urlParameter != null){
					sendResetPasswordInstructions(urlParameter);
				}
				
			}
			
		}
		
	}
	
	private void sendResetPasswordInstructions(String urlParameter){
		if(NetworkUtil.isNetworkAvailable(this)){
			SharedPreferences defaults = PreferenceManager
					.getDefaultSharedPreferences(this);
			String server_ip = defaults.getString("ipAddress",
					"192.168.4.4");

			String url = "http://" + server_ip + ":3000/users/reset?" + urlParameter;
			
			new ResetPasswordTask().execute(url);
		}
		else{
			Toast.makeText(this, "There is no internet connection!", Toast.LENGTH_SHORT).show();
		}
	}
	
	private boolean isEmailFormatValid(String email){
		Pattern pattern;
		Matcher matcher;
		
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		if(! matcher.matches())
		{
			return false;
		}
		
		return true;
	}
	
	private void parseResponse(String result){
		//TODO : Parse response for error. In case of success display a toast message and navigate back to login
	}
	
	private class ResetPasswordTask extends
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
			response = req.sendRequest(url);
		
			return response;
		}
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			Log.i("Forgot Password Response", result);
			parseResponse(result);
		}
		
	}

}
