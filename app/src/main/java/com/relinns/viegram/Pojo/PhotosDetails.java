
package com.relinns.viegram.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotosDetails {

    @SerializedName("total_posts")
    @Expose
    private String totalPosts;
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;

    public String getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(String totalPosts) {
        this.totalPosts = totalPosts;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

}
