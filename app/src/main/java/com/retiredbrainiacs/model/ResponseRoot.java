package com.retiredbrainiacs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retiredbrainiacs.model.feeds.CommentList;

import java.util.List;

public class ResponseRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    @SerializedName("like_count")
    @Expose
    private String likeCount;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("avt")
    @Expose
    private String image;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @SerializedName("rating")
    @Expose
    private String rating;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @SerializedName("msg")
    @Expose
    private String msg;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}
    @SerializedName("favourite")
    @Expose
    private List<Favourite> favourite = null;

    public List<Favourite> getFavourite() {
        return favourite;
    }

    public void setFavourite(List<Favourite> favourite) {
        this.favourite = favourite;
    }
    public List<CommentList> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentList> commentList) {
        this.commentList = commentList;
    }

    @SerializedName("comment_list")
    @Expose
    private List<CommentList> commentList = null;
}