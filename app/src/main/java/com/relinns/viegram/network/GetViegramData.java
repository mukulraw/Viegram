package com.relinns.viegram.network;

import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Pojo.UserData;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/*
 * Created by admin on 24-07-2017.
 */

public interface GetViegramData {

    //for email and username verify purpose
//    @Headers("Content-Type:application/json")
    @POST("verifyobject.php")
    Call<API_Response> getVerifyData(@Body Map<String, String> body);

    //signup, signin, forgotPassword, changePassword, otp verification,updatepassword, logout
//    @Headers("Content-Type:application/json")
    @POST("password_object.php")
    Call<API_Response> accountWork(@Body Map<String, String> body);

    //get Earning hints,followerRanking,
    // view AnotherProfile,entering viegram, icon guide, follower_following_total, update token
//    @Headers("Content-Type:application/json")
    @POST("hints_object.php")
    Call<API_Response> pointsWork(@Body Map<String, String> body);

    //get Earning hints
//    @Headers("Content-Type:application/json")
    @POST("hints_object.php")
    Call<UserData> Earnings(@Body Map<String, String> body);

    //get All data
//    @Headers("Content-Type:application/json")
    @POST("alldata_object.php")
    Call<UserData> getAllData(@Body Map<String, String> body);

//get Follower and get Following
//@Headers("Content-Type:application/json")
@POST("random_object.php")
    Call<UserData> getFollower(@Body Map<String, String> body);

    //fetchProfile
//    @Headers("Content-Type:application/json")
    @POST("upload_object.php")
    Call<API_Response> getProfile(@Body Map<String, String> body);

    //fetchProfile
//    @Headers("Content-Type:application/json")
    @POST("upload_object.php")
    Call<UserData> FetchProfile(@Body Map<String, String> body);

    //overall_stats,overall ranking,userRanking,breakdownStats,addSettings,getSettings,total points
//    @Headers("Content-Type:application/json")
    @POST("ranking_object.php")
    Call<API_Response> rankingRelated(@Body Map<String, String> body);

    //get Settings, get follower ranking
//    @Headers("Content-Type:application/json")
    @POST("ranking_object.php")
    Call<UserData> Settings(@Body Map<String, String> body);

    //likepost, comment,repost,fetch_comments,likecomment
//    @Headers("Content-Type:application/json")
    @POST("post_object.php")
    Call<API_Response> getPostActionResponse(@Body Map<String, String> body);

    //getFollowers,restriction,getfollowing,unfollow,random posts,delete_comment,delete post
    //searchLocation,requestFriend,open photo,fetch another_userProfile,another Follower/Following, all users
    //post details, commentliked list, respondRequest
//    @Headers("Content-Type:application/json")
    @POST("random_object.php")
    Call<API_Response> FriendsWork(@Body Map<String, String> body);

    //fetchTimeline, notification
//    @Headers("Content-Type:application/json")
    @POST("timeline_object.php")
    Call<API_Response> getTimeline(@Body Map<String, String> body);

//    @Headers("Content-Type:application/json")
    @Multipart
    @POST("upload_object.php")
    Call<API_Response> UploadVideo(@Part MultipartBody.Part filename, @Part MultipartBody.Part thumbnail, @Part("action") RequestBody action, @Part("userid") RequestBody userid, @Part("caption") RequestBody caption, @Part("file_type") RequestBody file_type,
                                   @Part("restricted_status") RequestBody restricted_status, @Part("tag_people") RequestBody tag_people, @Part("location") RequestBody location, @Part("x_cordinates") RequestBody x_cordinates, @Part("y_cordinates") RequestBody y_cordinates);


//    @Headers("Content-Type:application/json")
    @Multipart
    @POST("upload_object.php")
    Call<API_Response> UploadImage(@Part MultipartBody.Part filename, @Part("action") RequestBody action, @Part("userid") RequestBody userid, @Part("caption") RequestBody caption, @Part("file_type") RequestBody file_type,
                                   @Part("restricted_status") RequestBody restricted_status, @Part("tag_people") RequestBody tag_people, @Part("location") RequestBody location, @Part("x_cordinates") RequestBody x_cordinates, @Part("y_cordinates") RequestBody y_cordinates);


//    @Headers("Content-Type:application/json")
    @Multipart
    @POST("upload_object.php")
    Call<API_Response> EditProfile(@Part MultipartBody.Part profileimage, @Part MultipartBody.Part coverimage, @Part("action") RequestBody action, @Part("userid") RequestBody userid,
                                   @Part("full_name") RequestBody full_name, @Part("bio_data") RequestBody bio_data,
                                   @Part("link") RequestBody link, @Part("display_name") RequestBody display_name);

//    @Headers("Content-Type:application/json")
    @Multipart
    @POST("upload_object.php")
    Call<API_Response> updateprofilesingle(@Part MultipartBody.Part image, @Part("action") RequestBody action, @Part("userid") RequestBody userid, @Part("full_name") RequestBody full_name,
                                           @Part("bio_data") RequestBody bio_data, @Part("link") RequestBody link, @Part("display_name") RequestBody display_name);

//    @Headers("Content-Type:application/json")
    @Multipart
    @POST("upload_object.php")
    Call<API_Response> updateprofile(@Part("action") RequestBody action, @Part("userid") RequestBody userid, @Part("full_name") RequestBody full_name,
                                     @Part("bio_data") RequestBody bio_data, @Part("link") RequestBody link, @Part("display_name") RequestBody display_name);

//
//    //for email and username verify purpose
//    @POST("object.php")
//    Call<API_Response> getVerifyData(@Body Map<String, String> body);
//
//    //signup, signin, forgotPassword, changePassword, otp verification,updatepassword, logout
//    @POST("object.php")
//    Call<API_Response> accountWork(@Body Map<String, String> body);
//
//    //get Earning hints,followerRanking,
//    // view AnotherProfile,entering viegram, icon guide, follower_following_total, update token
//    @POST("object.php")
//    Call<API_Response> pointsWork(@Body Map<String, String> body);
//
//    //get Earning hints,
//    @POST("object.php")
//    Call<UserData> Earnings(@Body Map<String, String> body);
//
//    //get All data
//    @POST("object.php")
//    Call<UserData> getAllData(@Body Map<String, String> body);
//
//    //get Follower and get Following
//    @POST("object.php")
//    Call<UserData> getFollower(@Body Map<String, String> body);
//
//    //fetchProfile
//    @POST("object.php")
//    Call<API_Response> getProfile(@Body Map<String, String> body);
//
//    //fetchProfile
//    @POST("object.php")
//    Call<UserData> FetchProfile(@Body Map<String, String> body);
//
//    //overall_stats,overall ranking,userRanking,breakdownStats,addSettings,getSettings,total points
//    @POST("object.php")
//    Call<API_Response> rankingRelated(@Body Map<String, String> body);
//
//    //get Settings, get follower ranking
//    @POST("object.php")
//    Call<UserData> Settings(@Body Map<String, String> body);
//
//    //likepost, comment,repost,fetch_comments,likecomment
//    @POST("object.php")
//    Call<API_Response> getPostActionResponse(@Body Map<String, String> body);
//
//    //getFollowers,restriction,getfollowing,unfollow,random posts,delete_comment,delete post
//    //searchLocation,requestFriend,open photo,fetch another_userProfile,another Follower/Following, all users
//    //post details, commentliked list, respondRequest
//    @POST("object.php")
//    Call<API_Response> FriendsWork(@Body Map<String, String> body);
//
//    //fetchTimeline, notification
//    @POST("object.php")
//    Call<API_Response> getTimeline(@Body Map<String, String> body);
//
//
//    @Multipart
//    @POST("object.php")
//    Call<API_Response> UploadVideo(@Part MultipartBody.Part filename, @Part MultipartBody.Part thumbnail, @Part("action") RequestBody action, @Part("userid") RequestBody userid, @Part("caption") RequestBody caption, @Part("file_type") RequestBody file_type,
//                                   @Part("restricted_status") RequestBody restricted_status, @Part("tag_people") RequestBody tag_people, @Part("location") RequestBody location, @Part("x_cordinates") RequestBody x_cordinates, @Part("y_cordinates") RequestBody y_cordinates);
//
//
//    @Multipart
//    @POST("object.php")
//    Call<API_Response> UploadImage(@Part MultipartBody.Part filename, @Part("action") RequestBody action, @Part("userid") RequestBody userid, @Part("caption") RequestBody caption, @Part("file_type") RequestBody file_type,
//                                   @Part("restricted_status") RequestBody restricted_status, @Part("tag_people") RequestBody tag_people, @Part("location") RequestBody location, @Part("x_cordinates") RequestBody x_cordinates, @Part("y_cordinates") RequestBody y_cordinates);
//
//
//    @Multipart
//    @POST("object.php")
//    Call<API_Response> EditProfile(@Part MultipartBody.Part profileimage, @Part MultipartBody.Part coverimage, @Part("action") RequestBody action, @Part("userid") RequestBody userid,
//                                   @Part("full_name") RequestBody full_name, @Part("bio_data") RequestBody bio_data,
//                                   @Part("link") RequestBody link, @Part("display_name") RequestBody display_name);
//
//    @Multipart
//    @POST("object.php")
//    Call<API_Response> updateprofilesingle(@Part MultipartBody.Part image, @Part("action") RequestBody action, @Part("userid") RequestBody userid, @Part("full_name") RequestBody full_name,
//                                           @Part("bio_data") RequestBody bio_data, @Part("link") RequestBody link, @Part("display_name") RequestBody display_name);
//
//    @Multipart
//    @POST("object.php")
//    Call<API_Response> updateprofile(@Part("action") RequestBody action, @Part("userid") RequestBody userid, @Part("full_name") RequestBody full_name,
//                                     @Part("bio_data") RequestBody bio_data, @Part("link") RequestBody link, @Part("display_name") RequestBody display_name);

}
