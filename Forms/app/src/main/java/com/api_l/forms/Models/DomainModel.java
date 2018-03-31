package com.api_l.forms.Models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DomainModel implements Serializable {

    @SerializedName("domainId")
    @Expose
    private Integer domainId;
    @SerializedName("domainTitle")
    @Expose
    private String domainTitle;
    @SerializedName("Forms_formId")
    @Expose
    private Integer formsFormId;
    @SerializedName("form")
    @Expose
    private Object form;
    @SerializedName("goals")
    @Expose
    private List<Object> goals = null;

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public String getDomainTitle() {
        return domainTitle;
    }

    public void setDomainTitle(String domainTitle) {
        this.domainTitle = domainTitle;
    }

    public Integer getFormsFormId() {
        return formsFormId;
    }

    public void setFormsFormId(Integer formsFormId) {
        this.formsFormId = formsFormId;
    }

    public Object getForm() {
        return form;
    }

    public void setForm(Object form) {
        this.form = form;
    }

    public List<Object> getGoals() {
        return goals;
    }

    public void setGoals(List<Object> goals) {
        this.goals = goals;
    }


}
