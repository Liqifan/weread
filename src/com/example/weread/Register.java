package com.example.weread;

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
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
	
	EditText et1;
	EditText et2;
	EditText et3;
	EditText et4;
	TextView msg;
	ImageButton register;
	JSONObject outputJson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		et1 = (EditText) findViewById(R.id.editText1);
		et2 = (EditText) findViewById(R.id.editText2);
		et3 = (EditText) findViewById(R.id.editText3);
		et4 = (EditText) findViewById(R.id.editText4);
		register = (ImageButton) findViewById(R.id.register);
		msg = (TextView) findViewById(R.id.textView5);
		
		
		register.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String UserID = et1.getText().toString();
				String UserName = et2.getText().toString();
				String UserOctopus = et3.getText().toString();
				String Password = et4.getText().toString();
				
				if(UserID.equals("")||UserName.equals("")||UserOctopus.equals("")||Password.equals("")){
					msg.setText("Please fill in with complete information.");
					
				}else{
				
				String[] params = new String[3];
				params[0] = "http";
				params[1] = "/UserInfo";
				params[2] = "?route=createUser" + "&key=" + UserID
					+ "&key2=" + UserName + "&key3=" + Password + "&key4=" + UserOctopus;
				MyAsynTask MyGetLoginTask = new MyAsynTask(Register.this);
				try {
					outputJson = MyGetLoginTask.execute(params).get();
					Log.d("register", outputJson.toString());
					String state = outputJson.getString("state");
					String message = outputJson.getString("Message");
					if(state.equals("FAIL")){
						if(message.equals("IntegrityError"))
						    msg.setText("User ID already exists.");
						else
							msg.setText("Registration failed: Unknown reason");
					}else{
						Toast.makeText(getBaseContext(),
								"Registration successful! ", Toast.LENGTH_LONG)
								.show();
						Intent successful = new Intent(Register.this, LoginActivity.class);
						Bundle loginInfo = new Bundle();
						loginInfo.putString("UserID", UserID);
						loginInfo.putString("Password", Password);
						Log.d("register", loginInfo.toString());
						successful.putExtra("loginInfo", loginInfo);
						startActivity(successful);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
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
		SharedPreferences sharedPref = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		String UserName = sharedPref.getString("UserName", "");
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
        	if(UserName.equals("")){
        	    Intent LoginIntent = new Intent(Register.this, LoginActivity.class);
			    startActivity(LoginIntent);
        	}else{
        		Intent LoginIntent = new Intent(Register.this, LoginSuccessful.class);
			    startActivity(LoginIntent);
        	}
            return true;
        case R.id.action_settings:
            // settings
            return true;  
            
        default:
            return super.onOptionsItemSelected(item);
		}
	}
}
