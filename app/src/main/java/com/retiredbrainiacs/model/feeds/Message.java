package com.retiredbrainiacs.model.feeds;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

@SerializedName("users_wall_post_id")
@Expose
private String usersWallPostId;
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

}