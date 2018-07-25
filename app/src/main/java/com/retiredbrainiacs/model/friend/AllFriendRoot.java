package com.retiredbrainiacs.model.friend;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllFriendRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("list_all")
@Expose
private List<ListAll> listAll = null;

    @SerializedName("list_friends")
    @Expose
    private List<ListFriend> listFriends = null;
    @SerializedName("comment_count")
    @Expose
    private List<CommentCount> commentCount = null;


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

public List<ListAll> getListAll() {
return listAll;
}

public void setListAll(List<ListAll> listAll) {
this.listAll = listAll;
}

    public List<ListFriend> getListFriends() {
        return listFriends;
    }

    public void setListFriends(List<ListFriend> listFriends) {
        this.listFriends = listFriends;
    }

    public List<CommentCount> getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(List<CommentCount> commentCount) {
        this.commentCount = commentCount;
    }


}