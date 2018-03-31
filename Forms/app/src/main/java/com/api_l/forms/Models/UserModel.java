package com.api_l.forms.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("userid")
    @Expose
    private Integer userid;
    @SerializedName("userFullName")
    @Expose
    private String userFullName;
    @SerializedName("UserAcadmicID")
    @Expose
    private String UserAcadmicID;
    @SerializedName("userPassword")
    @Expose
    private String userPassword;
    @SerializedName("Roles_rolid")
    @Expose
    private Integer rolesRolid;
    @SerializedName("answers")
    @Expose
    private List<Object> answers = null;
    @SerializedName("role")
    @Expose
    private Role role;

    public UserModel(String userAcadmicID, String userPassword){
        this.UserAcadmicID = userAcadmicID;
        this.userPassword = userPassword;
    }
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return UserAcadmicID;
    }

    public void setUserEmail(String userEmail) {
        this.UserAcadmicID = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getRolesRolid() {
        return rolesRolid;
    }

    public void setRolesRolid(Integer rolesRolid) {
        this.rolesRolid = rolesRolid;
    }

    public List<Object> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Object> answers) {
        this.answers = answers;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}

