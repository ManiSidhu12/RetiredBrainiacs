package com.retiredbrainiacs.apis;

import com.retiredbrainiacs.model.ResponseRoot;
import com.retiredbrainiacs.model.feeds.FeedsRoot;
import com.retiredbrainiacs.model.forum.ForumRoot;
import com.retiredbrainiacs.model.friend.AllFriendRoot;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @POST("?action=my_wall_post")
    Observable<FeedsRoot> getHomeFeeds(@QueryMap Map<String, String> params);

    @POST("?action=list_all_forums")
    Observable<ForumRoot> getForums(@QueryMap Map<String, String> params);

    @POST("?action=sign_next_4_steps")
    Observable<ResponseRoot> signUpSteps(@QueryMap Map<String, String> params);

    @POST("?action=wall_post")
    Observable<ResponseRoot> addPost(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("?action=show_all_users")
    Observable<AllFriendRoot> getAllUsers(@Field("user_id") String u_id);

    @FormUrlEncoded
    @POST("?action=send_friend_request")
    Observable<ResponseRoot> sendRequest(@Field("user_id") String u_id,@Field("sent_to") String receiver_id);

    @FormUrlEncoded
    @POST("?action=show_all_friend")
    Observable<AllFriendRoot> getFriends(@Field("user_id") String u_id);

    @FormUrlEncoded
    @POST("?action=all_friend_request")
    Observable<AllFriendRoot> getRequests(@Field("user_id") String u_id);

    @FormUrlEncoded
    @POST("?action=accept_request")
    Observable<ResponseRoot> acceptRequest(@Field("user_id") String u_id,@Field("sent_from") String id,@Field("act") String type);

    @Multipart
    @POST("?action=sign_next_4_steps")
    Call<ResponseRoot> uploadImage(@Part MultipartBody.Part image, @QueryMap Map<String, String> params);
}
