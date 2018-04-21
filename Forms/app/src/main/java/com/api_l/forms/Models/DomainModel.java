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
    public Integer domainId;

    @SerializedName("Domains_domainId")
    @Expose
    private Integer Domains_domainId;
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
public  Integer getDomainID(){return  this.domainId;}
    public Integer getDomainId() {
        return Domains_domainId;
    }

    public void setDomainId(Integer Domains_domainId) {
        this.Domains_domainId = Domains_domainId;
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
