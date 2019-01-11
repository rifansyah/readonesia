package com.dev.reef.readonesia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView photoProfile;
    private TextInputEditText nameView;
    private TextInputEditText usernameView;
    private Button simpanView;

    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 72;

    private String fileName;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();

        photoProfile = (CircleImageView) findViewById(R.id.photo_profile);
        nameView = (TextInputEditText) findViewById(R.id.name);
        usernameView = (TextInputEditText) findViewById(R.id.username);
        simpanView = (Button) findViewById(R.id.button);

        getData();

        photoProfile.setOnClickListener(this);
        simpanView.setOnClickListener(this);
    }

    public void getData() {
        DatabaseReference profileRef = mDatabase.child("users").child(FirebaseAuth.getInstance().getUid());
        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String userName = dataSnapshot.child("username").getValue().toString();
                String imageUrl = dataSnapshot.child("imageUrl").getValue().toString();


                RequestOptions options = new RequestOptions();
                options.centerCrop();
                options.placeholder(R.drawable.placeholder);

                Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .apply(options)
                        .into(photoProfile);

                nameView.setText(name);
                usernameView.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();

            String[] filePathColumn = filePath.toString().split("/");

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Glide.with(getApplicationContext())
                        .load(bitmap)
                        .into(photoProfile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(final String name, final String username) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            fileName = firebaseUser.getUid();

            StorageReference ref = storageReference.child("users/"+ fileName);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            fileName = taskSnapshot.getMetadata().getName();

                            imageUrl = "";

                            storageReference.child("users").child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();

                                    DatabaseReference profileRef = mDatabase.child("users").child(firebaseUser.getUid());
                                    profileRef.child("name").setValue(name);
                                    profileRef.child("username").setValue(username);
                                    profileRef.child("imageUrl").setValue(imageUrl);

                                    finish();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            throw new NullPointerException();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        } else {
            throw new NullPointerException("error");
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_profile:
                chooseImage();
                break;
            case R.id.button:
                String name = nameView.getText().toString();
                String username = usernameView.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    uploadImage(name, username);
                } catch (Exception e) {
                    DatabaseReference profileRef = mDatabase.child("users").child(firebaseUser.getUid());
                    profileRef.child("name").setValue(name);
                    profileRef.child("username").setValue(username);

                    finish();
                }
                break;
        }
    }
}
