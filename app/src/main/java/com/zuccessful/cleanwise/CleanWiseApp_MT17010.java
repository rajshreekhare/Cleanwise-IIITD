package com.zuccessful.cleanwise;

import android.app.Application;
import android.util.Log;

import com.zuccessful.cleanwise.pojo.Supervisor_MT17010;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CleanWiseApp_MT17010 extends Application {

    private static final String TAG = CleanWiseApp_MT17010.class.getSimpleName();
    public static CleanWiseApp_MT17010 instance;
    public static FirebaseDatabase firebaseDbInstance;
    public static Supervisor_MT17010 supervisor;


    synchronized public static CleanWiseApp_MT17010 getInstance() {
        if (instance == null) {
            instance = new CleanWiseApp_MT17010();
        }
        return instance;
    }

    synchronized public FirebaseDatabase getFirebaseDatabaseInstance() {
        if (firebaseDbInstance == null) {
            firebaseDbInstance = FirebaseDatabase.getInstance();
            firebaseDbInstance.setPersistenceEnabled(true);
        }
        return firebaseDbInstance;
    }

    synchronized public Supervisor_MT17010 getAppUser(final Supervisor_MT17010 s) {
        if (s != null) {
            Log.d(TAG, s.getJobIds().toString());
            DatabaseReference ref = getFirebaseDatabaseInstance().getReference("supervisors");
            supervisor = s;
            ref.child(s.getId()).setValue(s);
        } else if (supervisor == null) {
            Log.d(TAG, "Turning out to be null for both objects");
            supervisor = new Supervisor_MT17010();
        } else {
            Log.d(TAG, "Else here? " + supervisor.getEmailId() + supervisor.getId());
        }
        return supervisor;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        getFirebaseDatabaseInstance();
        instance = this;
        FirebaseApp.initializeApp(this);
//        FirebaseMessaging.getInstance().subscribeToTopic("events");
    }
}
