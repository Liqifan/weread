package com.example.weread;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	JSONObject outputJson;
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button buttonsignin = (Button) findViewById(R.id.buttonsignin);

		buttonsignin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				EditText loginNameEditText = (EditText) findViewById(R.id.editloginname);
				EditText loginPasswordEditText = (EditText) findViewById(R.id.editpassword);

				String loginName = loginNameEditText.getText().toString();
				String loginPassword = loginPasswordEditText.getText()
						.toString();
				if (loginName.matches("") || loginPassword.matches("")) {
					Toast.makeText(getBaseContext(),
							"登入名稱 或 密碼不能空白!", Toast.LENGTH_LONG)
							.show();
				} else {

					String UserName = "";
					try {
						String[] params = new String[3];
		// e.g http://iems5722.ddns.net:5000/UserInfo/?route=getByUserID&key=111111&key2=123456
						params[0] = "http";
						params[1] = "/UserInfo/";
						params[2] = "?route=getByUserID" + "&key=" + loginName
								+ "&key2=" + loginPassword;
						MyAsynTask MyGetLoginTask = new MyAsynTask(
								LoginActivity.this);
						outputJson = MyGetLoginTask.execute(params).get();
						UserName = outputJson.getString("UserName");
						
						
						if (UserName.isEmpty()){
							String errmsg=outputJson.getString("Message");
							
							
							Toast.makeText(getBaseContext(),
									"登入失敗!("+errmsg+")", Toast.LENGTH_LONG)
									.show();
							}
						else{
							Toast.makeText(getBaseContext(),
									UserName+" 登入成功!", Toast.LENGTH_LONG)
									.show();
							finish();
							//to intent to main activity
							//Intent MainIntent = new Intent(MainActivity.class);
							//startActivity(MainIntent);
							
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
	    	finish();
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
