package com.example.weread;

import java.io.File;
import java.io.IOException;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StartDownloadTask extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		v = inflater.inflate(R.layout.fragment_one, container, false);

		Bundle bundle = this.getArguments();
		String download_url = bundle.getString("Url");

		String path = download_url;// it contain your path of image..im using a
									// temp string..
		String filename = path.substring(path.lastIndexOf("/") + 1);
		new DownloadFile().execute(download_url, filename);

		
	
		// send file name to loadPDF
		Bundle bundle1 = new Bundle();
		String myMessage = download_url ;
		bundle1.putString("message", myMessage);
		FragmentManager FM = getFragmentManager();
		FragmentTransaction FT = FM.beginTransaction();
		//loadPDF F1 = new loadPDF();
		OpenPdf F1 = new OpenPdf();
		F1.setArguments(bundle1);
		FT.add(R.id.first_layout, F1);
		//FT.addToBackStack("f1");
		FT.commit();
		// }
		// }
		return v;
	}



	private class DownloadFile extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... strings) {
			String fileUrl = strings[0]; // ->
											// http://maven.apache.org/maven-1.x/maven.pdf
			String fileName = strings[1]; // -> maven.pdf
			String extStorageDirectory = Environment
					.getExternalStorageDirectory().toString();
			File folder = new File(extStorageDirectory, "testthreepdf");
			folder.mkdir();

			File pdfFile = new File(folder, fileName);

			try {
				pdfFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			FileDownloader.downloadFile(fileUrl, pdfFile);
			return null;
		}
	}

}
