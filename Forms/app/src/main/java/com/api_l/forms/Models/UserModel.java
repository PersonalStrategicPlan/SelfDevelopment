package com.api_l.forms.Models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel  implements Serializable{

    @SerializedName("userid")
    @Expose
    private Integer userid;
    @SerializedName("userFullName")
    @Expose
    private String userFullName;
    @SerializedName("UserAcadmicID")
    @Expose
    private String UserAcadmicID;
    @SerializedName("userSpecilaist")
    @Expose
    private String userSpecilaist;
    @SerializedName("userExp")
    @Expose
    private String userExp;
    @SerializedName("userEmpName")
    @Expose
    private String userEmpName;
    @SerializedName("userMobile")
    @Expose
    private String mobile;
    @SerializedName("userQF")
    @Expose
    private String qf;
    @SerializedName("userEmail")
    @Expose
    private String email;

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

    public UserModel(){}
    public UserModel(String userAcadmicID, String userPassword){
        this.setUserAcadmicID(userAcadmicID);
        this.setUserPassword(userPassword);
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
        return getUserAcadmicID();
    }

    public void setUserEmail(String userEmail) {
        this.setUserAcadmicID(userEmail);
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

    public String getUserAcadmicID() {
        return UserAcadmicID;
    }

    public void setUserAcadmicID(String userAcadmicID) {
        UserAcadmicID = userAcadmicID;
    }

    public String getSp() {
        return userSpecilaist;
    }

    public void setSp(String userSpecilaist) {
        this.userSpecilaist = userSpecilaist;
    }

    public String getExp() {
        return userExp;
    }

    public void setuserExp(String userExp) {
        this.userExp = userExp;
    }

    public String getEmpName() {
        return userEmpName;
    }

    public void setEmpName(String userEmpName) {
        this.userEmpName = userEmpName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQf() {
        return qf;
    }

    public void setQf(String qf) {
        this.qf = qf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

