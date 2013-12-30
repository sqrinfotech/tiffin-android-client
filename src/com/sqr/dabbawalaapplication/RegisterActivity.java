package com.sqr.dabbawalaapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.sqr.dabbawalaapplication.utils.IPUtils;
import com.sqr.dabbawalaapplication.utils.NetworkUtil;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener{
	
	private EditText edit_name;
	private EditText edit_username;
	private EditText edit_password;
	private EditText edit_confirm_password;
	private EditText edit_email;
	private EditText edit_addr_line1;
	private EditText edit_addr_line2;
	private EditText edit_city;
	private EditText edit_state;
	private EditText edit_zip_code;
	
	private Button btn_sign_up;
	
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		edit_name = (EditText) findViewById(R.id.edit_name);
		edit_username = (EditText) findViewById(R.id.edit_username);
		edit_password = (EditText) findViewById(R.id.edit_password);
		edit_confirm_password = (EditText) findViewById(R.id.edit_confirm_password);
		edit_email = (EditText) findViewById(R.id.edit_email_id);
		edit_addr_line1 = (EditText) findViewById(R.id.edit_addr_line1);
		edit_addr_line2 = (EditText) findViewById(R.id.edit_addr_line2);
		edit_city = (EditText) findViewById(R.id.edit_city);
		edit_state = (EditText) findViewById(R.id.edit_state);
		edit_zip_code = (EditText) findViewById(R.id.edit_zip_code);
		
		edit_name.setText("Raeesaa");
		edit_username.setText("raeesaa");
		edit_password.setText("raeesaa");
		edit_confirm_password.setText("raeesaa");
		edit_email.setText("raeesaa.rm@gmail.com");
		edit_addr_line1.setText("abc");
		edit_city.setText("abc");
		edit_state.setText("abc");
		edit_zip_code.setText("411048");
		
		btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
		
		btn_sign_up.setOnClickListener(this);
		
		dialog = new ProgressDialog(this);
		dialog.setMessage("Signing Up.. ");
		dialog.setCanceledOnTouchOutside(false);
	}

	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.btn_sign_up)
		{
			if(isDataValid())
			{
				if(NetworkUtil.isNetworkAvailable(this))
				{
					String name = edit_name.getText().toString();
					String username = edit_username.getText().toString();
					String password = edit_password.getText().toString();
					String emailId = edit_email.getText().toString();
					String city = edit_city.getText().toString();
					StringBuilder address = new StringBuilder();
					address.append(edit_addr_line1.getText().toString());
					
					if(edit_addr_line2.getText().length() !=0 )
					{
						address.append(", " + edit_addr_line2.getText().toString());
					}
					
					address.append(", " + edit_city.getText().toString());
					address.append(", " + edit_state.getText().toString());
					address.append(", " + edit_zip_code.getText().toString());
					
					SharedPreferences defaults = PreferenceManager.getDefaultSharedPreferences(this);
					String server_ip = defaults.getString("ipAddress", "192.168.4.4");
					
					String url = "http://"+server_ip+":3000/users/create";
					//String ipAddress = IPUtils.getIPAddress(true);
					//Log.i("IP Address", "from utils : " + ipAddress);
					
					new RegistrationTask().execute(url, name, username, password, emailId, address.toString(), city);
				}
				else
				{
					Toast.makeText(this, "There is no internet connection!", Toast.LENGTH_SHORT).show();
				}
				
			}
		}
		
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
		if(dialog.isShowing())
		{
			dialog.dismiss();
		}
	}
	
	private boolean isDataValid()
	{
		boolean isDataValid = true;
		
		if(edit_name.getText().length() == 0)
		{
			isDataValid = false;
			edit_name.setError("Enter Name!");
		}
		
		if(edit_username.getText().length() == 0)
		{
			isDataValid = false;
			edit_username.setError("Enter Username!");
		}
		else
		{
			String username = edit_username.getText().toString();
			Pattern pattern;
			Matcher matcher;
			
			final String USERNAME_PATTERN = "^[A-Za-z0-9_]*$";
			
			pattern = Pattern.compile(USERNAME_PATTERN);
			matcher = pattern.matcher(username);
			if(! matcher.matches())
			{
				edit_username.setError("Username can contain only alphabets, numbers and underscores!");
				isDataValid = false;
			}
		}
		
		if(edit_password.getText().length() == 0)
		{
			isDataValid = false;
			edit_password.setError("Enter Password!");
		}
		else if(edit_password.getText().length() < 8)
		{
			isDataValid = false;
			edit_password.setError("Password should be minimum 8 characters long!");
		}

		if(edit_confirm_password.getText().length() == 0)
		{
			isDataValid = false;
			edit_confirm_password.setError("Repeat Password!");
		}
		
		if(!(edit_password.getText().toString()).equals(edit_confirm_password.getText().toString()))
		{
			edit_confirm_password.setError("Confirm Password should be same as Password");
			edit_confirm_password.setText(null);
			isDataValid = false;
		}
		
		if(edit_email.getText().length() == 0)
		{
			isDataValid = false;
			edit_email.setError("Enter Email ID!");
		}
		else
		{
			String email = edit_email.getText().toString();
			Pattern pattern;
			Matcher matcher;
			
			final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			
			pattern = Pattern.compile(EMAIL_PATTERN);
			matcher = pattern.matcher(email);
			if(! matcher.matches())
			{
				edit_email.setError("Invalid Email ID!");
				isDataValid = false;
			}
		}
		
		if(edit_addr_line1.getText().length() == 0)
		{
			isDataValid = false;
			edit_addr_line1.setError("Enter Address Line 1!");
		}
		
		if(edit_city.getText().length() == 0)
		{
			isDataValid = false;
			edit_city.setError("Enter City!");
		}
		
		if(edit_state.getText().length() == 0)
		{
			isDataValid = false;
			edit_state.setError("Enter State!");
		}
		
		if(edit_zip_code.getText().length() == 0)
		{
			isDataValid = false;
			edit_zip_code.setError("Enter Zip Code!");
		}
		
		return isDataValid;
		
	}
	
	private void parseResponse(String result)
	{
		boolean isJSON;
		
		try {
			new JSONObject(result);
			isJSON = true;
		} catch (Exception e) {
			isJSON = false;
		}
		
		if(isJSON)
		{
			Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
			startActivity(intent);
		}
		else
		{
			//TODO : Proper error handling
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setCancelable(false);
			builder.setTitle("Registration failed!");
			builder.setMessage(result);
			builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
			
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}
	
	//AsyncTask class to make login service call in background
		private class RegistrationTask extends AsyncTask<String,String,String> 
		{
		 	@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog.show();
			}
			
			@Override
			protected String doInBackground(String... urls) {
				
				String url=urls[0]; 
				 
				 Log.d("URLS : ",urls[0]+" "+urls[1]+" "+urls[2]);
				 			 
		    	 List<NameValuePair> params = new ArrayList<NameValuePair>();
				// params.add(new BasicNameValuePair("name",urls[1]));
				 params.add(new BasicNameValuePair("username",urls[2]));
				 params.add(new BasicNameValuePair("password",urls[3]));
				 params.add(new BasicNameValuePair("email", urls[4]));
				// params.add(new BasicNameValuePair("address", urls[5]));
				// params.add(new BasicNameValuePair("location", urls[6]));
				 
				 URLRequest req=new URLRequest();
				 String response=req.sendRequest(url,params);
				  	    		    	 
		    	 return response;
			}
			
			protected void onPostExecute(String result) 
			{
				super.onPostExecute(result);
				dialog.dismiss();
				Log.i("Register request response : ", result);
				parseResponse(result);
		    }

			
		 }

}
