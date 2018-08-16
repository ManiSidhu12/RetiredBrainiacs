package com.retiredbrainiacs.model.feeds;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retiredbrainiacs.activities.Interests;

import java.util.List;

public class Post implements Parcelable {
    @SerializedName("wall_post_user_name")
    @Expose
    private String wallPostUserName;
    @SerializedName("wall_post_user_image")
    @Expose
    private String wallPostUserImage;
    @SerializedName("users_wall_post_id")
    @Expose
    private String usersWallPostId;

    public Integer getUsersWallPostRating() {
        return usersWallPostRating;
    }

    public void setUsersWallPostRating(Integer usersWallPostRating) {
        this.usersWallPostRating = usersWallPostRating;
    }

    @SerializedName("wall_post_user_rating")
    @Expose
    private Integer usersWallPostRating;
    @SerializedName("from_user_id")
    @Expose
    private String fromUserId;
    @SerializedName("to_user_id")
    @Expose
    private String toUserId;
    @SerializedName("post_content")
    @Expose
    private String postContent;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("audio")
    @Expose
    private String audio;

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    @SerializedName("video_img")
    @Expose
    private String videoImg;

    @SerializedName("post_content_type")
    @Expose
    private String postContentType;
    @SerializedName("file_align")
    @Expose
    private String fileAlign;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("posting_date")
    @Expose
    private String postingDate;
    @SerializedName("like_count")
    @Expose
    private String likeCount;
    @SerializedName("liked_by_me")
    @Expose
    private String likedByMe;
    @SerializedName("comment_count")
    @Expose
    private String commentCount;
    @SerializedName("comment_list")
    @Expose
    private List<CommentList> commentList = null;

    protected Post(Parcel in) {
        wallPostUserName = in.readString();
        wallPostUserImage = in.readString();
        usersWallPostId = in.readString();
        fromUserId = in.readString();
        toUserId = in.readString();
        postContent = in.readString();
        image = in.readString();
        video = in.readString();
        audio = in.readString();
        postContentType = in.readString();
        fileAlign = in.readString();
        postType = in.readString();
        postingDate = in.readString();
        likeCount = in.readString();
        likedByMe = in.readString();
        commentCount = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getWallPostUserName() {
        return wallPostUserName;
    }

    public void setWallPostUserName(String wallPostUserName) {
        this.wallPostUserName = wallPostUserName;
    }

    public String getWallPostUserImage() {
        return wallPostUserImage;
    }

    public void setWallPostUserImage(String wallPostUserImage) {
        this.wallPostUserImage = wallPostUserImage;
    }

    public String getUsersWallPostId() {
        return usersWallPostId;
    }

    public void setUsersWallPostId(String usersWallPostId) {
        this.usersWallPostId = usersWallPostId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getPostContentType() {
        return postContentType;
    }

    public void setPostContentType(String postContentType) {
        this.postContentType = postContentType;
    }

    public String getFileAlign() {
        return fileAlign;
    }

    public void setFileAlign(String fileAlign) {
        this.fileAlign = fileAlign;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getLikedByMe() {
        return likedByMe;
    }

    public void setLikedByMe(String likedByMe) {
        this.likedByMe = likedByMe;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public List<CommentList> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentList> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wallPostUserName);
        dest.writeString(wallPostUserImage);
        dest.writeString(usersWallPostId);
        dest.writeString(fromUserId);
        dest.writeString(toUserId);
        dest.writeString(postContent);
        dest.writeString(image);
        dest.writeString(video);
        dest.writeString(audio);
        dest.writeString(postContentType);
        dest.writeString(fileAlign);
        dest.writeString(postType);
        dest.writeString(postingDate);
        dest.writeString(likeCount);
        dest.writeString(likedByMe);
        dest.writeString(commentCount);
    }
}