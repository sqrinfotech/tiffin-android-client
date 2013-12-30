package com.sqr.dabbawalaapplication;

import com.sqr.dabbawalaapplication.utils.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DabbawalaListActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_dabbawala_list);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dabbawalalist_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		if (item.getItemId() == R.id.action_signout) {
			SharedPreferences preferences = getSharedPreferences(Utilities.SESSION_PREFERENCE, MODE_PRIVATE);
			Editor edit = preferences.edit();
			edit.remove(Utilities.USER_CREDENTIALS);
			edit.commit();
			
			Intent intent = new Intent(DabbawalaListActivity.this, LoginActivity.class);
			startActivity(intent);
		}
		else if(item.getItemId() == R.id.action_view_profile){
			Intent intent = new Intent(DabbawalaListActivity.this, ProfileActivity.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

}
