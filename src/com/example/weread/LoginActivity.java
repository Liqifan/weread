package com.example.weread;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {

	JSONObject outputJson;
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mContext = this;

		final EditText loginNameEditText = (EditText) findViewById(R.id.editloginname);
		final EditText loginPasswordEditText = (EditText) findViewById(R.id.editpassword);
		Intent intent = getIntent();
		Bundle loginInfo = intent.getBundleExtra("loginInfo");
		if(loginInfo!=null){		
		Log.d("login", loginInfo.getString("UserID"));
		loginNameEditText.setText(loginInfo.getString("UserID"));
		loginPasswordEditText.setText(loginInfo.getString("Password"));
		}
		
		ImageButton buttonsignin = (ImageButton) findViewById(R.id.buttonsignin);
		ImageButton register = (ImageButton) findViewById(R.id.register);
		final SharedPreferences sharedPref = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

		buttonsignin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				

				String loginName = loginNameEditText.getText().toString();
				String loginPassword = loginPasswordEditText.getText()
						.toString();
				if (loginName.matches("") || loginPassword.matches("")) {
					Toast.makeText(getBaseContext(),
							"User ID and password cannot be empty!", Toast.LENGTH_LONG)
							.show();
				} else {

					String UserID = "";
					try {
						String[] params = new String[3];
		// e.g http://iems5722.ddns.net:5000/UserInfo/?route=getByUserID&key=111111&key2=123456
						params[0] = "http";
						params[1] = "/UserInfo";
						params[2] = "?route=getByUserID" + "&key=" + loginName
								+ "&key2=" + loginPassword;
						MyAsynTask MyGetLoginTask = new MyAsynTask(
								LoginActivity.this);
						outputJson = MyGetLoginTask.execute(params).get();
						Log.d("login", outputJson.toString());
						String UserName = outputJson.getString("UserName");
						String UserOctopus = outputJson.getString("UserOctopus");
						UserID = outputJson.getString("UserID");
						
						
						if (UserID.isEmpty()){
							String errmsg=outputJson.getString("Message");
							
							
							Toast.makeText(getBaseContext(),
									"Login failed!("+errmsg+")", Toast.LENGTH_LONG)
									.show();
							}
						else{
							Toast.makeText(getBaseContext(),
									UserName+" Login successful!", Toast.LENGTH_LONG)
									.show();
							
							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putString("UserName", UserName);
							editor.putString("UserOctopus", UserOctopus);
							editor.commit();
							Log.d("login", sharedPref.getString("UserName", ""));
							
							Intent successful = new Intent(LoginActivity.this, LoginSuccessful.class);
							startActivity(successful);
							
						}						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

		});
		
		register.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent regIntent = new Intent(mContext, Register.class);
			    startActivity(regIntent);
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
