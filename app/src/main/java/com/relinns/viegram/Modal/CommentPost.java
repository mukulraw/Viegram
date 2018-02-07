package com.relinns.viegram.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 04-08-2017.
 */
public class CommentPost {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("user_id")
    @Expose
    private String userid;

    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;

    public CommentPost(String userid, String displayName, String profileImage) {
        this.userid = userid;
        this.displayName = displayName;
        this.profileImage = profileImage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
