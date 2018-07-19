package com.retiredbrainiacs.apis;

import com.retiredbrainiacs.model.feeds.FeedsRoot;
import com.retiredbrainiacs.model.forum.ForumRoot;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @POST("?action=my_wall_post")
    Observable<FeedsRoot> getHomeFeeds(@QueryMap Map<String,String> params);

    @POST("?action=list_all_forums")
    Observable<ForumRoot> getForums(@QueryMap Map<String,String> params);


}
