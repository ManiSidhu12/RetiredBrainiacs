package com.retiredbrainiacs.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;

import com.retiredbrainiacs.R;


public class CommonUtils {
	Context ctx;
	private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;



	 private static int getConnectivityStatus(Context context) {
	        ConnectivityManager cm = (ConnectivityManager) context
	                .getSystemService(Context.CONNECTIVITY_SERVICE);
	 
	        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	        if (null != activeNetwork) {
	            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
	                return TYPE_WIFI;
	             
	            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
	                return TYPE_MOBILE;
	        } 
	        return TYPE_NOT_CONNECTED;
	    }
	     
	    public static String getConnectivityStatusString(Context context) {
	        int conn = CommonUtils.getConnectivityStatus(context);
	        String status = null;
	        if (conn == CommonUtils.TYPE_WIFI) {
	            status = "true";
	        } else if (conn == CommonUtils.TYPE_MOBILE) {
	            status = "true";
	        } else if (conn == CommonUtils.TYPE_NOT_CONNECTED) {
	            status = "false";
	        }
	        return status;
	    }
	public static ProgressDialog createProgressDialog(Context mContext) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		try {
			dialog.show();
		} catch (WindowManager.BadTokenException e) {

		}
		dialog.setCancelable(false);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.progressdialog);
		// dialog.setMessage(Message);
		return dialog;
	}
	    public static void openInternetDialog(final Context c){
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
			alertDialogBuilder.setTitle("Internet Alert!");
			alertDialogBuilder
					.setMessage("You are not connected to Internet..Please Enable Internet!")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
									
									final Intent intent = new Intent(Intent.ACTION_MAIN, null);
									intent.addCategory(Intent.CATEGORY_LAUNCHER);
									final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
									intent.setComponent(cn);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								c.startActivity( intent);
								//	ctx.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
								}
							})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
	    }

}
