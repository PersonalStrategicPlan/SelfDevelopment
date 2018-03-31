package com.api_l.forms.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormModel {

    @SerializedName("formId")
    @Expose
    private Integer formId;
    @SerializedName("formTitle")
    @Expose
    private String formTitle;
    @SerializedName("domains")
    @Expose
    private List<Object> domains = null;

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

    public List<Object> getDomains() {
        return domains;
    }

    public void setDomains(List<Object> domains) {
        this.domains = domains;
    }

}