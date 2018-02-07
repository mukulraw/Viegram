
package com.relinns.viegram.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowerList {

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
    @SerializedName("restrict_status")
    @Expose
    private String restrictStatus;

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

    public String getRestrictStatus() {
        return restrictStatus;
    }

    public void setRestrictStatus(String restrictStatus) {
        this.restrictStatus = restrictStatus;
    }

}
