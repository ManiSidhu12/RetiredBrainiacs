package com.retiredbrainiacs.apis;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


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

public class RegisterWebService
{
	private URL connectURL;
	private String status,response;
	byte[] dataToServer;
	Context ctx;
	private String to_user_id,post_content,postType,imagetype,filename,mediaType,user_id;
	ArrayList<HashMap<String, String>> listVideo;

	List<String > keys;
	String loc,date;
	String thumb_name,thumb_type;
	SharedPreferences sp;
	public RegisterWebService(Context c, String u_id, String to_id, String content, String type, String filetype, String filename1, String mType, ArrayList<HashMap<String, String>> videoList) {
		try
		{
			connectURL = new URL(GlobalConstants.API_URL+"wall_post");
		}
		catch (Exception ex)
		{
			Log.i("URL FORMATION", "MALFORMATED URL");
		}

		ctx = c;
		user_id = u_id;
		to_user_id = to_id;
		post_content = content;
		postType = type;
		mediaType = mType;

		imagetype=filetype;
		this.filename=filename1;
		listVideo = videoList;
		Log.e("img", postType+imagetype+filename+listVideo);
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
			
			
			dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(user_id + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			

			dos.writeBytes("Content-Disposition: form-data; name=\"to_user_id\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(to_user_id + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			dos.writeBytes("Content-Disposition: form-data; name=\"post_content\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(post_content + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			
			
			dos.writeBytes("Content-Disposition: form-data; name=\"post_type\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(postType + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			dos.writeBytes("Content-Disposition: form-data; name=\"file_align\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes("center" + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);




			dos.writeBytes("Content-Disposition: form-data; name=\"audio\"" + lineEnd);
			dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes("" + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			//---------image
		//	dos.writeBytes("Content-Disposition: multipart/Form-data; name=\"image\";filename=\""+ filename + "\"" + lineEnd);
			

if(listVideo.size()> 0)
{
String[] keys={"video","image"};
for(int i=0;i < listVideo.size();i++) {
	File f = new File(listVideo.get(i).get("key_path"));
	exsistingFileName = f.getName();
	Log.e("exsistingFileName", exsistingFileName);
	fileInputStream = new FileInputStream(f);
	dos.writeBytes("Content-Disposition: form-data; name=\"" + keys[i] + "\";filename=\"" + exsistingFileName + "\"" + lineEnd);

	//dos.writeBytes("Content-Disposition: form-data; name=\"" + "videos" + "\";filename=\"" + exsistingFileName + "\"" + lineEnd);
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
		Log.e("videotype", "4");
		dos.writeBytes("Content-type: video/3gp;" + lineEnd);
	}
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

	if (i < listVideo.size()) {
		dos.writeBytes(lineEnd);
		dos.writeBytes(twoHyphens + boundary + lineEnd);
		//dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		Log.e("VALUE", "" + i);
	} else {
		if (i + 1 == listVideo.size()) {
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
	///
}
	Log.e("aman", "aman" + thumb_name);

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

				//.....................................
		/*	if (imagetype.equalsIgnoreCase("jpg"))
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
			
			Log.e("Response of Edit Profile webService","fetched code"+i);
			int ch;

			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1)
			{
				b.append((char) ch);
			}
			String s = b.toString();
			Log.e("Response of Edit Profile webService","fetched"+s);
			response = "true";
			
			dos.close();

		} 
		catch (MalformedURLException ex)
		{
			Log.e(Tag, "error: " + ex.getMessage(), ex);
		}

		catch (IOException ioe) 
		{
			Log.e(Tag, "error: " + ioe.getMessage(), ioe);
		}
		return response;
		
	
		
	
			}
}*/
