
package com.relinns.viegram.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingDetails {

    @SerializedName("s_id")
    @Expose
    private String sId;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("privacy")
    @Expose
    private String privacy;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("repost")
    @Expose
    private String repost;
    @SerializedName("follow_request")
    @Expose
    private String followRequest;
    @SerializedName("gifts")
    @Expose
    private String gifts;

    public String getSId() {
        return sId;
    }

    public void setSId(String sId) {
        this.sId = sId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getRepost() {
        return repost;
    }

    public void setRepost(String repost) {
        this.repost = repost;
    }

    public String getFollowRequest() {
        return followRequest;
    }

    public void setFollowRequest(String followRequest) {
        this.followRequest = followRequest;
    }

    public String getGifts() {
        return gifts;
    }

    public void setGifts(String gifts) {
        this.gifts = gifts;
    }

}
