package com.api_l.forms.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;



public class GoalModel implements Serializable {
    @SerializedName("goalId")
    @Expose
    private Integer goalId;

    @SerializedName("Users_userid")
    @Expose
    private Integer Users_userid;
    @SerializedName("IsPublic")
    @Expose
    private boolean IsPublic;
    @SerializedName("goal1")
    @Expose
    private String goal1;
    @SerializedName("Domains_domainId")
    @Expose
    private Integer Domains_domainId;
    @SerializedName("goals_meta_data")
    @Expose
    private List<GoalsMetaDatum> goalsMetaData = null;

    @SerializedName("Forms_formId")
    @Expose
    private Integer formsFormId;

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

    public List<GoalsMetaDatum> getGoalsMetaData() {
        return goalsMetaData;
    }

    public void setGoalsMetaData(List<GoalsMetaDatum> goalsMetaData) {
        this.goalsMetaData = goalsMetaData;
    }

    public Integer getFormsFormId() {
        return formsFormId;
    }

    public void setFormsFormId(Integer formsFormId) {
        this.formsFormId = formsFormId;
    }

    public Integer getUsers_userid() {
        return Users_userid;
    }

    public void setUsers_userid(Integer users_userid) {
        Users_userid = users_userid;
    }

    public boolean isPublic() {
        return IsPublic;
    }

    public void setPublic(boolean aPublic) {
        IsPublic = aPublic;
    }
}
