package com.zuccessful.cleanwise.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zuccessful.cleanwise.CheckNetworkReceiver_MT17010;
import com.zuccessful.cleanwise.CleanWiseApp_MT17010;
import com.zuccessful.cleanwise.R;
import com.zuccessful.cleanwise.pojo.Record_MT17010;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WashroomActivity_MT17010 extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = WashroomActivity_MT17010.class.getSimpleName();
    private Button superviseMorningButton, superviseNoonButton, superviseEveningButton;
    private Button[] superviseButtons;
    private String washroomId;
    private CleanWiseApp_MT17010 application;
    private DatabaseReference mRecordRef;
    private String onlyDate;
    private DatabaseReference mWashroomRef;
    BroadcastReceiver networkBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washroom_screen);
        Intent intentFromMainActivity = getIntent();
        washroomId = intentFromMainActivity.getStringExtra("washroomId");
        TextView washroomIdView = findViewById(R.id.washroomId);
        washroomIdView.setText(washroomId);

        //initialize broadcast receiver
        networkBroadCastReceiver = new CheckNetworkReceiver_MT17010();

        application = CleanWiseApp_MT17010.getInstance();
        mRecordRef = application.getFirebaseDatabaseInstance().getReference("records");

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        onlyDate = dateFormat.format(date);

        superviseMorningButton = findViewById(R.id.superviseMorningButton);
        superviseNoonButton = findViewById(R.id.superviseNoonButton);
        superviseEveningButton = findViewById(R.id.superviseEveningButton);

        superviseButtons = new Button[]{superviseMorningButton, superviseNoonButton, superviseEveningButton};


        mWashroomRef = application.getFirebaseDatabaseInstance().getReference("washrooms");
        mWashroomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds != null && ds.getKey().equals(washroomId)) {
                        flag = true;
                        mRecordRef.child(onlyDate).child(washroomId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    for (Button superviseButton : superviseButtons) {
                                        superviseButton.setBackgroundResource(R.drawable.rounded_button);
                                    }
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        try {
                                            int slot = Integer.parseInt(ds.getKey()) - 1;
                                            switch (ds.getValue(Record_MT17010.class).getStatus()) {
                                                case DONE:
                                                    superviseButtons[slot].setBackgroundResource(R.drawable.rounded_button_green);
                                                    break;
                                                case LATE:
                                                    superviseButtons[slot].setBackgroundResource(R.drawable.rounded_button_red);
                                                    break;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        break;
                    }
                }
                if (!flag) {
                    Toast.makeText(WashroomActivity_MT17010.this, "Not a valid Washroom_MT17010 Id. Scan correct QR code", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Check the statuses of the washroom for the day


        superviseMorningButton.setOnClickListener(this);
        superviseNoonButton.setOnClickListener(this);
        superviseEveningButton.setOnClickListener(this);

        TextView setDate = findViewById(R.id.currentDate);
        setDate.setText(onlyDate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkBroadCastReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: Code to disable the buttons if time passed
        /*
            try {
                ArrayList<Utilities_MT17010.Status> statuses = Utilities_MT17010.inWhichSlot();
                for (int i = 0; i < superviseButtons.length && i < statuses.size(); i++) {
                    if (statuses.get(i) != Utilities_MT17010.Status.PENDING) {
                        superviseButtons[i].setEnabled(false);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        */
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

    @Override
    public void onClick(View view) {
        Intent callCheckListIntent = new Intent(WashroomActivity_MT17010.this, CheckListActivity_MT17010.class);
//        callCheckListIntent.putExtra("superviseTime", superviseMorningButton.getText());
        callCheckListIntent.putExtra("washroom_id", washroomId);
        callCheckListIntent.putExtra("date", onlyDate);
        callCheckListIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        switch (view.getId()) {
            case R.id.superviseMorningButton:
                callCheckListIntent.putExtra("slot", 1);
                startActivity(callCheckListIntent);
                break;
            case R.id.superviseNoonButton:
                callCheckListIntent.putExtra("slot", 2);
                startActivity(callCheckListIntent);
                break;
            case R.id.superviseEveningButton:
                callCheckListIntent.putExtra("slot", 3);
                startActivity(callCheckListIntent);
                break;
        }
    }
}
