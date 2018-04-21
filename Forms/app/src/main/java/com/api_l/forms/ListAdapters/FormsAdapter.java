package com.api_l.forms.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.api_l.forms.DomainsActivity;
import com.api_l.forms.FormsActivity;
import com.api_l.forms.GoalsActivity;
import com.api_l.forms.Helper.Constants;
import com.api_l.forms.Models.DomainModel;
import com.api_l.forms.Models.DomainsIntentModel;
import com.api_l.forms.Models.FormModel;
import com.api_l.forms.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;



public class FormsAdapter extends ArrayAdapter<FormModel> implements View.OnClickListener {
    private ArrayList<FormModel> models = new ArrayList<FormModel>();
    private int UserId =1 ;
    private Activity activity;
    private  Context ctx;
    public FormsAdapter(@NonNull Context ctx, @NonNull ArrayList<FormModel> objects) {
        super(ctx,0, objects);
        this.models = objects;
        this.ctx = ctx;
    }
    public FormModel getItem(int p){
        return  models.get(p);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.generic_list_adapter, parent, false);
        }


            FormModel model = getItem(position);
        switch (model.getFormId()){
            case 1:
            convertView.setBackgroundColor(Constants.Form_1_Color);
                break;
            case 2:
                convertView.setBackgroundColor(Constants.Form_2_Color);
                break;
            case 3:
                convertView.setBackgroundColor(Constants.Form_3_Color);
                break;
            case 4:
                convertView.setBackgroundColor(Constants.Form_4_Color);
                break;
        }

        TextView btn = (TextView) convertView.findViewById(R.id.itemTitle);
        btn.setText(model.getFormTitle());

            btn.setTag(position);
            btn.setOnClickListener(this);


        return convertView;
    }

        @Override
    public void onClick(View v) {

                FormModel selectedItem = getItem((int)v.getTag());
                ArrayList<DomainModel> domainModels = selectedItem.getDomains();
                DomainsIntentModel intentModel = new DomainsIntentModel();
                intentModel.setDomainModels(domainModels);
                if(selectedItem !=null){


                        Intent domainsIntent = new Intent(ctx, DomainsActivity.class);
                        domainsIntent.putExtra("formId", selectedItem.getFormId());

                        ctx.startActivity(domainsIntent);
                    }

    }

    @NonNull
    private ArrayList<DomainModel> getDomainModels(FormModel selectedItem)
    {
        ArrayList<DomainModel> domainsForm = new ArrayList<DomainModel>();
        for (Object o : selectedItem.getDomains()){
            DomainModel domainModel = new DomainModel();
            domainModel = (DomainModel)o;
            domainModel.setDomainId(((DomainModel) o).getDomainId());
            domainsForm.add(domainModel);
        }
        return domainsForm;
    }
}
