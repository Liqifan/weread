package com.example.weread;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginSuccessful extends Activity {
	
	ImageButton logout;
	TextView username;
	TextView octopus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_successful);
		getActionBar().setTitle("User Info");
		
		logout = (ImageButton)findViewById(R.id.logout);
		username = (TextView)findViewById(R.id.username);
		octopus = (TextView)findViewById(R.id.octopus);
		
		final SharedPreferences sharedPref = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		username.setText(sharedPref.getString("UserName", ""));
		octopus.setText(sharedPref.getString("UserOctopus", ""));
		
		logout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.commit();
                finish();
                //Intent LoginIntent = new Intent(LoginSuccessful.this, LoginActivity.class);
    			//startActivity(LoginIntent);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {  
	    case android.R.id.home:  
	    	Intent upIntent = NavUtils.getParentActivityIntent(this);  
	        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {  
	            TaskStackBuilder.create(this)  
	                    .addNextIntentWithParentStack(upIntent)  
	                    .startActivities();  
	        } else {  
	            upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	            NavUtils.navigateUpTo(this, upIntent);  
	        }  
	        return true;    
	    case R.id.action_comment:
            // view comment
            return true;
        case R.id.action_findlocation:
            // find location on map
            return true;
        case R.id.action_login:
            // login
            return true;
        case R.id.action_settings:
            // settings
            return true;  
            
        default:
            return super.onOptionsItemSelected(item);
		}
	}
}
