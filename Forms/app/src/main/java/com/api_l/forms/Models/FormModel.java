package com.api_l.forms.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormModel implements Serializable{

    @SerializedName("formId")
    @Expose
    private Integer formId;
    @SerializedName("formTitle")
    @Expose
    private String formTitle;
    @SerializedName("domains")
    @Expose
    private ArrayList<DomainModel> domains = null;

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public ArrayList<DomainModel> getDomains() {
        return domains;
    }

    public void setDomains(ArrayList<DomainModel> domains) {
        this.domains = domains;
    }

}