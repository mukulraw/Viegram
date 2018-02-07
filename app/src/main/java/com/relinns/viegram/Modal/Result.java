package com.relinns.viegram.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 24-07-2017.
 */

public class Result {


    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("total_records")
    @Expose
    private String totalRecords;

    @SerializedName("total_post_points")
    @Expose
    private String total_post_points;

    @SerializedName("total_posts")
    @Expose
    private String total_posts;


    @SerializedName("detail")
    @Expose
    private Detail detail;
    @SerializedName("details")
    @Expose
    private Detail profile_details;

    @SerializedName("total_followers")
    @Expose
    private String totalFollowers;
    @SerializedName("total_following")
    @Expose
    private String totalFollowing;
    @SerializedName("follower_list")
    @Expose
    private List<FollowerList_Model> followerList = null;

    @SerializedName("following_list")
    @Expose
    private List<FollowerList_Model> followingList = null;
    @SerializedName("timeline_posts")
    @Expose
    private List<TimelinePost> timelinePosts = null;

    @SerializedName("comments")
    @Expose
    private List<Comment_Model> comments = null;

    @SerializedName("total_follower")
    @Expose
    private String totalFollower;

    @SerializedName("total_comment_points")
    @Expose
    private String total_comment_points;

    @SerializedName("notification")
    @Expose
    private List<Notification> notification = null;

    @SerializedName("total_followings")
    @Expose
    private String totalFollowings;


    @SerializedName("like_counter")
    @Expose
    private String likeCounter;
    @SerializedName("like_comment")
    @Expose
    private List<LikeComment> likeComment = null;

    @SerializedName("comment_counter")
    @Expose
    private String commentCounter;
    @SerializedName("repost_counter")
    @Expose
    private String repostCounter;
    @SerializedName("mycomment_counter")
    @Expose
    private String mycommentCounter;
    @SerializedName("like_posts")
    @Expose
    private List<LikeComment> likePosts = null;
    @SerializedName("comment_post")
    @Expose
    private List<CommentPost> commentPost = null;
    @SerializedName("repost_post")
    @Expose
    private List<RepostPost> repostPost = null;
    @SerializedName("my_comments")
    @Expose
    private List<CommentLiked> myComments = null;

    @SerializedName("user_details")
    @Expose
    private List<CommentPost> userDetails = null;


    @SerializedName("places_detail")
    @Expose
    private List<PlacesDetail> placesDetail = null;


    @SerializedName("setting_details")
    @Expose
    private SettingDetails settingDetails;
    @SerializedName("reference_link")
    @Expose
    private String referenceLink;

    @SerializedName("points")
    @Expose
    private List<Point_Model> points = null;


    @SerializedName("hints")
    @Expose
    private List<Point_Model> hints = null;

    public List<Point_Model> getHints() {
        return hints;
    }

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


    @SerializedName("score_points")
    @Expose
    private String score_points;

    @SerializedName("total_score")
    @Expose
    private String total_score;

    @SerializedName("icon_details")
    @Expose
    private List<IconDetail> iconDetails = null;


    @SerializedName("follower_details")
    @Expose
    private List<FollowerDetail> followerDetails = null;


    @SerializedName("ranking_details")
    @Expose
    private List<LeaderData> rankingDetails = null;

    public String getTotal_posts() {
        return total_posts;
    }

    public void setTotal_posts(String total_posts) {
        this.total_posts = total_posts;
    }

    public List<FollowerList_Model> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<FollowerList_Model> followingList) {
        this.followingList = followingList;
    }

    public List<LeaderData> getRankingDetails() {
        return rankingDetails;
    }

    public void setRankingDetails(List<LeaderData> rankingDetails) {
        this.rankingDetails = rankingDetails;
    }

    public List<FollowerDetail> getFollowerDetails() {
        return followerDetails;
    }

    public void setFollowerDetails(List<FollowerDetail> followerDetails) {
        this.followerDetails = followerDetails;
    }

    public List<IconDetail> getIconDetails() {
        return iconDetails;
    }

    public String getScore_points() {
        return score_points;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public void setScore_points(String score_points) {
        this.score_points = score_points;
    }

    public void setIconDetails(List<IconDetail> iconDetails) {
        this.iconDetails = iconDetails;
    }

    public String getViegramRank() {
        return viegramRank;
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

    public void setHints(List<Point_Model> hints) {
        this.hints = hints;
    }

    public List<Point_Model> getPoints() {
        return points;
    }

    public void setPoints(List<Point_Model> points) {
        this.points = points;
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

    public String getUserId() {
        return userId;
    }

    public List<PlacesDetail> getPlacesDetail() {
        return placesDetail;
    }

    public void setPlacesDetail(List<PlacesDetail> placesDetail) {
        this.placesDetail = placesDetail;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CommentPost> getUserDetails() {
        return userDetails;
    }

    public String getTotal_comment_points() {
        return total_comment_points;
    }

    public void setTotal_comment_points(String total_comment_points) {
        this.total_comment_points = total_comment_points;
    }

    public void setUserDetails(List<CommentPost> userDetails) {
        this.userDetails = userDetails;
    }

    public String getTotalFollower() {
        return totalFollower;
    }

    public void setTotalFollower(String totalFollower) {
        this.totalFollower = totalFollower;
    }

    public List<CommentPost> getCommentPost() {
        return commentPost;
    }

    public void setCommentPost(List<CommentPost> commentPost) {
        this.commentPost = commentPost;
    }

    public List<RepostPost> getRepostPost() {
        return repostPost;
    }

    public void setRepostPost(List<RepostPost> repostPost) {
        this.repostPost = repostPost;
    }

    public List<CommentLiked> getMyComments() {
        return myComments;
    }

    public void setMyComments(List<CommentLiked> myComments) {
        this.myComments = myComments;
    }

    public String getCommentCounter() {
        return commentCounter;
    }

    public void setCommentCounter(String commentCounter) {
        this.commentCounter = commentCounter;
    }

    public String getRepostCounter() {
        return repostCounter;
    }

    public void setRepostCounter(String repostCounter) {
        this.repostCounter = repostCounter;
    }

    public String getMycommentCounter() {
        return mycommentCounter;
    }

    public void setMycommentCounter(String mycommentCounter) {
        this.mycommentCounter = mycommentCounter;
    }

    public List<LikeComment> getLikePosts() {
        return likePosts;
    }

    public void setLikePosts(List<LikeComment> likePosts) {
        this.likePosts = likePosts;
    }



    public List<LikeComment> getLikeComment() {
        return likeComment;
    }

    public void setLikeComment(List<LikeComment> likeComment) {
        this.likeComment = likeComment;
    }

    public String getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(String likeCounter) {
        this.likeCounter = likeCounter;
    }

    public String getTotalFollowings() {
        return totalFollowings;
    }

    public void setTotalFollowings(String totalFollowings) {
        this.totalFollowings = totalFollowings;
    }

    public Detail getProfile_details() {
        return profile_details;
    }

    public void setProfile_details(Detail profile_details) {
        this.profile_details = profile_details;
    }

    public List<Notification> getNotification() {
        return notification;
    }

    public void setNotification(List<Notification> notification) {
        this.notification = notification;
    }

    public String getTotalFollowers() {
        return totalFollowers;
    }

    public Detail getDetails() {
        return profile_details;
    }

    public void setDetails(Detail details) {
        this.profile_details = details;
    }

    public List<Comment_Model> getComments() {
        return comments;
    }

    public void setComments(List<Comment_Model> comments) {
        this.comments = comments;
    }

    public void setTotalFollowers(String totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public String getTotalFollowing() {
        return totalFollowing;
    }

    public void setTotalFollowing(String totalFollowing) {
        this.totalFollowing = totalFollowing;
    }

    public List<FollowerList_Model> getFollowerList() {
        return followerList;
    }

    public void setFollowerList(List<FollowerList_Model> followerList) {
        this.followerList = followerList;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
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

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<TimelinePost> getTimelinePosts() {
        return timelinePosts;
    }

    public void setTimelinePosts(List<TimelinePost> timelinePosts) {
        this.timelinePosts = timelinePosts;
    }

    public String getTotal_post_points() {
        return total_post_points;
    }

    public void setTotal_post_points(String total_post_points) {
        this.total_post_points = total_post_points;
    }
}
