package com.relinns.viegram.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 24-07-2017.
 */
public class TimelinePost {

    @SerializedName("post_type")
    @Expose
    private String postType;

    @SerializedName("type")
    @Expose
    private String randomtype;
    @SerializedName("repost_id")
    @Expose
    private String repostId;
    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("post_points")
    @Expose
    private String postPoints;

    @SerializedName("points")
    @Expose
    private String Points;

    @SerializedName("post_like")
    @Expose
    private String postLike;
    @SerializedName("userid")
    @Expose
    private String userid;

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("second_userid")
    @Expose
    private String secondUserid;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;

    @SerializedName("total_likes")
    @Expose
    private String total_likes;

    @SerializedName("total_comments")
    @Expose
    private String total_comments;

    @SerializedName("total_points")
    @Expose
    private String total_points;

    @SerializedName("photo")
    @Expose
    private String photo;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("caption")
    @Expose
    private String caption;

    @SerializedName("first_post_id")
    @Expose
    private String first_post_id;

    @SerializedName("first_caption")
    @Expose
    private String first_caption;

    @SerializedName("first_display_name")
    @Expose
    private String first_display_name;

    @SerializedName("tag_people")
    @Expose
    private List<TagPerson> tagPeople = null;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("first_user_id")
    @Expose
    private String first_user_id;

    @SerializedName("first_user_profile_image")
    @Expose
    private String firstUserProfileImage;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("image_width")
    @Expose
    private String imageWidth;
    @SerializedName("image_height")
    @Expose
    private String imageHeight;
    @SerializedName("time_ago")
    @Expose
    private String timeAgo;

    @SerializedName("video")
    @Expose
    private String video;


    public TimelinePost(String video,String postType, String repostId, String postId, String postPoints, String postLike, String userid, String secondUserid, String displayName, String profileImage, String photo, String fileType, String caption, String first_display_name, List<TagPerson> tagPeople, String location,  String imageWidth, String imageHeight, String timeAgo) {
        this.postType = postType;
        this.video=video;
        this.repostId = repostId;
        this.postId = postId;
        this.postPoints = postPoints;
        this.postLike = postLike;
        this.userid = userid;
        this.secondUserid = secondUserid;
        this.displayName = displayName;
        this.profileImage = profileImage;
        this.photo = photo;
        this.fileType = fileType;
        this.caption = caption;
        this.first_display_name = first_display_name;
        this.tagPeople = tagPeople;
        this.location = location;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.timeAgo = timeAgo;
    }

    public String getRandomtype() {
        return randomtype;
    }

    public void setRandomtype(String randomtype) {
        this.randomtype = randomtype;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(String total_likes) {
        this.total_likes = total_likes;
    }

    public String getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(String total_comments) {
        this.total_comments = total_comments;
    }

    public String getTotal_points() {
        return total_points;
    }

    public void setTotal_points(String total_points) {
        this.total_points = total_points;
    }

    public String getFirst_post_id() {
        return first_post_id;
    }

    public void setFirst_post_id(String first_post_id) {
        this.first_post_id = first_post_id;
    }

    public String getFirst_caption() {
        return first_caption;
    }

    public void setFirst_caption(String first_caption) {
        this.first_caption = first_caption;
    }

    public String getFirst_display_name() {
        return first_display_name;
    }

    public void setFirst_display_name(String first_display_name) {
        this.first_display_name = first_display_name;
    }

    public String getPoints() {
        return Points;
    }

    public void setPoints(String points) {
        Points = points;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getRepostId() {
        return repostId;
    }

    public void setRepostId(String repostId) {
        this.repostId = repostId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostPoints() {
        return postPoints;
    }

    public void setPostPoints(String postPoints) {
        this.postPoints = postPoints;
    }

    public String getPostLike() {
        return postLike;
    }

    public void setPostLike(String postLike) {
        this.postLike = postLike;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSecondUserid() {
        return secondUserid;
    }

    public void setSecondUserid(String secondUserid) {
        this.secondUserid = secondUserid;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<TagPerson> getTagPeople() {
        return tagPeople;
    }

    public void setTagPeople(List<TagPerson> tagPeople) {
        this.tagPeople = tagPeople;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public String getFirstUserProfileImage() {
        return firstUserProfileImage;
    }

    public void setFirstUserProfileImage(String firstUserProfileImage) {
        this.firstUserProfileImage = firstUserProfileImage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getFirst_user_id() {
        return first_user_id;
    }

    public void setFirst_user_id(String first_user_id) {
        this.first_user_id = first_user_id;
    }
}
