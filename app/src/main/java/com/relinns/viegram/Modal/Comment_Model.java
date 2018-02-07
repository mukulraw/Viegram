package com.relinns.viegram.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 25-07-2017.
 */

public class Comment_Model {

    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("postid")
    @Expose
    private String postid;

    @SerializedName("post_userid")

    @Expose
    private String postUserid;
    @SerializedName("comment_userid")
    @Expose
    private String commentUserid;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("total_likes")
    @Expose
    private String totalLikes;
    @SerializedName("like")
    @Expose
    private String like;
    @SerializedName("time_ago")
    @Expose
    private String timeAgo;


    @SerializedName("mention_people")
    @Expose
    private List<LikeComment> mentionPeople = null;


    public List<LikeComment> getMentionPeople() {
        return mentionPeople;
    }

    public void setMentionPeople(List<LikeComment> mentionPeople) {
        this.mentionPeople = mentionPeople;
    }

    public String getPostUserid() {
        return postUserid;
    }

    public void setPostUserid(String postUserid) {
        this.postUserid = postUserid;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getCommentUserid() {
        return commentUserid;
    }

    public void setCommentUserid(String commentUserid) {
        this.commentUserid = commentUserid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }



}
