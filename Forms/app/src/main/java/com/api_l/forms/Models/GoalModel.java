package com.api_l.forms.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class GoalModel implements Serializable {
    @SerializedName("goalId")
    @Expose
    private Integer goalId;
    @SerializedName("goal1")
    @Expose
    private String goal1;
    @SerializedName("Domains_domainId")
    @Expose
    private Integer Domains_domainId;

    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
    }

    public String setGoal1() {
        return getGoal1();
    }

    public void setDomains_domainId(Integer domains_domainId) {
        this.Domains_domainId = domains_domainId;
    }

    public Integer getDomains_domainId() {
        return Domains_domainId;
    }

    public String getGoal1() {
        return goal1;
    }

    public void setGoal1(String goal1) {
        this.goal1 = goal1;
    }
}
