package com.facebooklogin.application.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebooklogin.application.R;
import com.facebooklogin.application.entities.Job;
import com.facebooklogin.application.entities.Like;

import java.util.List;


public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ContactViewHolder>{

    private Context context;
    private List<Job> jobList;


    public JobAdapter(Context context, List<Job> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_job, parent, false);

        return new ContactViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return jobList.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView position, company, period;
        ContactViewHolder(View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            company = itemView.findViewById(R.id.company);
            period = itemView.findViewById(R.id.period);
        }
    }



    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        final Job item = jobList.get(position);
        holder.position.setText(item.getPosition());
        holder.company.setText(item.getCompany());
        holder.period.setText(item.getPeriod());
        holder.setIsRecyclable(false);
    }
}

