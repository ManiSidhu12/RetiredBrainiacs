package com.retiredbrainiacs.common;

import android.app.Application;
import android.util.Log;

import com.retiredbrainiacs.R;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.util.ArrayList;
import java.util.HashMap;

public class Global extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();

        //finally initialize twitter with created configs
        Twitter.initialize(config);
    }

    public HashMap<String, ArrayList<HashMap<String, String>>> getListDataChild() {
        return listDataChild;
    }

    public void setListDataChild(HashMap<String, ArrayList<HashMap<String, String>>> listDataChild) {
        this.listDataChild = listDataChild;
    }

    HashMap<String, ArrayList<HashMap<String,String>>> listDataChild;

    public ArrayList<HashMap<String, String>> getListValues() {
        return listValues;
    }

    public void setListValues(ArrayList<HashMap<String, String>> listValues) {
        this.listValues = listValues;
    }

    ArrayList<HashMap<String, String>>   listValues;

}
