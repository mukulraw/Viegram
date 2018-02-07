package com.relinns.viegram.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 08-08-2017.
 */
public class LeaderData {

    @SerializedName("data_msg")
    @Expose
    private String dataMsg;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;

    public String getDataMsg() {
        return dataMsg;
    }

    public void setDataMsg(String dataMsg) {
        this.dataMsg = dataMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
}
