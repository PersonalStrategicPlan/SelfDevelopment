package com.api_l.forms.Models;

import java.io.Serializable;
import java.util.ArrayList;


public class DomainsIntentModel implements Serializable{
    private ArrayList<DomainModel> domainModels;

    public ArrayList<DomainModel> getDomainModels() {
        return domainModels;
    }

    public void setDomainModels(ArrayList<DomainModel> domainModels) {
        this.domainModels = domainModels;
    }
}
