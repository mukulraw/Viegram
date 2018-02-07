package com.relinns.viegram.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 04-08-2017.
 */
public class LikeComment {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;



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
