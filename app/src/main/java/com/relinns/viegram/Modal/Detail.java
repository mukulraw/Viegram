package com.relinns.viegram.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.relinns.viegram.Pojo.Post;

import java.util.List;

/**
 * Created by admin on 25-07-2017.
 */
public class Detail {


    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;

    @SerializedName("reference_id")
    @Expose
    private String referenceId;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("country_iso")
    @Expose
    private String countryIso;

    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;


    @SerializedName("scorepoint")
    @Expose
    private String scorepoint;


    @SerializedName("total_posts")
    @Expose
    private String totalPosts;
    @SerializedName("follower_status")
    @Expose
    private String followerStatus;
    @SerializedName("privacy_status")
    @Expose
    private String privacyStatus;
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;


    @SerializedName("bio_data")
    @Expose
    private String bioData;
    @SerializedName("link")
    @Expose
    private String link;



    public String getBioData() {
        return bioData;
    }

    public void setBioData(String bioData) {
        this.bioData = bioData;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getScorepoint() {
        return scorepoint;
    }

    public void setScorepoint(String scorepoint) {
        this.scorepoint = scorepoint;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(String totalPosts) {
        this.totalPosts = totalPosts;
    }

    public String getFollowerStatus() {
        return followerStatus;
    }

    public void setFollowerStatus(String followerStatus) {
        this.followerStatus = followerStatus;
    }

    public String getPrivacyStatus() {
        return privacyStatus;
    }

    public void setPrivacyStatus(String privacyStatus) {
        this.privacyStatus = privacyStatus;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }
}
