package com.zuccessful.cleanwise.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zuccessful.cleanwise.CheckNetworkReceiver_MT17010;
import com.zuccessful.cleanwise.CleanWiseApp_MT17010;
import com.zuccessful.cleanwise.R;
import com.zuccessful.cleanwise.adapters.ImageAdapter_MT17010;
import com.zuccessful.cleanwise.pojo.Job_MT17010;
import com.zuccessful.cleanwise.pojo.Record_MT17010;
import com.zuccessful.cleanwise.pojo.Supervisor_MT17010;
import com.zuccessful.cleanwise.utilities.Utilities_MT17010;
import com.zuccessful.cleanwise.views.ExpandableHeightGridView_MT17010;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class CheckListActivity_MT17010 extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;
    public static final int REQUEST_CAMERA = 1121;
    private Button uploadButton;
    private TextView washroomText;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private ImageAdapter_MT17010 imageAdapter;
    private ExpandableHeightGridView_MT17010 mCheckListGrid;
    private ProgressDialog progressDialog;

    private String[] checklistId;
    private Boolean[] checkedBoxes;


    private CleanWiseApp_MT17010 application;
    private Supervisor_MT17010 supervisor;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mJobsRef;
    private DatabaseReference mRecordRef;
    private ArrayList<Uri> allImagesUri = new ArrayList<>();
    private ArrayList<Uri> allBlankImagesUri = new ArrayList<>();
    private ArrayList<String> imagePaths;
    private String TAG = CheckListActivity_MT17010.class.getSimpleName();
    private String currDate;
    private File mCurrentPhotoFile;
    private String mCurrentPhotoPath;

    private int slot;
    private String washroomId;

    private int numColumns = 2;

    BroadcastReceiver networkBroadcastReceiver;
    boolean receiver_flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        Intent superviseTimeIntent = getIntent();
        String supervisionTime = superviseTimeIntent.getStringExtra("superviseTime");
        washroomId = superviseTimeIntent.getStringExtra("washroom_id");
        currDate = superviseTimeIntent.getStringExtra("date");
        slot = superviseTimeIntent.getIntExtra("slot", 0);
        imagePaths = new ArrayList<>();

        application = CleanWiseApp_MT17010.getInstance();
        networkBroadcastReceiver = new CheckNetworkReceiver_MT17010();

        uploadButton = findViewById(R.id.uploadButton);
        washroomText = findViewById(R.id.washroom_id);
        washroomText.setText(washroomId + "\n" + currDate + '\n' + Utilities_MT17010.getTimeSlot(slot));
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = findViewById(R.id.image_list);
        mCheckListGrid = findViewById(R.id.checklist_grid);

        String[] checklistItems = getResources().getStringArray(R.array.checklist_items);
        checklistId = getResources().getStringArray(R.array.checklist_item_ids);
        checkedBoxes = new Boolean[checklistItems.length];
        for (int i = 0; i < checkedBoxes.length; i++) {
            checkedBoxes[i] = false;
        }
        ArrayAdapter<String> checklistAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, checklistItems);


        mCheckListGrid.setNumColumns(numColumns);
        mCheckListGrid.setExpanded(true);
        mCheckListGrid.setAdapter(checklistAdapter);
        mCheckListGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView = ((CheckedTextView) view);
                checkedTextView.setChecked(!checkedTextView.isChecked());
                checkedBoxes[i] = checkedTextView.isChecked();
            }
        });


        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mJobsRef = application.getFirebaseDatabaseInstance().getReference("jobs");
        mRecordRef = application.getFirebaseDatabaseInstance().getReference("records");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onSuccess");
                try {
                    uploadImage();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        imageAdapter = new ImageAdapter_MT17010(this);
        imageAdapter.setOnItemClickListener(new ImageAdapter_MT17010.ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (view instanceof ImageView) {
                    imageAdapter.deleteImage(position);
                } else if (view instanceof ViewGroup) {
                    dispatchTakePictureIntent();
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        mRecyclerView.setAdapter(imageAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkBroadcastReceiver, intentFilter);
        receiver_flag = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver_flag)
            unregisterReceiver(networkBroadcastReceiver);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            numColumns = 4;
        else
            numColumns = 2;
    }

    private void showProgressDialog(String msg, int progress) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        if (progress != -1)
            progressDialog.setProgress(progress);
        progressDialog.show();
    }

    private void setProgressDialogProgress(int progress) {
        progressDialog.setMessage("Uploading Image...  " + progress + "%");
    }

    private void hideProgressDialog() {
        progressDialog.hide();
    }


    /* REFERENCE: Android Developers: https://developer.android.com/training/camera/photobasics.html */

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "CW_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = getExternalFilesDir(null);
        Log.d(TAG, storageDir.toString());
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Log.d(TAG, image.toString());

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void dispatchTakePictureIntent() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSION_REQUEST_CAMERA);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    // Continue only if the File was successfully created
                    Uri photoURI;
                    if (photoFile != null) {
                        mCurrentPhotoFile = photoFile;
                        photoURI = FileProvider.getUriForFile(CheckListActivity_MT17010.this, "com.zuccessful.cleanwise.provider", photoFile);
                        //                    photoURI = FileProvider.getUriForFile(this,
                        //                            "com.zuccessful.cleanwise.provider",
                        //                            photoFile);
                        allImagesUri.add(photoURI);
                        imagePaths.add(mCurrentPhotoPath);
                        Log.d(TAG, "New Func: " + photoFile.getAbsolutePath());
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                    }
                } catch (IOException ex) {
                    // Nothing
                    Log.d(TAG, "New Func: " + "Error Occurred while creating file!");
                } catch (IllegalArgumentException e) {
                    Log.d(TAG, e.toString());
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
//            return;
//        }
        if (requestCode == MY_PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(CheckListActivity_MT17010.this, permissions[0])) {
                    Toast.makeText(CheckListActivity_MT17010.this, "Please provide the required permissions for the app to work!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else
                    ActivityCompat.requestPermissions(CheckListActivity_MT17010.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
//            Bitmap bm = (Bitmap) data.getExtras().get("data");
//            Log.d(TAG, "Result from camera received " + bm.getConfig());
//        }
        switch (requestCode) {
            case REQUEST_CAMERA:

                if (resultCode == RESULT_OK) {
                    galleryAddPic();
                    imageAdapter.updateImageList(allImagesUri, imagePaths);
                } else {
                    if (mCurrentPhotoFile.exists()) {
                        if (mCurrentPhotoFile.delete()) {
                            Uri photoURI = FileProvider.getUriForFile(CheckListActivity_MT17010.this, "com.zuccessful.cleanwise.provider", mCurrentPhotoFile);
                            if (allImagesUri.contains(photoURI) && imagePaths.contains(mCurrentPhotoPath)) {
                                allImagesUri.remove(photoURI);
                                imagePaths.remove(mCurrentPhotoPath);
                                imageAdapter.updateImageList(allImagesUri, imagePaths);
                            }
                        } else {
                            Log.d(TAG, "File Not able to delete.");
                        }
                    }

                }
                break;
        }
//            String photouri = getIntent().getExtras().getString(MediaStore.EXTRA_OUTPUT);
////            if (allImagesUri.contains(photouri)){
//            Uri image = data.getData();
//            Toast.makeText(this, "HAAN HAI", Toast.LENGTH_SHORT).show();
//            allImagesUri.remove(image);
//
    }

    private String getImageExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() throws ParseException {
        Utilities_MT17010.Status aStatus = Utilities_MT17010.inWhichSlot().get(slot - 1);
        final Utilities_MT17010.Status actualStatus;
        if (aStatus.equals(Utilities_MT17010.Status.PENDING)) {
            actualStatus = Utilities_MT17010.Status.DONE;
        } else {
            actualStatus = aStatus;
        }

        showProgressDialog("Uploading Image", 0);
//        mProgressBar.setVisibility(View.VISIBLE);
        final Activity activity = this;
        if (!allImagesUri.isEmpty()) {
            final Job_MT17010 job = new Job_MT17010();
            // Collecting Checked List Data
            HashMap<String, Boolean> checklist = new HashMap<>();
            for (int i = 0; i < mCheckListGrid.getCount(); i++) {
//                checkedBoxes[i] = mCheckListGrid.isItemChecked(i);
                checklist.put(checklistId[i], checkedBoxes[i]);
            }
            job.setChecklist(checklist);
            job.setSlot(slot);
            job.setWashroomId(washroomId);
            job.setSupervisorId(application.getAppUser(null).getId());
            final ArrayList<String> imageUrls = new ArrayList<>();

            for (Uri imageUri : allImagesUri) {
                Log.d(TAG, imageUri.toString());
//                String filename = imageUri.getPathSegments().get(imageUri.getPathSegments().size() - 1);
                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                        + "." + getImageExtension(imageUri));

                fileReference.putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, String.valueOf(taskSnapshot.getBytesTransferred()));
                        double ratio = ((float) taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        setProgressDialogProgress((int) (ratio * 100));
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        imageUrls.add(taskSnapshot.getDownloadUrl().toString());
                        if (imageUrls.size() == allImagesUri.size()) {
                            job.setImages(imageUrls);
                            supervisor = application.getAppUser(null);
                            Log.d(TAG, supervisor.getId() + " : here");
                            mRecordRef.child(currDate).child(washroomId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    Log.d(TAG, dataSnapshot.toString());

                                    // If the record already exists, i.e. user is updating the record
                                    if (dataSnapshot.hasChild(String.valueOf(slot))) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (ds.getKey().equals(String.valueOf(slot))) {
                                                Record_MT17010 record = ds.getValue(Record_MT17010.class);
//                                                Log.d(TAG, supervisor.getId() + " : " + record.getSupervisorId());
                                                if (record != null && record.getSupervisorId().equals(supervisor.getId())) {
                                                    String jobId = record.getJobId();
                                                    Log.d(TAG, jobId);

                                                    // To delete previous set of images from server
                                                    mJobsRef.child(jobId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            Job_MT17010 j = dataSnapshot.getValue(Job_MT17010.class);
                                                            if (j != null) {
                                                                ArrayList<String> images = j.getImages();
                                                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                                                for (String image : images) {
                                                                    firebaseStorage.getReferenceFromUrl(image).delete();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    job.setId(jobId);
                                                    record.setJobId(job.getId());
                                                    record.setStatus(actualStatus);
                                                    record.setSupervisorId(job.getSupervisorId());
                                                    mJobsRef.child(jobId).setValue(job);
                                                    mJobsRef.child(jobId).child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                    mRecordRef.child(currDate).child(job.getWashroomId()).child(String.valueOf(slot)).setValue(record);
                                                    Toast.makeText(CheckListActivity_MT17010.this, "Job_MT17010 Updated!!!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(CheckListActivity_MT17010.this, "Already Registered! Not authorized to edit this entry.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                    } else {
                                        // New Records being submitted by the supervisor

                                        String jobId = mJobsRef.push().getKey();
                                        job.setId(jobId);

                                        Record_MT17010 record = new Record_MT17010();
                                        record.setJobId(job.getId());
                                        record.setStatus(actualStatus);
                                        record.setSupervisorId(job.getSupervisorId());

                                        mJobsRef.child(jobId).setValue(job);
                                        mJobsRef.child(jobId).child("timestamp").setValue(ServerValue.TIMESTAMP);
                                        mRecordRef.child(currDate).child(job.getWashroomId()).child(String.valueOf(slot)).setValue(record);

                                        //Add this job id to users past records
                                        if (supervisor != null) {
                                            supervisor.addJob(job.getId());
                                            application.getAppUser(supervisor);
                                        }
                                        Toast.makeText(CheckListActivity_MT17010.this, "Job_MT17010 Added!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    hideProgressDialog();
                                }
                            });
                            hideProgressDialog();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure");
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onProgress");
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        mProgressBar.setProgress((int) progress);
                    }
                });
            }

        } else {
            Toast.makeText(this, "No image Added!", Toast.LENGTH_SHORT).show();
            hideProgressDialog();
        }
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