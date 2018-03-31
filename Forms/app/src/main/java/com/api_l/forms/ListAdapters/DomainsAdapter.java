package com.api_l.forms.ListAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.api_l.forms.DomainsActivity;
import com.api_l.forms.GoalsActivity;
import com.api_l.forms.Models.DomainModel;
import com.api_l.forms.Models.DomainsIntentModel;
import com.api_l.forms.Models.FormModel;
import com.api_l.forms.R;

import java.util.ArrayList;



public class DomainsAdapter extends ArrayAdapter<DomainModel> implements View.OnClickListener{


        private ArrayList<DomainModel> domains = new ArrayList<DomainModel>();
        private int UserId =1 ;
        private Context ctx;
        public DomainsAdapter(@NonNull Context context, @NonNull ArrayList<DomainModel> objects) {
            super(context,0, objects);
            this.domains = objects;
            ctx = context;
        }
        public DomainModel getItem(int p){
            return  domains.get(p);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.generic_list_adapter, parent, false);
            }
                DomainModel model = getItem(position);
            TextView btn = (TextView) convertView.findViewById(R.id.itemTitle);
            btn.setTag(position);
            btn.setText(model.getDomainTitle());
            btn.setOnClickListener(this);


            return convertView;
        }

        @Override
        public void onClick(View v) {
            DomainModel selectedItem = getItem((int)v.getTag());
            if(selectedItem !=null){
                Intent domainsIntent = new Intent(ctx, GoalsActivity.class);
                domainsIntent.putExtra("domainId",selectedItem.getDomainId());
                ctx.startActivity(domainsIntent);

            }
        }
    }

