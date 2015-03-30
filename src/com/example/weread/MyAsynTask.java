package com.example.weread;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;


public class MyAsynTask extends AsyncTask<String, Void, JSONObject> {
	// ArrayList<String> response = new ArrayList<String>();
	Context mycontxt;
	public static final String SERVER_NAME = "iems5722.ddns.net:5000";
	String aResult = "";
	JSONObject ajsonOut;

	public MyAsynTask(Context context) {
		this.mycontxt = context;
	}

	protected void onPreExecute() {
		super.onPreExecute();
	}

	protected JSONObject doInBackground(String... Params) {
		String getResult = "";
		if (Params[0] == "http") {
			try {
				String urls = "http://" + SERVER_NAME + "/?";
				urls = urls + Params[1]+ Params[2];
				HttpClient http_client = new DefaultHttpClient();
				HttpGet request = new HttpGet(urls);
				HttpResponse response = http_client.execute(request);
				HttpEntity entity = response.getEntity();
				getResult = EntityUtils.toString(entity, HTTP.UTF_8);
				ajsonOut = new JSONObject(getResult);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ajsonOut;
	}

	protected void onPostExecute(JSONObject j) {

		
		

	}
}

// package com.example.weread;
//
//
// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.PrintWriter;
// import java.net.InetAddress;
// import java.net.Socket;
// import java.net.UnknownHostException;
// import org.json.JSONException;
// import org.json.JSONObject;
// import android.content.Context;
// import android.os.AsyncTask;
// import android.util.Log;
// import android.view.Gravity;
// import android.widget.Toast;
//
// public class MyAsynTask extends AsyncTask<String, Void, JSONObject> {
// private final String TAG = "TranslateTask";
// String tcpAddress, connection;
// int tcpPort;
// String aResult = "";
// JSONObject ajsonOut;
// String userInput;
// Context mycontxt;
//
// public TranslateTask(Context context, String tcpAddress, int tcpPort,
// String connection) {
// this.tcpAddress = tcpAddress;
// this.tcpPort = tcpPort;
// this.connection = connection;
//
// this.mycontxt = context;
//
// // // this.histManager = histManager;
// Log.d(TAG, "Init new task");
// }
//
// @Override
// protected JSONObject doInBackground(String... input) {
// Log.d(TAG, "Doing translate task");
// String userInput = input[0];
//
// if (this.connection.equals("tcp")) {
// // use tcp translation server
// translateTCP(userInput);
// } else {
// Log.e(TAG, "Unknown service requested");
// }
// Log.d(TAG, "Returning");
//
// if (!aResult.isEmpty()) {
//
// try {
// ajsonOut = new JSONObject(
// "{\"status\":\"FAIL\",\"output\":\"\",\"message\":\""
// + aResult + "\"}");
// } catch (JSONException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
//
// }
//
// return ajsonOut;
// }
//
// private void translateTCP(String userInput) {
// Log.d(TAG, "TCP translate");
// // IO on non UI thread
// Socket socket = null;
// String response = "";
// try {
// this.userInput = userInput;
//
// Log.d(TAG, "Trying to get socket");
// InetAddress destination = InetAddress.getByName(tcpAddress);
// Log.d(TAG, "Connecting to " + destination.getHostAddress());
// socket = new Socket(destination, tcpPort);
//
// // send data to server
//
// Log.d(TAG, "Sending " + userInput + " to server");
// PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
// writer.println(userInput);
//
// // read response from server
// Log.d(TAG, "Getting response");
// BufferedReader reader = new BufferedReader(new InputStreamReader(
// socket.getInputStream()));
// response += reader.readLine();
//
// ajsonOut = new JSONObject(response);
//
// Log.d(TAG, "Received " + response);
// reader.close();
// } catch (UnknownHostException e) {
// e.printStackTrace();
// aResult += "Unknown Host ";
// } catch (IOException e) {
// e.printStackTrace();
// aResult += "IOException ";
// } catch (JSONException e) {
// aResult += "JSONException ";
// // TODO Auto-generated catch block
// e.printStackTrace();
// } finally {
// Log.d(TAG, "Finally");
// if (socket != null) {
// try {
// socket.close();
// } catch (IOException e) {
// e.printStackTrace();
// aResult += "IOException ";
// }
//
// }
// }
//
// }
//
// @Override
// protected void onPostExecute(JSONObject j) {
//
// if (!aResult.isEmpty()) {
// Toast toast = Toast.makeText(this.mycontxt, aResult,
// Toast.LENGTH_SHORT);
// toast.setGravity(Gravity.CENTER_VERTICAL
// | Gravity.CENTER_HORIZONTAL, 0, 0);
// toast.show();
// }
//
// }
// }
