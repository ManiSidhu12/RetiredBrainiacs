package com.retiredbrainiacs.apis;

import android.content.Context;
import android.util.Log;

import com.retiredbrainiacs.common.GlobalConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddMemorialPages {
    private URL connectURL;
    private String imagetype,filename,actType,u_name,dob,dor,story1,story2,story3;
    private String response;
    byte[] dataToServer;
    Context ctx;
    String msg;
    String status;
    String user_id;
    public AddMemorialPages(Context c,String filetype, String filename1,String u_id,String type,String name,String datOfBirth,String dateofretire,String content1,String content2,String content3) {
        try
        {
            connectURL = new URL(GlobalConstants.API_URL+"add_memorial_page");
        }
        catch (Exception ex)
        {
            Log.i("URL FORMATION", "MALFORMATED URL");
        }

        ctx = c;
        user_id = u_id;
        imagetype=filetype;
        filename=filename1;
        actType = type;
        u_name = name;
        dob = datOfBirth;
        dor = dateofretire;
        story1 = content1;
        story2 = content2;
        story3 = content3;

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

            dos.writeBytes("Content-Disposition: form-data; name=\"act\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(actType + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"person_name\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(u_name + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"date_of_birth\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(dob + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"end_date\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(dor + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"sample_content1\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(story1 + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"sample_content2\""	+ lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(story2 + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"sample_content3\""	+ lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(story3 + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);


            dos.writeBytes("Content-Disposition: form-data; name=\"sample_content4\""	+ lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            //---------image
            dos.writeBytes("Content-Disposition: form-data; name=\"cover_photo\";filename=\""+ filename + "\"" + lineEnd);

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
