package com.retiredbrainiacs.apis;

import android.content.Context;
import android.util.Log;

import com.retiredbrainiacs.common.GlobalConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddForum {
    private URL connectURL;
    private String imagetype,filename;
    Context ctx;
    String msg;
    String status,mediaName="",mediaUrl="";
    String url;
    String type= "";
    public AddForum(Context c,String filetype, String filename1,String url,String tyype) {
        this.url = url;
        try
        {
            connectURL = new URL(url);
        }
        catch (Exception ex)
        {
            Log.i("URL FORMATION", "MALFORMATED URL");
        }

        ctx = c;


        imagetype=filetype;
        this.filename=filename1;
        type = tyype;
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


            //---------image
            if(type.equalsIgnoreCase("classified")){
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadimage[]\";filename=\""+ filename + "\"" + lineEnd);

            }
            else {
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadfile\";filename=\"" + filename + "\"" + lineEnd);
            }
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
            if (imagetype.equalsIgnoreCase("mp4"))
            {
                Log.i("imagetype", "4");
                dos.writeBytes("Content-type: video/mp4;" + lineEnd);
            }
            if (imagetype.equalsIgnoreCase("m4a"))
            {
                Log.i("imagetype", "4");
                dos.writeBytes("Content-type: audio/m4a;" + lineEnd);
            }
            if (imagetype.equalsIgnoreCase("doc"))
            {
                Log.i("imagetype", "4");
                dos.writeBytes("Content-type: file/doc;" + lineEnd);
            }
            if (imagetype.equalsIgnoreCase("docx"))
            {
                Log.i("imagetype", "4");
                dos.writeBytes("Content-type: file/docx;" + lineEnd);
            }
            if (imagetype.equalsIgnoreCase("xml"))
            {
                Log.i("imagetype", "4");
                dos.writeBytes("Content-type: file/xml;" + lineEnd);
            }
            if (imagetype.equalsIgnoreCase("xls"))
            {
                Log.i("imagetype", "4");
                dos.writeBytes("Content-type: file/xls;" + lineEnd);
            }
            if (imagetype.equalsIgnoreCase("xlsx"))
            {
                Log.i("imagetype", "4");
                dos.writeBytes("Content-type: file/xlsx;" + lineEnd);
            }
            if (imagetype.equalsIgnoreCase("pdf"))
            {
                Log.i("imagetype", "4");
                dos.writeBytes("Content-type: file/pdf;" + lineEnd);
            }
            if (imagetype.equalsIgnoreCase("txt"))
            {
                Log.i("imagetype", "4");
                dos.writeBytes("Content-type: file/txt;" + lineEnd);
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
            Log.e("Response of add forum","fetched"+s);
            JSONObject job = new JSONObject(s);
            status = job.getString("status");
            msg = job.getString("message");
mediaName = "";
            if(status.equalsIgnoreCase("true"))
            {
                if(type.equalsIgnoreCase("classified")){
                    JSONArray arr = job.getJSONArray("images");
                    for(int j = 0; j< arr.length();j++){
                        JSONObject obj = arr.getJSONObject(j);
                        mediaName = obj.getString("image");
                    }
                }
                else {
                    mediaName = job.getString("media_name");
                    mediaUrl = job.getString("uploaded_media_url");
                }
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
        return status+","+msg+","+mediaName;




    }
}
