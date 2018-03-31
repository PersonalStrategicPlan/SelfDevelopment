package com.api_l.forms.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GoalsMetaDatum implements Serializable{

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Goals_goal_id")
    @Expose
    private Integer goalsGoalId;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("value")
    @Expose
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoalsGoalId() {
        return goalsGoalId;
    }

    public void setGoalsGoalId(Integer goalsGoalId) {
        this.goalsGoalId = goalsGoalId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}