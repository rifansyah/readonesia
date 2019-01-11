package com.dev.reef.readonesia.Beradu;


import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.reef.readonesia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameBacaFragment extends Fragment {
    private String idPost;
    private DatabaseReference mDatabase;

    private ImageView photo;
    private TextView titleView;
    private TextView authorView;
    private WebView descriptionView;

    public GameBacaFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public GameBacaFragment(String idPost) {
        this.idPost = idPost;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_baca, container, false);

        photo = (ImageView) v.findViewById(R.id.image_post);
        titleView = (TextView) v.findViewById(R.id.title);
        authorView = (TextView) v.findViewById(R.id.author);
        descriptionView = (WebView) v.findViewById(R.id.webview_description);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getData();

        return v;
    }

    public void getData() {
        DatabaseReference postRef = mDatabase.child("game_post").child(idPost);
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                authorView.setText(dataSnapshot.child("author").getValue().toString());

                titleView.setText(dataSnapshot.child("title").getValue().toString());

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

                String descriptionText = String.format(template_translation, dataSnapshot.child("description").getValue().toString());
                descriptionView.loadDataWithBaseURL("", descriptionText, "text/html", "utf-8", null);

                Glide.with(getContext())
                        .load(dataSnapshot.child("urlToImage").getValue().toString())
                        .into(photo);
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

}
