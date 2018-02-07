
package com.relinns.viegram.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.relinns.viegram.Modal.Detail;

public class ResultPojo {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("reason")
    @Expose
    private String reason;
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
    @SerializedName("bio_data")
    @Expose
    private String bioData;
    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("details")
    @Expose
    private Detail profile_details;

    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("reference_id")
    @Expose
    private String referenceId;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;
    @SerializedName("country_iso")
    @Expose
    private String countryIso;
    @SerializedName("score_points")
    @Expose
    private String scorePoints;
    @SerializedName("total_today_points")
    @Expose
    private String totalTodayPoints;
    @SerializedName("total_week_points")
    @Expose
    private String totalWeekPoints;
    @SerializedName("total_month_points")
    @Expose
    private String totalMonthPoints;
    @SerializedName("total_year_points")
    @Expose
    private String totalYearPoints;
    @SerializedName("total_overall_points")
    @Expose
    private String totalOverallPoints;
    @SerializedName("breakdown_points")
    @Expose
    private List<BreakdownPoint> breakdownPoints = null;
    @SerializedName("viegram_rank")
    @Expose
    private String viegramRank;
    @SerializedName("country_rank")
    @Expose
    private String countryRank;
    @SerializedName("follower_rank")
    @Expose
    private String followerRank;
    @SerializedName("following_rank")
    @Expose
    private String followingRank;
    @SerializedName("photos_details")
    @Expose
    private PhotosDetails photosDetails;
    @SerializedName("follower_ranking")
    @Expose
    private List<FollowerRanking> followerRanking = null;
    @SerializedName("setting_details")
    @Expose
    private SettingDetails settingDetails;
    @SerializedName("reference_link")
    @Expose
    private String referenceLink;
    @SerializedName("total_follower")
    @Expose
    private String totalFollowers;
    @SerializedName("total_following")
    @Expose
    private String totalFollowings;
    @SerializedName("icon_details")
    @Expose
    private List<IconDetail> iconDetails = null;
    @SerializedName("hints")
    @Expose
    private List<Hint> hints = null;
    @SerializedName("follower_list")
    @Expose
    private List<FollowerList> followerList = null;
    @SerializedName("following_list")
    @Expose
    private List<FollowingList> followingList = null;

    public Detail getProfile_details() {
        return profile_details;
    }

    public void setProfile_details(Detail profile_details) {
        this.profile_details = profile_details;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
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

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    public String getScorePoints() {
        return scorePoints;
    }

    public void setScorePoints(String scorePoints) {
        this.scorePoints = scorePoints;
    }

    public String getTotalTodayPoints() {
        return totalTodayPoints;
    }

    public void setTotalTodayPoints(String totalTodayPoints) {
        this.totalTodayPoints = totalTodayPoints;
    }

    public String getTotalWeekPoints() {
        return totalWeekPoints;
    }

    public void setTotalWeekPoints(String totalWeekPoints) {
        this.totalWeekPoints = totalWeekPoints;
    }

    public String getTotalMonthPoints() {
        return totalMonthPoints;
    }

    public void setTotalMonthPoints(String totalMonthPoints) {
        this.totalMonthPoints = totalMonthPoints;
    }

    public String getTotalYearPoints() {
        return totalYearPoints;
    }

    public void setTotalYearPoints(String totalYearPoints) {
        this.totalYearPoints = totalYearPoints;
    }

    public String getTotalOverallPoints() {
        return totalOverallPoints;
    }

    public void setTotalOverallPoints(String totalOverallPoints) {
        this.totalOverallPoints = totalOverallPoints;
    }

    public List<BreakdownPoint> getBreakdownPoints() {
        return breakdownPoints;
    }

    public void setBreakdownPoints(List<BreakdownPoint> breakdownPoints) {
        this.breakdownPoints = breakdownPoints;
    }

    public String getViegramRank() {
        return viegramRank;
    }

    public void setViegramRank(String viegramRank) {
        this.viegramRank = viegramRank;
    }

    public String getCountryRank() {
        return countryRank;
    }

    public void setCountryRank(String countryRank) {
        this.countryRank = countryRank;
    }

    public String getFollowerRank() {
        return followerRank;
    }

    public void setFollowerRank(String followerRank) {
        this.followerRank = followerRank;
    }

    public String getFollowingRank() {
        return followingRank;
    }

    public void setFollowingRank(String followingRank) {
        this.followingRank = followingRank;
    }

    public PhotosDetails getPhotosDetails() {
        return photosDetails;
    }

    public void setPhotosDetails(PhotosDetails photosDetails) {
        this.photosDetails = photosDetails;
    }

    public List<FollowerRanking> getFollowerRanking() {
        return followerRanking;
    }

    public void setFollowerRanking(List<FollowerRanking> followerRanking) {
        this.followerRanking = followerRanking;
    }

    public SettingDetails getSettingDetails() {
        return settingDetails;
    }

    public void setSettingDetails(SettingDetails settingDetails) {
        this.settingDetails = settingDetails;
    }

    public String getReferenceLink() {
        return referenceLink;
    }

    public void setReferenceLink(String referenceLink) {
        this.referenceLink = referenceLink;
    }

    public String getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(String totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public String getTotalFollowings() {
        return totalFollowings;
    }

    public void setTotalFollowings(String totalFollowings) {
        this.totalFollowings = totalFollowings;
    }

    public List<IconDetail> getIconDetails() {
        return iconDetails;
    }

    public void setIconDetails(List<IconDetail> iconDetails) {
        this.iconDetails = iconDetails;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public void setHints(List<Hint> hints) {
        this.hints = hints;
    }

    public List<FollowerList> getFollowerList() {
        return followerList;
    }

    public void setFollowerList(List<FollowerList> followerList) {
        this.followerList = followerList;
    }

    public List<FollowingList> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<FollowingList> followingList) {
        this.followingList = followingList;
    }

}
