package com.zuccessful.cleanwise.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zuccessful.cleanwise.R;
import com.zuccessful.cleanwise.pojo.Job_MT17010;
import com.zuccessful.cleanwise.utilities.Utilities_MT17010;

import java.util.ArrayList;

public class HistoryAdapter_MT17010 extends RecyclerView.Adapter<HistoryAdapter_MT17010.HistoryVH> {

    private ArrayList<Job_MT17010> jobs;
    private Context mContext;
    private static final String TAG = HistoryAdapter_MT17010.class.getSimpleName();

    public HistoryAdapter_MT17010(Context mContext) {
        this.jobs = new ArrayList<>();
        this.mContext = mContext;
    }

    public HistoryAdapter_MT17010(Context mContext, ArrayList<Job_MT17010> jobs) {
        this.jobs = jobs;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HistoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.job_list_item, parent, false);
        return new HistoryVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryVH holder, int position) {
        Job_MT17010 j = jobs.get(position);
        holder.washroomIdView.setText(j.getWashroomId());
        holder.slotView.setText(Utilities_MT17010.getTimeSlot(j.getSlot()));
        holder.dateView.setText(Utilities_MT17010.getDateString(j.getTimestamp()));
        holder.setListener(j);
    }

    public void updateJobs(ArrayList<Job_MT17010> newJobs) {
        jobs = newJobs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    static class HistoryVH extends RecyclerView.ViewHolder {
        TextView washroomIdView, slotView, dateView;
        Job_MT17010 job;

        HistoryVH(View itemView) {
            super(itemView);
            washroomIdView = itemView.findViewById(R.id.washroom_id);
            slotView = itemView.findViewById(R.id.slot);
            dateView = itemView.findViewById(R.id.date);
        }

        void setListener(Job_MT17010 j) {
            this.job = j;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Item Clicked " + job.getId());
                }
            });
        }
    }
}
