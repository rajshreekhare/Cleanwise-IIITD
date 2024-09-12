package com.zuccessful.cleanwise.activities;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zuccessful.cleanwise.CheckNetworkReceiver_MT17010;
import com.zuccessful.cleanwise.CleanWiseApp_MT17010;
import com.zuccessful.cleanwise.R;
import com.zuccessful.cleanwise.adapters.HistoryAdapter_MT17010;
import com.zuccessful.cleanwise.pojo.Job_MT17010;
import com.zuccessful.cleanwise.pojo.Supervisor_MT17010;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity_MT17010 extends AppCompatActivity {

    private static final String TAG = HistoryActivity_MT17010.class.getSimpleName();
    private TextView mEmptyTextView;
    private RecyclerView mHistoryList;

    private HistoryAdapter_MT17010 mAdapter;

    private CleanWiseApp_MT17010 application;
    private DatabaseReference mJobRef;
    private Supervisor_MT17010 supervisor;
    private ArrayList<Job_MT17010> jobs;
    BroadcastReceiver networkBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize broadcast receiver
        networkBroadCastReceiver = new CheckNetworkReceiver_MT17010();

        application = CleanWiseApp_MT17010.getInstance();
        mJobRef = application.getFirebaseDatabaseInstance().getReference("jobs");
        supervisor = application.getAppUser(null);
        jobs = new ArrayList<>();

        mHistoryList = findViewById(R.id.history_list);
        mEmptyTextView = findViewById(R.id.history_text);
        mAdapter = new HistoryAdapter_MT17010(this, jobs);
        mHistoryList.setLayoutManager(new LinearLayoutManager(this));
        mHistoryList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mHistoryList.setAdapter(mAdapter);

        mJobRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Job_MT17010 job = ds.getValue(Job_MT17010.class);

                        if (job != null && job.getSupervisorId() != null && supervisor != null) {
                            Log.d(TAG, supervisor.getId() + " : " + job.getSupervisorId());
                            if (job.getSupervisorId().equals(supervisor.getId()))
                                jobs.add(job);
                        }
                        if (mAdapter != null && jobs.size() > 0) {
                            mEmptyTextView.setVisibility(View.GONE);
                            mHistoryList.setVisibility(View.VISIBLE);
                            mAdapter.updateJobs(jobs);
                        } else {
                            mEmptyTextView.setVisibility(View.VISIBLE);
                            mHistoryList.setVisibility(View.GONE);
                        }
                    }
                    if (jobs.size() == 0) {
                        mHistoryList.setVisibility(View.GONE);
                        mEmptyTextView.setVisibility(View.VISIBLE);
                    } else {
                        mHistoryList.setVisibility(View.VISIBLE);
                        mEmptyTextView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkBroadCastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkBroadCastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
