package com.retiredbrainiacs.apis;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.retiredbrainiacs.common.Global;
import com.retiredbrainiacs.common.GlobalConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddClassified
{
	private URL connectURL;
	private String status,response;
	byte[] dataToServer;
	Context ctx;
	String user_id,title,location,category,video_url,description;
	ArrayList<HashMap<String, String>> imageUpload=new ArrayList<HashMap<String,String>>();
	Global global;

	public AddClassified(Context c,ArrayList<HashMap<String, String>> imageUploads,String u_id,String title1,String loc,String cat_id,String youtube,String desc) {
		try 
		{
			connectURL = new URL(GlobalConstants.API_URL+"add_classified");
		}
		catch (Exception ex)
		{
			Log.i("URL FORMATION", "MALFORMATED URL");
		}
		
		ctx = c;

		this.imageUpload = imageUploads;
user_id = u_id;
title = title1;
location = loc;
category = cat_id;
video_url = youtube;
description = desc;
		global = (Global)c.getApplicationContext();


Log.e("imges",imageUpload.toString());


	}

	public String doStart(FileInputStream stream)
	{
		fileInputStream = stream;
		return thirdTry();
	}

	FileInputStream fileInputStream = null;

	public String thirdTry()
	{
		String exsistingFileName = "";

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String Tag = "3rd";
		try {
			
			HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			//conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			dos.writeBytes("Content-Disposition: form-data; name=\"title\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(title + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			dos.writeBytes("Content-Disposition: form-data; name=\"location\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(location + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			
			
			dos.writeBytes("Content-Disposition: form-data; name=\"category\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(category + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			dos.writeBytes("Content-Disposition: form-data; name=\"video_url\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(video_url + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			
			
			dos.writeBytes("Content-Disposition: form-data; name=\"description\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(description + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(user_id + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);


			//--------------image
			if(imageUpload.size()> 0)
			{
				Log.e("size of Image",""+imageUpload.size());
				int j=0;
			for (int i = 0; i < imageUpload.size(); i++)
			{
				Log.e("Enter the for loop", "Enter the forloop"+imageUpload.get(i).get("key_path"));
				
				Log.e("value",imageUpload.get(i).get("key_path"));
				if (imageUpload.get(i).get("key_path")!=null) {
					Log.e("values", String.valueOf(i));
					//if (!imageUpload.get(i).get("key_path").equalsIgnoreCase("")) {
						File f = new File(imageUpload.get(i).get("key_path"));

						exsistingFileName = f.getName();
						Log.e("values12", String.valueOf(i));
						Log.e("exsistingFileName", exsistingFileName);
						fileInputStream = new FileInputStream(f);
						int v = i + 1;
						String value = "images" + v;
					dos.writeBytes("Content-Disposition: form-data; name=\"uploadimage[]\";filename=\""+ exsistingFileName + "\"" + lineEnd);

						if (exsistingFileName.endsWith(".jpg")) {
							Log.i("imagetype", "1");
							dos.writeBytes("Content-type: image/jpg;" + lineEnd);
						}
						if (exsistingFileName.endsWith(".png")) {
							Log.i("imagetype", "2");
							dos.writeBytes("Content-type: image/png;" + lineEnd);
						}
						if (exsistingFileName.endsWith(".gif")) {
							Log.i("imagetype", "3");
							dos.writeBytes("Content-type: image/gif;" + lineEnd);
						}
						if (exsistingFileName.endsWith(".jpeg")) {
							Log.i("imagetype", "4");
							dos.writeBytes("Content-type: image/jpeg;" + lineEnd);
						}

						if (exsistingFileName.endsWith(".pdf")) {
							Log.i("pdf", "1");
							dos.writeBytes("Content-type: application/pdf;" + lineEnd);
						}

						if (exsistingFileName.endsWith(".mp4")) {
							Log.i("videotype", "1");
							dos.writeBytes("Content-type: video/mp4;" + lineEnd);
						}
						if (exsistingFileName.endsWith(".avi")) {
							Log.i("videotype", "2");
							dos.writeBytes("Content-type: video/avi;" + lineEnd);
						}
						if (exsistingFileName.endsWith(".ogg")) {
							Log.i("videotype", "3");
							dos.writeBytes("Content-type: video/ogg;" + lineEnd);
						}
						if (exsistingFileName.endsWith(".3gp")) {
							Log.i("videotype", "4");
							dos.writeBytes("Content-type: video/3gp;" + lineEnd);
						}
						dos.writeBytes(lineEnd);


						int bytesAvailable = fileInputStream.available();
						int maxBufferSize = 1024 * 1024;
						int bufferSize = Math.min(bytesAvailable, maxBufferSize);
						byte[] buffer = new byte[bufferSize];

						int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

						while (bytesRead > 0) {
							dos.write(buffer, 0, bufferSize);
							bytesAvailable = fileInputStream.available();
							bufferSize = Math.min(bytesAvailable, maxBufferSize);
							bytesRead = fileInputStream.read(buffer, 0, bufferSize);
						}

						if (i < imageUpload.size()) {
							dos.writeBytes(lineEnd);
							dos.writeBytes(twoHyphens + boundary + lineEnd);
							//dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
							Log.e("VALUE", "" + i);
						} else {
							if (i + 1 == imageUpload.size()) {
								dos.writeBytes(lineEnd);
								dos.writeBytes(twoHyphens + boundary + lineEnd);
								dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
								Log.e("VALUE", "" + i);
							} else {
								dos.writeBytes(lineEnd);
								dos.writeBytes(twoHyphens + boundary + lineEnd);
								Log.e("VALUE", "" + i);

							}

						}
						Log.e("file Strem", "" + fileInputStream);

						//dos.close();
					}

			}
		}

			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];

			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.close();
			fileInputStream.close();
			dos.flush();
				
				Log.e(Tag, "File is written");

			InputStream is = conn.getInputStream();

			int ch;

			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			String s = b.toString();


			Log.e("webService",s);
			try {
				JSONObject job = new JSONObject(s);
				 status = job.getString("status");
				
				if(status.equalsIgnoreCase("true"))
				{
					global.getImageUpload().clear();
					response = job.getString("message");

				}
				else
				{
					response = job.getString("message");

				}
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			dos.close();
		} catch (MalformedURLException ex) {
			Log.e(Tag, "error: " + ex.getMessage(), ex);
		}

		catch (IOException ioe) {
			Log.e(Tag, "error: " + ioe.getMessage(), ioe);
		}
		return status+","+response;
	}

}

