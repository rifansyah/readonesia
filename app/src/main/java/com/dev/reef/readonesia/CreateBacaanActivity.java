package com.dev.reef.readonesia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateBacaanActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private CircleImageView addImage;
    private TextView fileNameText;
    private Button button;
    private RadioGroup radioCategory;
    private RadioButton radioBukis;
    private RadioButton radioArtikel;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    private String fileName;
    private String idPost;
    private String urlToImage;

    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bacaan);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        titleEditText = (EditText) findViewById(R.id.title);
        descriptionEditText = (EditText) findViewById(R.id.description);
        button = (Button) findViewById(R.id.button);
        radioCategory = (RadioGroup) findViewById(R.id.radio_category);
        radioBukis = (RadioButton) findViewById(R.id.radio_bukis);
        radioArtikel = (RadioButton) findViewById(R.id.radio_artikel);
        addImage = (CircleImageView) findViewById(R.id.add_image);
        fileNameText = (TextView) findViewById(R.id.filename);

        fileName = "";
        idPost = generateIdPost();

        button.setOnClickListener(this);
        addImage.setOnClickListener(this);
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

            fileNameText.setText(filePath.toString());

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(final String title, final String description) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            fileName = UUID.randomUUID().toString();

            StorageReference ref = storageReference.child("images/"+ fileName);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            fileName = taskSnapshot.getMetadata().getName();

                            urlToImage = "";

                            storageReference.child("images").child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlToImage = uri.toString();

                                    Post post = new Post(title, description, getCategory(), getDate(), urlToImage);

                                    DatabaseReference postReference = mDatabase.child("users").child(firebaseUser.getUid().toString()).child("post").child(idPost);
                                    postReference.setValue(post);

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

    public String generateIdPost() {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMMMyyyy-HH:mm:ss");
        Date date = new Date();
        return "post" + formatter.format(date);
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        Date date = new Date();
        return formatter.format(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(getApplicationContext(), "Masukkan judul bacaan", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(getApplicationContext(), "Masukkan konten", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    uploadImage(title, description);
                } catch (Exception e) {
                    Post post = new Post(title, description, getCategory(), getDate(), "");

                    DatabaseReference postReference = mDatabase.child("users").child(firebaseUser.getUid().toString()).child("post").child(idPost);
                    postReference.setValue(post);

                    finish();
                    break;
                }

                break;
            case R.id.add_image:
                chooseImage();
                break;
        }
    }

    public String getCategory() {
        int selectedId = radioCategory.getCheckedRadioButtonId();

        RadioButton radio = (RadioButton) findViewById(selectedId);

        return radio.getText().toString();
    }
}
