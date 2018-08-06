package com.retiredbrainiacs.apis;

import android.content.Context;
import android.util.Log;

import com.retiredbrainiacs.common.GlobalConstants;
import com.retiredbrainiacs.common.SharedPrefManager;

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
import java.util.ArrayList;
import java.util.HashMap;

public class AddPostAPI {
    private URL connectURL;
    private String to_user_id,post_content,postType,imagetype,filename,mediaType;
    private String response;
    byte[] dataToServer;
    Context ctx;
    String msg,id;
    String status,category;
    String open,close;
    String user_id;
    ArrayList<HashMap<String, String>> listVideo;
    public AddPostAPI(Context c, String u_id, String to_id, String content, String type, String filetype, String filename1, String mType, ArrayList<HashMap<String, String>> videoList) {
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
        Log.e("img", imagetype+filename+listVideo);
    }



    public String doStart(FileInputStream stream)
    {
        fileInputStream = stream;
        return thirdTry();
    }

    FileInputStream fileInputStream = null;

    public String thirdTry()
    {
      //  String existingFileName = filename;
        String exsistingFileName = "";

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


            //---------image

          /*  if(mediaType.equalsIgnoreCase("image")) {

            }
            else if(mediaType.equalsIgnoreCase("video")){

            }
            else if(mediaType.equalsIgnoreCase("audio")){
                dos.writeBytes("Content-Disposition: form-data; name=\"audio\";filename=\"" + filename + "\"" + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"video\";filename=\"" + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + lineEnd);
            }
            else{
                dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"video\";filename=\"" + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"audio\";filename=\"" + lineEnd);
            }*/
if(mediaType.equalsIgnoreCase("image")) {
    dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + filename + "\"" + lineEnd);
    dos.writeBytes("Content-Disposition: form-data; name=\"video\";filename=\"" + lineEnd);
    dos.writeBytes("Content-Disposition: form-data; name=\"audio\";filename=\"" + lineEnd);
    if (imagetype.equalsIgnoreCase("jpg")) {
        Log.i("imagetype", "1");
        dos.writeBytes("Content-type: image/jpg;" + lineEnd);
    }
    if (imagetype.equalsIgnoreCase("png")) {
        Log.i("imagetype", "2");
        dos.writeBytes("Content-type: image/png;" + lineEnd);
    }
    if (imagetype.equalsIgnoreCase("gif")) {
        Log.i("imagetype", "3");
        dos.writeBytes("Content-type: image/gif;" + lineEnd);
    }
    if (imagetype.equalsIgnoreCase("jpeg")) {
        Log.i("imagetype", "4");
        dos.writeBytes("Content-type: image/jpeg;" + lineEnd);
    }
    if (imagetype.endsWith(".mp4")) {
        Log.i("videotype", "1");
        dos.writeBytes("Content-type: video/mp4;" + lineEnd);
    }
    if (imagetype.endsWith(".avi")) {
        Log.i("videotype", "2");
        dos.writeBytes("Content-type: video/avi;" + lineEnd);
    }
    if (imagetype.endsWith(".ogg")) {
        Log.i("videotype", "3");
        dos.writeBytes("Content-type: video/ogg;" + lineEnd);
    }
    if (imagetype.endsWith(".3gp")) {
        Log.i("videotype", "4");
        dos.writeBytes("Content-type: video/3gp;" + lineEnd);
    }
    dos.writeBytes(lineEnd);

}
if(mediaType.equalsIgnoreCase("video")) {
  //  dos.writeBytes("Content-Disposition: form-data; name=\"video\";filename=\"" + filename + "\"" + lineEnd);
  //  dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + lineEnd);
    dos.writeBytes("Content-Disposition: form-data; name=\"audio\";filename=\"" + lineEnd);
    if (listVideo != null && listVideo.size() > 0) {
        String[] keys = {"video", "image"};
        for (int i = 0; i < listVideo.size(); i++) {
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
        }

    }
}
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
                //String img = job.getString("avt");
               // SharedPrefManager.getInstance(ctx).setUserImage(img);

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
