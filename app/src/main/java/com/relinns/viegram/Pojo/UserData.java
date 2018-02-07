
package com.relinns.viegram.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("result")
    @Expose
    private ResultPojo result;

    public ResultPojo getResult() {
        return result;
    }

    public void setResult(ResultPojo result) {
        this.result = result;
    }

}
