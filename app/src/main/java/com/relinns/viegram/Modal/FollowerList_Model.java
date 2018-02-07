package com.relinns.viegram.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 25-07-2017.
 */

public class FollowerList_Model {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("follow_status")
    @Expose
    private String followStatus;


    @SerializedName("user_follow_status")
    @Expose
    private String userFollowStatus;


    public String getUserFollowStatus() {
        return userFollowStatus;
    }

    public void setUserFollowStatus(String userFollowStatus) {
        this.userFollowStatus = userFollowStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(String followStatus) {
        this.followStatus = followStatus;
    }

}
