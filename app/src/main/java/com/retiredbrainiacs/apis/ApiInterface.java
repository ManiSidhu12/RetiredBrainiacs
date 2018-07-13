package com.retiredbrainiacs.apis;

import com.retiredbrainiacs.model.feeds.FeedsRoot;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @POST("?action=my_wall_post")
    Observable<FeedsRoot> getHomeFeeds(@QueryMap Map<String,String> params);
}
