package com.retiredbrainiacs.model.feeds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentList {
    @SerializedName("from_user_id")
    @Expose
    private String fromUserId;
    @SerializedName("from_user_image")
    @Expose
    private String fromUserImage;
    @SerializedName("from_user_rating")
    @Expose
    private Integer fromUserRating;
    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("comment")
    @Expose
    private String comment;

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    @SerializedName("comment_date_time")
    @Expose
    private String commentTime;
    public Integer getEdit() {
        return edit;
    }

    public void setEdit(Integer edit) {
        this.edit = edit;
    }

    @SerializedName("edit")
    @Expose
    private Integer edit;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getWallPostImage() {
        return wallPostImage;
    }

    public void setWallPostImage(String wallPostImage) {
        this.wallPostImage = wallPostImage;
    }

    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("wall_post_user_image")
    @Expose
    private String wallPostImage;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserImage() {
        return fromUserImage;
    }

    public void setFromUserImage(String fromUserImage) {
        this.fromUserImage = fromUserImage;
    }

    public Integer getFromUserRating() {
        return fromUserRating;
    }

    public void setFromUserRating(Integer fromUserRating) {
        this.fromUserRating = fromUserRating;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}