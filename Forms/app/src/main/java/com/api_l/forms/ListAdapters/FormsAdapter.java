package com.api_l.forms.ListAdapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.api_l.forms.Models.FormModel;
import com.api_l.forms.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


public class FormsAdapter extends ArrayAdapter<FormModel> implements View.OnClickListener {
    private ArrayList<FormModel> models = new ArrayList<FormModel>();
    private int UserId =1 ;
    private Context ctx;
    public FormsAdapter(@NonNull Context context, @NonNull List<FormModel> objects) {
        super(context,0, objects);
    }
    public FormModel getItem(int p){
        return  models.get(p);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.forms_adapter_layout, parent, false);
        }
        if(models.size()>0){
            FormModel model = getItem(position);
            Button btn = (Button) convertView.findViewById(R.id.formBtn);
        btn.setText(model.getFormTitle());}

        return convertView;
    }

        @Override
    public void onClick(View v) {

    }
}
