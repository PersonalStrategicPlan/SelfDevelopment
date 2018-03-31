package com.api_l.forms.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class AnswerModel implements Serializable {
    @SerializedName("ansId")
    @Expose
    private Integer ansId;
    @SerializedName("Users_userid")
    @Expose
    private Integer Users_userid;
    @SerializedName("Goals_goalId")
    @Expose
    private Integer Goals_goalId;
    @SerializedName("color")
    @Expose
    private Integer color;

    public Integer getAnsId() {
        return ansId;
    }

    public void setAnsId(Integer ansId) {
        this.ansId = ansId;
    }

    public Integer getUsers_userid() {
        return Users_userid;
    }

    public void setUsers_userid(Integer users_userid) {
        Users_userid = users_userid;
    }

    public Integer getGoals_goalId() {
        return Goals_goalId;
    }

    public void setGoals_goalId(Integer goals_goalId) {
        Goals_goalId = goals_goalId;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
