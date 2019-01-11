package com.dev.reef.readonesia;

import android.content.res.Resources;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReadActivity extends AppCompatActivity implements View.OnClickListener {

    private String idPoster;
    private String idPost;

    private ImageView imagePost;
    private ImageView imagePhotoProfileComment;

    private ImageView sendButton;
    private EditText editTextComment;

    private TextView author;
    private TextView title;
    private WebView description;

    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;
    private CommentAdapter mAdapter;
    private ArrayList<Comment> listComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        imagePost = (ImageView) findViewById(R.id.image_post);
        author = (TextView) findViewById(R.id.author);
        title = (TextView) findViewById(R.id.title);
        description = (WebView) findViewById(R.id.webview_description);
        imagePhotoProfileComment = (ImageView) findViewById(R.id.image_comment);
        sendButton = (ImageView) findViewById(R.id.send_button);
        editTextComment = (EditText) findViewById(R.id.edit_text_comment);

        idPoster = "";
        idPost = "";

        try{
            idPoster = getIntent().getStringExtra("id_poster");
            idPost = getIntent().getStringExtra("id_post");
        } catch (Exception e) {

        }

        listComment = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mAdapter = new CommentAdapter(listComment, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        sendButton.setOnClickListener(this);


        parsingData();
        getDate();
        getCommentData();

    }

    public void parsingData() {
        final DatabaseReference bacaanPath = mDatabase.child("users").child(idPoster);
        bacaanPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                author.setText(dataSnapshot.child("name").getValue().toString());

                DataSnapshot postSnapshot = dataSnapshot.child("post").child(idPost);

                title.setText(postSnapshot.child("title").getValue().toString());

                String template_translation = "<html>" +
                        "<head>" +
                        "<style type=\"text/css\">" +
                        "@font-face {" +
                        "    font-family: MyFont;" +
                        "    src: url(\"file:///android_asset/fonts/timesnewroman.ttf\")" +
                        "}" +
                        "body {" +
                        "    font-family: MyFont;" +
                        "white-space: pre-wrap;" +
                        "    padding:0;"+
                        "        margin:0px;" +
                        "    font-size: " + 16 + "px;" +
                        "    text-align: justify;" +
                        "}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "%s" +
                        "</body>" +
                        "</html>";

                String descriptionText = String.format(template_translation, postSnapshot.child("description").getValue().toString());
                description.loadDataWithBaseURL("", descriptionText, "text/html", "utf-8", null);

                Glide.with(getApplicationContext())
                        .load(postSnapshot.child("urlToImage").getValue().toString())
                        .into(imagePost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public float convertDptoPx(int dp) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                Date date = new Date();
                String dateString = formatter.format(date);

                String textKomentar = editTextComment.getText().toString();

                if(textKomentar.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Isi komentar terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                }

                Comment comment = new Comment(auth.getUid(), textKomentar);

                DatabaseReference commentDb = mDatabase.child("users").child(idPoster).child("post").child(idPost).child("comment").child(dateString + auth.getUid());
                commentDb.setValue(comment);

                editTextComment.setText("");
        }
    }

    public void getDate() {
        DatabaseReference db = mDatabase.child("users").child(auth.getUid());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                options.placeholder(R.drawable.placeholder);

                try {
                    Glide.with(getApplicationContext())
                            .load(dataSnapshot.child("imageUrl").getValue().toString())
                            .apply(options)
                            .into(imagePhotoProfileComment);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getCommentData() {
        DatabaseReference db = mDatabase.child("users").child(idPoster).child("post").child(idPost).child("comment");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment.clear();
                for(DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    String idCommenter = commentSnapshot.child("idCommenter").getValue().toString();
                    String comment = commentSnapshot.child("comment").getValue().toString();

                    Comment comment1 = new Comment(idCommenter, comment);

                    listComment.add(comment1);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
