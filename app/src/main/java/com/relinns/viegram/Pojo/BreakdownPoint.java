
package com.relinns.viegram.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BreakdownPoint {

    @SerializedName("earn_id")
    @Expose
    private String earnId;
    @SerializedName("earning_hints")
    @Expose
    private String earningHints;
    @SerializedName("points")
    @Expose
    private String points;

    public String getDisplayHints() {
        return displayHints;
    }

    public void setDisplayHints(String displayHints) {
        this.displayHints = displayHints;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @SerializedName("display_hints")
    @Expose
    private String displayHints;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getEarnId() {
        return earnId;
    }

    public void setEarnId(String earnId) {
        this.earnId = earnId;
    }

    public String getEarningHints() {
        return earningHints;
    }

    public void setEarningHints(String earningHints) {
        this.earningHints = earningHints;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}
