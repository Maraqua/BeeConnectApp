package com.marty.beeconnect.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class IdeasModel {

    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("postUserId")
    @Expose
    private String postUserId;
    @SerializedName("post")
    @Expose
    private String post;
    @SerializedName("statusImage")
    @Expose
    private String statusImage;
    @SerializedName("statusTime")
    @Expose
    private String statusTime;
    @SerializedName("voteCount")
    @Expose
    private String voteCount;
    @SerializedName("commentCount")
    @Expose
    private String commentCount;
    @SerializedName("hasComment")
    @Expose
    private String hasComment;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profileUrl")
    @Expose
    private String profileUrl;
    @SerializedName("userToken")
    @Expose
    private String userToken;
    @SerializedName("isVoted")
    @Expose
    private boolean isVoted;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getStatusImage() {
        return statusImage;
    }

    public void setStatusImage(String statusImage) {
        this.statusImage = statusImage;
    }

    public String getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getHasComment() {
        return hasComment;
    }

    public void setHasComment(String hasComment) {
        this.hasComment = hasComment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public
    boolean isVoted () {
        return isVoted;
    }

    public
    void setVoted ( boolean voted ) {
        isVoted = voted;
    }
}