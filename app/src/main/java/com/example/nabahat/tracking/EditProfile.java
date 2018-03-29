package com.example.nabahat.tracking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import static java.security.AccessController.getContext;


public class EditProfile extends AppCompatActivity {
    private EditText displayname, displaynumber, displayroute;
    private Button save, upload;
    private TextView emailedit;
    ImageView image;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 2;
    Uri uri;Uri downloadUri;

    StorageTask uploadTask;

    StorageReference storageRef;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        displayname = (EditText)findViewById(R.id.editname);
        emailedit = (TextView) findViewById(R.id.editemail);
        displaynumber = (EditText) findViewById(R.id.editphone);
        displayroute = (EditText)findViewById(R.id.editroute);
        save = (Button)findViewById(R.id.saveprofilebtn);
        image = (ImageView) findViewById(R.id.profileimageupload);
        upload = (Button)findViewById(R.id.upload);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorage = FirebaseStorage.getInstance().getReference();
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);






        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadTask != null &&  uploadTask.isInProgress() ){
                     upload.setClickable(false);
                     Toast.makeText(EditProfile.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent uploadintent = new Intent(Intent.ACTION_PICK);
                    uploadintent.setType("image/*");

                    startActivityForResult(uploadintent, GALLERY_INTENT);
                }

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Driver").child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Data Received",dataSnapshot.toString());
                if (dataSnapshot.exists()) {


                        String name= dataSnapshot.child("username").getValue(String.class);
                        String email= dataSnapshot.child("email").getValue(String.class);
                        String phone= dataSnapshot.child("phonenumber").getValue(String.class);
                        String route= dataSnapshot.child("busnumber").getValue(String.class);
                        displayname.setText(name);
                        emailedit.setText(email);
                        displaynumber.setText(phone);
                        displayroute.setText(route);


                    String path ="";

                    for (DataSnapshot child: dataSnapshot.child("image").getChildren()) {
                        path= child.getValue().toString();

                    }
                    Picasso.with(EditProfile.this).load(path)                            .into(image);

                }
                else{
                    Toast.makeText(EditProfile.this, "No Snapshot", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadTask != null &&  uploadTask.isInProgress() ){
                    upload.setClickable(false);
                    Toast.makeText(EditProfile.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                }
                else{
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Driver").child(userId);

                //  ref.child("username").setValue(displayname.getText());

                //Driver driver = new Driver(userId, displayname.getText().toString(), displayname.getText().toString(), displayname.getText().toString(), displayname.getText().toString());)
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String name = displayname.getText().toString();
                        String email = emailedit.getText().toString();
                        String phone = displaynumber.getText().toString();
                        String route = displayroute.getText().toString();
                        dataSnapshot.child("username").getRef().setValue(name);
                        dataSnapshot.child("email").getRef().setValue(email);
                        dataSnapshot.child("phonenumber").getRef().setValue(phone);
                        dataSnapshot.child("busnumber").getRef().setValue(route);

                        if (uri != null) {
                            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            StorageReference filepath = mStorage.child(userId);
                            //StorageReference storagereference = mStorage.getR
                            uploadTask = filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressBar.setProgress(0);
                                    Toast.makeText(EditProfile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                    downloadUri = taskSnapshot.getDownloadUrl();
                                   DatabaseReference newref = FirebaseDatabase.getInstance().getReference().child("Driver").child(userId);
                                   newref.child("image").child(userId).setValue(downloadUri.toString());
                                    // Toast.makeText(EditProfile.this, "uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressBar.setProgress((int) progress);
                                }
                            });
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if(requestCode==GALLERY_INTENT && resultCode ==RESULT_OK && data!=null && data.getData()!=null){

             uri = data.getData();
           //  Picasso.with(this).load(uri).into(image);
           /*  Glide.with(getApplicationContext())
                     .load(uri)
                     .into(image);*/
             Picasso.with(EditProfile.this).load(uri)                            .into(image);

/*
             Glide.with(getApplicationContext()).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(image) {
                 @Override
                 protected void setResource(Bitmap resource) {
                     RoundedBitmapDrawable circularBitmapDrawable =
                             RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                     circularBitmapDrawable.setCircular(true);
                     image.setImageDrawable(circularBitmapDrawable);
                 }
             });
*/


         }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent viewprofile = new Intent(EditProfile.this, ViewProfile.class);
        startActivity(viewprofile);
        finish();// mAuth.removeAuthStateListener(firebaseauthlistener);




    }
}

