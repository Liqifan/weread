package com.example.weread;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.RandomAccessFile;

import net.sf.andpdf.nio.ByteBuffer;
import net.sf.andpdf.refs.HardReference;


import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFImage;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPaint;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;



public class loadPDF extends Fragment {
	//Globals:
	private WebView wv;
	private int ViewSize = 0;
	Button bn;
	EditText msg;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState){
		View v;
		v= inflater.inflate(R.layout.fragment_one, container,false);
		
	    //Settings
	    PDFImage.sShowImages = true; // show images
	    PDFPaint.s_doAntiAlias = true; // make text smooth
	    HardReference.sKeepCaches = true; // save images in cache

	    //Setup webview
	    wv = (WebView)v.findViewById(R.id.webView1);
	    wv.getSettings().setBuiltInZoomControls(true);//show zoom buttons
	    wv.getSettings().setSupportZoom(true);//allow zoom
	    //get the width of the webview
	    wv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
	    {
	        @Override
	        public void onGlobalLayout()
	        {
	            ViewSize = wv.getWidth();
	            wv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	        }
	    });

	    try
	    {
	    	//Bundle extras = getIntent().getExtras();
	    	//if (extras != null) {
	    	//	if (extras.containsKey("url")) {
	    	//		Toast.makeText(this, "Please wait, loading the book now", Toast.LENGTH_SHORT).show();
	 		//    	String download_url=extras.getString("url");	 
	 		    	//String download_url="http://fzs.sve-mo.ba/sites/default/files/dokumenti-vijesti/sample.pdf";
	 		   // 	 String path=download_url;//it contain your path of image..im using a temp string..
	 		   //     String filename=path.substring(path.lastIndexOf("/")+1);
	    	Bundle bundle = this.getArguments();
	        String filename = bundle.getString("message");
	    //	String filename="pdf-sample.pdf";
			        File file = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + filename);
			        RandomAccessFile f = new RandomAccessFile(file, "r");
			        byte[] data = new byte[(int)f.length()];
			        f.readFully(data);
			        pdfLoadImages(data);
	   // 	}
	   // 	}
	    }
	    catch(Exception ignored)
	    {
	    }
		return v;
	}
	
	
	//Load Images:
		private void pdfLoadImages(final byte[] data)
		{
		    try
		    {
		        // run async
		        new AsyncTask<Void, Void, String>()
		        {
		            // create and show a progress dialog
		        	ProgressDialog  progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true); 
		        //    ProgressDialog progressDialog = ProgressDialog.show(loadPDF.this, "", "Opening...");

		            @Override
		            protected void onPostExecute(String html)
		            {
		                //after async close progress dialog
		                progressDialog.dismiss();
		                //load the html in the webview
		                wv.loadDataWithBaseURL("", html, "text/html","UTF-8", "");
		            }

		            @Override
		            protected String doInBackground(Void... params)
		            {
		                try
		                {
		                    //create pdf document object from bytes
		                    ByteBuffer bb = ByteBuffer.NEW(data);
		                    PDFFile pdf = new PDFFile(bb);
		                    //Get the first page from the pdf doc
		                    PDFPage PDFpage = pdf.getPage(1, true);
		                    //create a scaling value according to the WebView Width
		                    final float scale = ViewSize / PDFpage.getWidth() * 0.95f;
		                    //convert the page into a bitmap with a scaling value
		                    Bitmap page = PDFpage.getImage((int)(PDFpage.getWidth() * scale), (int)(PDFpage.getHeight() * scale), null, true, true);
		                    //save the bitmap to a byte array
		                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
		                    page.compress(Bitmap.CompressFormat.PNG, 100, stream);
		                    byte[] byteArray = stream.toByteArray();
		                    stream.reset();
		                    //convert the byte array to a base64 string
		                    String base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
		                    //create the html + add the first image to the html
		                    String html = "<!DOCTYPE html><html><body bgcolor=\"#b4b4b4\"><img src=\"data:image/png;base64,"+base64+"\" hspace=10 vspace=10><br>";
		                    //loop though the rest of the pages and repeat the above
		                    for(int i = 2; i <= pdf.getNumPages(); i++)
		                    {
		                        PDFpage = pdf.getPage(i, true);
		                        page = PDFpage.getImage((int)(PDFpage.getWidth() * scale), (int)(PDFpage.getHeight() * scale), null, true, true);
		                        page.compress(Bitmap.CompressFormat.PNG, 100, stream);
		                        byteArray = stream.toByteArray();
		                        stream.reset();
		                        base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
		                        html += "<img src=\"data:image/png;base64,"+base64+"\" hspace=10 vspace=10><br>";
		                    }
		                    stream.close();
		                    html += "</body></html>";
		                    return html;
		                }
		                catch (Exception e)
		                {
		                    Log.d("error", e.toString());
		                }
		                return null;
		            }
		        }.execute();
		        System.gc();// run GC
		    }
		    catch (Exception e)
		    {
		        Log.d("error", e.toString());
		    }
		}

	}

