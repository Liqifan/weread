package com.example.weread;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;






import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button buttonread, B2;
	public static final String MIME_TEXT_PLAIN = "text/plain";
	public static final String TAG = "NfcDemo";

	private NfcAdapter mNfcAdapter;
	String ISBN = "";
	String URL = "";
	JSONObject outputJson;
	Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button buttonread = (Button) findViewById(R.id.buttonread);
		
		mContext = this;
		
		buttonread.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView showText = (TextView) findViewById(R.id.textView1);

				if (!ISBN.isEmpty()) {

					// to do

					Bundle bundle = new Bundle();
					bundle.putString("Url", getURL(ISBN));
					FragmentManager FM = getFragmentManager();
					FragmentTransaction FT = FM.beginTransaction();
					StartDownloadTask F1 = new StartDownloadTask();
					F1.setArguments(bundle);
					FT.replace(R.id.first_layout, F1);
					// FT.addToBackStack("f1");
					FT.commit();
					ISBN = "";
					showText.setText("");
				} else {
					// do nothing
				}

			}

		});		
		
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (mNfcAdapter == null) {
			// Stop here, we definitely need NFC
			Toast.makeText(this, "This device doesn't support NFC.",
					Toast.LENGTH_LONG).show();
			finish();
			return;

		}

		if (!mNfcAdapter.isEnabled()) {
			// mTextView.setText("NFC is disabled.");
		} else {
			// mTextView.setText(R.string.explanation);
		}

		handleIntent(getIntent());

	}

	protected String getURL(String input) {
		String URL = "";
		try {

			String[] params = new String[3];

			params[0] = "http";
			params[1] = "/BookInfo/";
			params[2] = "?route=getByBookID&key=" + ISBN;
			
			MyAsynTask MyGetUrlTask = new MyAsynTask(MainActivity.this);
			outputJson = MyGetUrlTask.execute(params).get();
			URL = outputJson.getString("BookURL");

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
		return URL;
	}

	@Override
	protected void onResume() {
		super.onResume();

		/**
		 * It's important, that the activity is in the foreground (resumed).
		 * Otherwise an IllegalStateException is thrown.
		 */
		setupForegroundDispatch(this, mNfcAdapter);
	}

	@Override
	protected void onPause() {
		/**
		 * Call this before onPause, otherwise an IllegalArgumentException is
		 * thrown as well.
		 */
		stopForegroundDispatch(this, mNfcAdapter);

		super.onPause();
	}

	@Override
	protected void onNewIntent(Intent intent) {

		handleIntent(intent);
	}

	public static void setupForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(),
				activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(
				activity.getApplicationContext(), 0, intent, 0);

		IntentFilter[] filters = new IntentFilter[1];
		String[][] techList = new String[][] {};

		// Notice that this is the same filter as in our manifest.
		filters[0] = new IntentFilter();
		filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filters[0].addCategory(Intent.CATEGORY_DEFAULT);
		try {
			filters[0].addDataType(MIME_TEXT_PLAIN);
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("Check your mime type.");
		}

		adapter.enableForegroundDispatch(activity, pendingIntent, filters,
				techList);
	}

	public static void stopForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}

	private void handleIntent(Intent intent) {
		// TODO: handle Intent

				String action = intent.getAction();
				if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

					String type = intent.getType();
					if (MIME_TEXT_PLAIN.equals(type)) {

						Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
						new NdefReaderTask().execute(tag);

					} else {
						Log.d(TAG, "Wrong mime type: " + type);
					}
				} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

					// In case we would still use the Tech Discovered Intent
					Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
					String[] techList = tag.getTechList();
					String searchedTech = Ndef.class.getName();

					for (String tech : techList) {
						if (searchedTech.equals(tech)) {
							new NdefReaderTask().execute(tag);
							break;
						}
					}
				}

	}

	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

		@Override
		protected String doInBackground(Tag... params) {
			Tag tag = params[0];

			Ndef ndef = Ndef.get(tag);
			if (ndef == null) {
				// NDEF is not supported by this Tag.
				return null;
			}

			NdefMessage ndefMessage = ndef.getCachedNdefMessage();

			NdefRecord[] records = ndefMessage.getRecords();
			for (NdefRecord ndefRecord : records) {
				if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN
						&& Arrays.equals(ndefRecord.getType(),
								NdefRecord.RTD_TEXT)) {
					try {
						return readText(ndefRecord);
					} catch (UnsupportedEncodingException e) {
						Log.e(TAG, "Unsupported Encoding", e);
					}
				}
			}

			return null;
		}

		private String readText(NdefRecord record)
				throws UnsupportedEncodingException {
			/*
			 * See NFC forum specification for "Text Record Type Definition" at
			 * 3.2.1
			 * 
			 * http://www.nfc-forum.org/specs/
			 * 
			 * bit_7 defines encoding bit_6 reserved for future use, must be 0
			 * bit_5..0 length of IANA language code
			 */

			byte[] payload = record.getPayload();

			// Get the Text Encoding
			String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8"
					: "UTF-16";

			// Get the Language Code
			int languageCodeLength = payload[0] & 0063;

			// String languageCode = new String(payload, 1, languageCodeLength,
			// "US-ASCII");
			// e.g. "en"

			// Get the Text
			return new String(payload, languageCodeLength + 1, payload.length
					- languageCodeLength - 1, textEncoding);
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {

				ISBN = result;
				TextView showText = (TextView) findViewById(R.id.textView1);
				Uri notification = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				Ringtone r = RingtoneManager.getRingtone(
						getApplicationContext(), notification);
				r.play();
				showText.setText(ISBN);
				// mTextView.setText("Read content: " + result);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences sharedPref = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		String UserName = sharedPref.getString("UserName", "");
		switch (item.getItemId()) {
        case R.id.action_comment:
        	Intent CmtIntent = new Intent(mContext, ViewComment.class);
        	CmtIntent.putExtra("BookID", ISBN);
        	startActivity(CmtIntent);
            // view comment
            return true;
        case R.id.action_findlocation:
            // find location on map
            return true;
        case R.id.action_login:
            // login page
        	if(UserName.equals("")){
        	    Intent LoginIntent = new Intent(mContext, LoginActivity.class);
			    startActivity(LoginIntent);
        	}else{
        		Intent LoginIntent = new Intent(mContext, LoginSuccessful.class);
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
