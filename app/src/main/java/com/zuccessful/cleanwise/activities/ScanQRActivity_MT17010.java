package com.zuccessful.cleanwise.activities;

//import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zuccessful.cleanwise.CheckNetworkReceiver_MT17010;
import com.zuccessful.cleanwise.CleanWiseApp_MT17010;
import com.zuccessful.cleanwise.R;
import com.zuccessful.cleanwise.ShowPoints_MT17010;
import com.zuccessful.cleanwise.pojo.Supervisor_MT17010;
import com.zuccessful.cleanwise.pojo.Washroom_MT17010;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class ScanQRActivity_MT17010 extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, OnQRCodeReadListener {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;

    private ViewGroup mainLayout;
    private Button logOutButton;
    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;
    private CheckBox flashlightCheckBox;
    protected Button historyButton;
    private ShowPoints_MT17010 pointsOverlayView;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    SignInActivity_MT17010 signInActivityActivity = new SignInActivity_MT17010();
    FirebaseUser user;
    private CleanWiseApp_MT17010 application;
    boolean receiver_flag = false;

    boolean doubleBackToExitPressedOnce = false;
    String LOG_TAG = "sir's log";

    BroadcastReceiver networkBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkBroadcastReceiver = new CheckNetworkReceiver_MT17010();

//        setContentView(R.layout.activity_scan_qr);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        application = CleanWiseApp_MT17010.getInstance();

//        logOutButton = (Button) findViewById(R.id.log_out);


//        mAuth = FirebaseAuth.getInstance();


//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() != null) {
//                    Intent intent = new Intent(SignInActivity_MT17010.this, ScanQRActivity_MT17010.class);
//                    intent.putExtra("account_name", account_name);
//                    startActivity(intent);
//                }
//            }
//        };
        Log.d(LOG_TAG, "in oncreate");

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() == null){
//                    startActivity(new Intent(ScanQRActivity_MT17010.this, SignInActivity_MT17010.class));
//                }
//            }
//        };

//        try {
//            releaseCameraAndPreview();
//            if (camId == 0) {
//                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
//            }
//            else {
//                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
//            }
//        } catch (Exception e) {
//            Log.e(getString(R.string.app_name), "failed to open Camera");
//            e.printStackTrace();
//        }

    }

//    public boolean isCameraUsebyApp() {
//        Camera camera = null;
//        try {
//            camera = Camera.open();
//        } catch (RuntimeException e) {
//            return true;
//        } finally {
//            if (camera != null) camera.release();
//        }
//        return false;
//    }

//    private void releaseCameraAndPreview() {
//        myCameraPreview.setCamera(null);
//        if (mCamera != null) {
//            mCamera.release();
//            mCamera = null;
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "in onstart");
//        super.onStart();
//        if (user != null) {
////            userDetailsText.setText("User: " + user.getDisplayName() + "\nEmail: " + user.getEmail() + "\nAccess: Granted");
//            //user.getPhotoUrl()
////            userNameText.setText);
////            userEmailText.setText(user.getEmail());
////            mAuth.addAuthStateListener(mAuthListener);
//
//        } else {
////            userDetailsText.setText("Not Logged In!");
//            finish();
//        }

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSION_REQUEST_CAMERA);
            Log.d(LOG_TAG, "if on create");
        }
        else{
            setContentView(R.layout.activity_scan_qr);
            mainLayout = (ViewGroup) findViewById(R.id.main_layout);
            resultTextView = (TextView) findViewById(R.id.result_text_view);
            resultTextView.setText(user.getDisplayName());
            historyButton = (Button) findViewById(R.id.scan_screen_button);

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(networkBroadcastReceiver, intentFilter);
            receiver_flag = true;


            Log.d(LOG_TAG, "else on create");
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
            if (qrCodeReaderView != null) {
                qrCodeReaderView.startCamera();
            }
//        } else {
//            requestCameraPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "in resume");


//        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_DENIED)) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CAMERA},
//                    MY_PERMISSION_REQUEST_CAMERA);
//            Log.d(LOG_TAG, "if on resume");
//        }
//        else{
//            Log.d(LOG_TAG, "else on resume");
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            initQRCodeReaderView();
//            if (qrCodeReaderView != null) {
//                qrCodeReaderView.startCamera();
//            }
////        } else {
////            requestCameraPermission();
//        }
        Log.d(LOG_TAG, "in resume exit");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onpause");

        if (qrCodeReaderView != null) {
            qrCodeReaderView.stopCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(LOG_TAG, "in requestpermission");
//        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
//            return;
//        }
        if (requestCode == MY_PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //            Snackbar.make(mainLayout, "Camera permission was granted.", Snackbar.LENGTH_SHORT).show();

                setContentView(R.layout.activity_scan_qr);
                mainLayout = (ViewGroup) findViewById(R.id.main_layout);
                resultTextView = (TextView) findViewById(R.id.result_text_view);
                resultTextView.setText(user.getDisplayName());
                historyButton = (Button) findViewById(R.id.scan_screen_button);

                final IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                registerReceiver(networkBroadcastReceiver, intentFilter);
                receiver_flag = true;

                initQRCodeReaderView();
            } else {
//                Snackbar.make(mainLayout, "Camera permission request was denied.", Snackbar.LENGTH_SHORT).show();
                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth.getCurrentUser() == null) {
                            Intent toWashroomScreenIntent = new Intent(ScanQRActivity_MT17010.this, SignInActivity_MT17010.class);
                            startActivity(toWashroomScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    }
                };

                if (!ActivityCompat.shouldShowRequestPermissionRationale(ScanQRActivity_MT17010.this, permissions[0])) {
                    Toast.makeText(ScanQRActivity_MT17010.this, "Please provide the required permissions for the app to work!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else
                    ActivityCompat.requestPermissions(ScanQRActivity_MT17010.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
            }
        }

    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        pointsOverlayView.setPoints(points);
        Intent toWashroomScreenIntent = new Intent(ScanQRActivity_MT17010.this, WashroomActivity_MT17010.class);
        toWashroomScreenIntent.putExtra("washroomId", text);
        toWashroomScreenIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(toWashroomScreenIntent);

    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(ScanQRActivity_MT17010.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
    }

    private void initQRCodeReaderView() {

//        View content = getLayoutInflater().inflate(R.layout.activity_scan_qr, mainLayout, true);

        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        pointsOverlayView = (ShowPoints_MT17010) findViewById(R.id.points_overlay_view);
        flashlightCheckBox = (CheckBox) findViewById(R.id.flashlight_checkbox);

        qrCodeReaderView.setAutofocusInterval(1000L);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setBackCamera();
        flashlightCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                qrCodeReaderView.setTorchEnabled(isChecked);
            }
        });
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHistoryActivityIntent = new Intent(ScanQRActivity_MT17010.this, HistoryActivity_MT17010.class);
                startActivity(toHistoryActivityIntent);
            }
        });

//        logOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signOut();
//            }
//        });


        qrCodeReaderView.startCamera();
    }

    public void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        signInActivityActivity.updateUI(null);
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                signOut();
                return true;
//            case R.id.action_insert:
//                ArrayList<Washroom_MT17010> washrooms = new ArrayList<>();
//                washrooms.add(new Washroom_MT17010("OBH1-F0-W1", "Old Boys Hostel", 0));
//                washrooms.add(new Washroom_MT17010("OBH1-F0-W2", "Old Boys Hostel", 0));
//                washrooms.add(new Washroom_MT17010("OBH1-F0-W3", "Old Boys Hostel", 0));
//                washrooms.add(new Washroom_MT17010("OBH1-F1-W1", "Old Boys Hostel", 1));
//                washrooms.add(new Washroom_MT17010("OBH1-F1-W2", "Old Boys Hostel", 1));
//                washrooms.add(new Washroom_MT17010("OBH1-F1-W3", "Old Boys Hostel", 1));
//                washrooms.add(new Washroom_MT17010("OBH1-F2-W1", "Old Boys Hostel", 2));
//                washrooms.add(new Washroom_MT17010("OBH1-F2-W2", "Old Boys Hostel", 2));
//                washrooms.add(new Washroom_MT17010("OBH1-F2-W3", "Old Boys Hostel", 2));
//                DatabaseReference washroomReference = application.getFirebaseDatabaseInstance().getReference("washrooms");
//                for (Washroom_MT17010 w : washrooms) {
//                    washroomReference.child(w.getId()).setValue(w);
//                }
//
//                ArrayList<Supervisor_MT17010> supervisors = new ArrayList<>();
//                supervisors.add(new Supervisor_MT17010("vaibhav17065","vaibhav17065@iiitd.ac.in", "Vaibhav Varshney", FirebaseInstanceId.getInstance().getToken()));
//                supervisors.add(new Supervisor_MT17010("yogesh17071","yogesh17071@iiitd.ac.in", "Yogesh IIITD", FirebaseInstanceId.getInstance().getToken()));
//                supervisors.add(new Supervisor_MT17010("shubhi17057","shubhi17057@iiitd.ac.in", "Shubhi Tiwari", FirebaseInstanceId.getInstance().getToken()));
//                supervisors.add(new Supervisor_MT17010("rajshree17045","rajshree17045@iiitd.ac.in", "Rajshree Khare", FirebaseInstanceId.getInstance().getToken()));
//                supervisors.add(new Supervisor_MT17010("chirag17010","chirag17010@iiitd.ac.in", "Chirag Khurana", FirebaseInstanceId.getInstance().getToken()));
//                DatabaseReference supervisorReference = application.getFirebaseDatabaseInstance().getReference("supervisors");
//                for (Supervisor_MT17010 s: supervisors) {
//                    supervisorReference.child(s.getId()).setValue(s);
//                }
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "on stop");

        if(receiver_flag == true)
            unregisterReceiver(networkBroadcastReceiver);
    }
}