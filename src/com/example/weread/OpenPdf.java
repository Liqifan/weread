package com.example.weread;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class OpenPdf extends Fragment {

	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
				Bundle savedInstanceState){
			View v;
			v= inflater.inflate(R.layout.fragment_one, container,false);
			Bundle bundle = this.getArguments();
	        String strPdf = bundle.getString("message");
	  //  WebView mWebView=new WebView(OpenPdf.this);
			WebView mWebView=(WebView)v.findViewById(R.id.webView1);
			
	    mWebView.getSettings().setJavaScriptEnabled(true);
//	    mWebView.getSettings().setPluginState(true);
//	    String strPdf="http://fzs.sve-mo.ba/sites/default/files/dokumenti-vijesti/sample.pdf";
	    mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + strPdf);
	    //setContentView(mWebView);
	    return v;
	  }
	}