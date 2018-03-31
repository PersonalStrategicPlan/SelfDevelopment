package com.api_l.forms.Models;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Role {

    @SerializedName("rolid")
    @Expose
    private Integer rolid;
    @SerializedName("roletitle")
    @Expose
    private String roletitle;
    @SerializedName("users")
    @Expose
    private List<Object> users = null;

    public Integer getRolid() {
        return rolid;
    }

    public void setRolid(Integer rolid) {
        this.rolid = rolid;
    }

    public String getRoletitle() {
        return roletitle;
    }

    public void setRoletitle(String roletitle) {
        this.roletitle = roletitle;
    }

    public List<Object> getUsers() {
        return users;
    }

    public void setUsers(List<Object> users) {
        this.users = users;
    }

}