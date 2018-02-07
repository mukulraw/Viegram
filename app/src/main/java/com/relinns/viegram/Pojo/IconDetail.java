
package com.relinns.viegram.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IconDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("icons")
    @Expose
    private String icons;
    @SerializedName("detail")
    @Expose
    private String detail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
