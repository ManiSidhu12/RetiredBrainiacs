package com.retiredbrainiacs.apis;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.retiredbrainiacs.common.GlobalConstants;
import com.retiredbrainiacs.common.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SendImage
{
	private URL connectURL;
	private String phn,skype,country,city,adrs1,adrs2,zipcode,imagetype,filename;
	private String response;
	byte[] dataToServer;
	Context ctx;
	String msg,id;
	String status,category;
	String open,close;
	String user_id;
	public SendImage(Context c,String u_id, String phone, String skypeId, String contry, String city1, String adress1, String address2, String pin, String filetype, String filename1) {
		try 
		{
			connectURL = new URL(GlobalConstants.API_URL+"sign_next_4_steps");
		}
		catch (Exception ex)
		{
			Log.i("URL FORMATION", "MALFORMATED URL");
		}
		
		ctx = c;
		user_id = u_id;
	phn = phone;
	skype = skypeId;
	country = contry;
	city = city1;
	adrs1 = adress1;
	adrs2 = address2;
	zipcode  = pin;

		imagetype=filetype;
		this.filename=filename1;
		Log.e("img", imagetype+filename);
	}



	public String doStart(FileInputStream stream)
	{
		fileInputStream = stream;
		return thirdTry();
	}

	FileInputStream fileInputStream = null;

	public String thirdTry()
	{
		String existingFileName = filename;

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String Tag = "3rd";
		try {
			
			HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
			//conn.setRequestProperty ("Authorization", "application/x-www-form-urlencoded");
			//conn.setRequestProperty("Accept", "application/json");

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(user_id + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			dos.writeBytes("Content-Disposition: form-data; name=\"phone\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(phn + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			dos.writeBytes("Content-Disposition: form-data; name=\"skype_id\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(skype + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			dos.writeBytes("Content-Disposition: form-data; name=\"iso\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(country + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			dos.writeBytes("Content-Disposition: form-data; name=\"location\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes("" + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			dos.writeBytes("Content-Disposition: form-data; name=\"city\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(city + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			dos.writeBytes("Content-Disposition: form-data; name=\"street_address_line1\""	+ lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(adrs1 + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			dos.writeBytes("Content-Disposition: form-data; name=\"street_address_line2\""	+ lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(adrs2 + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);


			dos.writeBytes("Content-Disposition: form-data; name=\"zip_code\""	+ lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(zipcode + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			//---------image
			dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""+ filename + "\"" + lineEnd);
			
			if (imagetype.equalsIgnoreCase("jpg"))
			{
				Log.i("imagetype", "1");
				dos.writeBytes("Content-type: image/jpg;" + lineEnd);
			}
			if (imagetype.equalsIgnoreCase("png"))
			{
				Log.i("imagetype", "2");
				dos.writeBytes("Content-type: image/png;" + lineEnd);
			}
			if (imagetype.equalsIgnoreCase("gif")) 
			{
				Log.i("imagetype", "3");
				dos.writeBytes("Content-type: image/gif;" + lineEnd);
			}
			if (imagetype.equalsIgnoreCase("jpeg"))
			{
				Log.i("imagetype", "4");
				dos.writeBytes("Content-type: image/jpeg;" + lineEnd);
			}
			dos.writeBytes(lineEnd);

			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];

			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) 
			{
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			Log.e(Tag, "File is written");
			fileInputStream.close();
			dos.flush();

			InputStream is = conn.getInputStream();
			
			int i = conn.getResponseCode();
			
			Log.e("Response of SignUp","fetched code"+i);
			int ch;

			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1)
			{
				b.append((char) ch);
			}
			String s = b.toString();
			Log.e("Response of SignUp","fetched"+s);
			JSONObject job = new JSONObject(s);
			 status = job.getString("status");
			msg = job.getString("message");

			if(status.equalsIgnoreCase("true"))
			{
				String img = job.getString("avt");
SharedPrefManager.getInstance(ctx).setUserImage(img);

			}
			else{

			}
			//response = "true";
			
			dos.close();

		} 
		catch (MalformedURLException ex)
		{
			Log.e(Tag, "error: " + ex.getMessage(), ex);
		}

		catch (IOException ioe) 
		{
			Log.e(Tag, "error: " + ioe.getMessage(), ioe);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e("msg",status+","+msg);
		return status+","+msg;
		
	
		
	
			}
}
