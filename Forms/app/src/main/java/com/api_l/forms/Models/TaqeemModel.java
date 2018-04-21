package com.api_l.forms.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class TaqeemModel implements Serializable {
    @SerializedName("SoHappyCount")
    @Expose
    private Integer SoHappyCount;
    @SerializedName("HappyCount")
    @Expose
    private Integer HappyCount;

    @SerializedName("AvaCount")
    @Expose
    private Integer AvaCount;

    @SerializedName("NotHappy")
    @Expose
    private Integer NotHappyCount;

    @SerializedName("Sad")
    @Expose
    private Integer SadCount;

    @SerializedName("Percentage")
    @Expose
    private Double Percentage;

    public Integer getSoHappyCount() {
        return SoHappyCount;
    }

    public void setSoHappyCount(Integer soHappyCount) {
        SoHappyCount = soHappyCount;
    }

    public Integer getHappyCount() {
        return HappyCount;
    }

    public void setHappyCount(Integer happyCount) {
        HappyCount = happyCount;
    }

    public Integer getAvaCount() {
        return AvaCount;
    }

    public void setAvaCount(Integer avaCount) {
        AvaCount = avaCount;
    }

    public Integer getNotHappyCount() {
        return NotHappyCount;
    }

    public void setNotHappyCount(Integer notHappyCount) {
        NotHappyCount = notHappyCount;
    }

    public Integer getSadCount() {
        return SadCount;
    }

    public void setSadCount(Integer sadCount) {
        SadCount = sadCount;
    }

    public Double getPercentage() {
        return Percentage;
    }

    public void setPercentage(Double percentage) {
        Percentage = percentage;
    }
}
