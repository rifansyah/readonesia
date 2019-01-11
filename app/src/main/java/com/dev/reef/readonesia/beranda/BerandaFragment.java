package com.dev.reef.readonesia.beranda;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dev.reef.readonesia.Bacaan;
import com.dev.reef.readonesia.BacaanAdapter;
import com.dev.reef.readonesia.R;
import com.dev.reef.readonesia.ReadActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BerandaFragment extends Fragment implements View.OnClickListener {
    private String categoryFragment;

    private RecyclerView recyclerView;
    private BacaanAdapter bacaanAdapter;
    private ArrayList<Bacaan> listBacaan;

    private DatabaseReference mDatabase;

    private ImageView bannerPhoto;
    private TextView bannerTitle;
    private TextView bannerDescription;
    private TextView bannerAuthor;
    private TextView bannerCategory;

    private ConstraintLayout layoutBerandaMainItem;


    public BerandaFragment() {
        this.categoryFragment = "semua";
    }


    @SuppressLint("ValidFragment")
    public BerandaFragment(String categoryFragment) {
        this.categoryFragment = categoryFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_beranda, container, false);

        listBacaan = new ArrayList<Bacaan>();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        recyclerView = (RecyclerView) v.findViewById(R.id.list_item);
        bannerPhoto = (ImageView) v.findViewById(R.id.imageView2);
        bannerTitle = (TextView) v.findViewById(R.id.title);
        bannerDescription = (TextView) v.findViewById(R.id.description);
        bannerAuthor = (TextView) v.findViewById(R.id.author);
        bannerCategory = (TextView) v.findViewById(R.id.category);
        layoutBerandaMainItem = (ConstraintLayout) v.findViewById(R.id.layout_beranda_main_item);

        bacaanAdapter = new BacaanAdapter(listBacaan, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bacaanAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);



//        prepareMockBacaanData();
        getBacaanData();
        getBacaanFavorit();

        layoutBerandaMainItem.setOnClickListener(this);

        return v;
    }

//    public void prepareMockBacaanData() {
//        listBacaan.add(new Bacaan("Populis di Tahun Politis", "Sebuah pola menarik yang senantiasa terulang, utamanya di satu tahun terakhir masa jabatan. \"Kebijakan Populis\", begitu banyak orang menamainya. Segala kebijakan yang dijalankan untuk mengurangi beban dan tekanan hidup rakyat sembari berusaha mengambil simpati darinya.", "Artikel", "Anu wa", "29/01/2018", "https://assets-a2.kompasiana.com/items/album/2018/08/31/pertamina-spbu-2-5b88b37143322f6a9a1468ba.jpg?t=o&v=760"));
//        listBacaan.add(new Bacaan("Populis di Tahun Politis", "Sebuah pola menarik yang senantiasa terulang, utamanya di satu tahun terakhir masa jabatan. \"Kebijakan Populis\", begitu banyak orang menamainya. Segala kebijakan yang dijalankan untuk mengurangi beban dan tekanan hidup rakyat sembari berusaha mengambil simpati darinya.", "Artikel", "Anu wa", "29/01/2018", "https://assets-a2.kompasiana.com/items/album/2018/08/31/pertamina-spbu-2-5b88b37143322f6a9a1468ba.jpg?t=o&v=760"));
//        listBacaan.add(new Bacaan("Populis di Tahun Politis", "Sebuah pola menarik yang senantiasa terulang, utamanya di satu tahun terakhir masa jabatan. \"Kebijakan Populis\", begitu banyak orang menamainya. Segala kebijakan yang dijalankan untuk mengurangi beban dan tekanan hidup rakyat sembari berusaha mengambil simpati darinya.", "Artikel", "Anu wa", "29/01/2018", "https://assets-a2.kompasiana.com/items/album/2018/08/31/pertamina-spbu-2-5b88b37143322f6a9a1468ba.jpg?t=o&v=760"));
//        listBacaan.add(new Bacaan("Populis di Tahun Politis", "Sebuah pola menarik yang senantiasa terulang, utamanya di satu tahun terakhir masa jabatan. \"Kebijakan Populis\", begitu banyak orang menamainya. Segala kebijakan yang dijalankan untuk mengurangi beban dan tekanan hidup rakyat sembari berusaha mengambil simpati darinya.", "Artikel", "Anu wa", "29/01/2018", "https://assets-a2.kompasiana.com/items/album/2018/08/31/pertamina-spbu-2-5b88b37143322f6a9a1468ba.jpg?t=o&v=760"));
//        listBacaan.add(new Bacaan("Populis di Tahun Politis", "Sebuah pola menarik yang senantiasa terulang, utamanya di satu tahun terakhir masa jabatan. \"Kebijakan Populis\", begitu banyak orang menamainya. Segala kebijakan yang dijalankan untuk mengurangi beban dan tekanan hidup rakyat sembari berusaha mengambil simpati darinya.", "Artikel", "Anu wa", "29/01/2018", "https://assets-a2.kompasiana.com/items/album/2018/08/31/pertamina-spbu-2-5b88b37143322f6a9a1468ba.jpg?t=o&v=760"));
//        listBacaan.add(new Bacaan("Populis di Tahun Politis", "Sebuah pola menarik yang senantiasa terulang, utamanya di satu tahun terakhir masa jabatan. \"Kebijakan Populis\", begitu banyak orang menamainya. Segala kebijakan yang dijalankan untuk mengurangi beban dan tekanan hidup rakyat sembari berusaha mengambil simpati darinya.", "Artikel", "Anu wa", "29/01/2018", "https://assets-a2.kompasiana.com/items/album/2018/08/31/pertamina-spbu-2-5b88b37143322f6a9a1468ba.jpg?t=o&v=760"));
//        listBacaan.add(new Bacaan("Populis di Tahun Politis", "Sebuah pola menarik yang senantiasa terulang, utamanya di satu tahun terakhir masa jabatan. \"Kebijakan Populis\", begitu banyak orang menamainya. Segala kebijakan yang dijalankan untuk mengurangi beban dan tekanan hidup rakyat sembari berusaha mengambil simpati darinya.", "Artikel", "Anu wa", "29/01/2018", "https://assets-a2.kompasiana.com/items/album/2018/08/31/pertamina-spbu-2-5b88b37143322f6a9a1468ba.jpg?t=o&v=760"));
//
//        bacaanAdapter.notifyDataSetChanged();
//    }

    public void getBacaanData() {
        DatabaseReference bacaanPath = mDatabase.child("users");
        bacaanPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listBacaan.clear();
                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (final DataSnapshot bacaanSnapshot : postSnapshot.child("post").getChildren()) {
                        String title = bacaanSnapshot.child("title").getValue().toString();
                        String description = bacaanSnapshot.child("description").getValue().toString();
                        String category = bacaanSnapshot.child("category").getValue().toString();
                        String author = postSnapshot.child("name").getValue().toString();
                        String publishedAt = bacaanSnapshot.child("publishedAt").getValue().toString();
                        String urlToImage = bacaanSnapshot.child("urlToImage").getValue().toString();
                        String id = bacaanSnapshot.getKey().toString();
                        String authorId = postSnapshot.getKey().toString();

                        Bacaan bacaan = new Bacaan(title, description, category, author, publishedAt, urlToImage, id, authorId);
                        if(categoryFragment.equalsIgnoreCase("semua")) {
                            listBacaan.add(bacaan);
                        } else if (categoryFragment.equalsIgnoreCase("bukis") && category.equalsIgnoreCase("bukis")) {
                            listBacaan.add(bacaan);
                        } else if (categoryFragment.equalsIgnoreCase("artikel") && category.equalsIgnoreCase("artikel")) {
                            listBacaan.add(bacaan);
                        }
                    }
                }
                bacaanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getBacaanFavorit() {
        DatabaseReference bacaanPath = mDatabase.child("fav_post");
        bacaanPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (categoryFragment.equalsIgnoreCase("artikel") && postSnapshot.child("category").getValue().toString().equalsIgnoreCase("artikel")){

                        DatabaseReference favPath = mDatabase.child("users").child(postSnapshot.child("id_poster").getValue().toString());
                        favPath.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String author = dataSnapshot.child("name").getValue().toString();

                                DataSnapshot postSnapshotnya = dataSnapshot.child("post").child(postSnapshot.child("id_post").getValue().toString());

                                String title = postSnapshotnya.child("title").getValue().toString();
                                String category = "artikel";
                                String description = postSnapshotnya.child("description").getValue().toString();
                                String urlToImage = postSnapshotnya.child("urlToImage").getValue().toString();

                                bannerTitle.setText(title);
                                bannerAuthor.setText(author);
                                bannerCategory.setText(category);
                                bannerDescription.setText(description);

                                RequestOptions options = new RequestOptions();
                                options.centerCrop();
                                options.placeholder(R.drawable.placeholder);

                                try{
                                    Glide.with(getContext())
                                            .load(urlToImage)
                                            .apply(options)
                                            .into(bannerPhoto);
                                } catch (Exception e) {

                                }

                                layoutBerandaMainItem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getContext(), ReadActivity.class);
                                        intent.putExtra("id_poster", postSnapshot.child("id_poster").getValue().toString());
                                        intent.putExtra("id_post", postSnapshot.child("id_post").getValue().toString());
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else if (categoryFragment.equalsIgnoreCase("bukis") && postSnapshot.child("category").getValue().toString().equalsIgnoreCase("bukis")){

                        DatabaseReference favPath = mDatabase.child("users").child(postSnapshot.child("id_poster").getValue().toString());
                        favPath.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String author = dataSnapshot.child("name").getValue().toString();

                                DataSnapshot postSnapshotnya = dataSnapshot.child("post").child(postSnapshot.child("id_post").getValue().toString());

                                String title = postSnapshotnya.child("title").getValue().toString();
                                String category = "bukis";
                                String description = postSnapshotnya.child("description").getValue().toString();
                                String urlToImage = postSnapshotnya.child("urlToImage").getValue().toString();

                                bannerTitle.setText(title);
                                bannerAuthor.setText(author);
                                bannerCategory.setText(category);
                                bannerDescription.setText(description);

                                try{
                                    RequestOptions options = new RequestOptions();
                                    options.centerCrop();
                                    options.placeholder(R.drawable.placeholder);
                                    Glide.with(getContext())
                                            .load(urlToImage)
                                            .apply(options)
                                            .into(bannerPhoto);
                                } catch (Exception e) {

                                }

                                layoutBerandaMainItem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getContext(), ReadActivity.class);
                                        intent.putExtra("id_poster", postSnapshot.child("id_poster").getValue().toString());
                                        intent.putExtra("id_post", postSnapshot.child("id_post").getValue().toString());
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else if(categoryFragment.equalsIgnoreCase("semua")) {

                        DatabaseReference favPath = mDatabase.child("users").child(postSnapshot.child("id_poster").getValue().toString());
                        favPath.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String author = dataSnapshot.child("name").getValue().toString();

                                DataSnapshot postSnapshotnya = dataSnapshot.child("post").child(postSnapshot.child("id_post").getValue().toString());

                                String title = postSnapshotnya.child("title").getValue().toString();
                                String category = postSnapshotnya.child("category").getValue().toString();
                                String description = postSnapshotnya.child("description").getValue().toString();
                                String urlToImage = postSnapshotnya.child("urlToImage").getValue().toString();

                                bannerTitle.setText(title);
                                bannerAuthor.setText(author);
                                bannerCategory.setText(category);
                                bannerDescription.setText(description);

                                try{
                                    RequestOptions options = new RequestOptions();
                                    options.centerCrop();
                                    options.placeholder(R.drawable.placeholder);

                                    Glide.with(getContext())
                                            .load(urlToImage)
                                            .apply(options)
                                            .into(bannerPhoto);
                                } catch (Exception e) {

                                }

                                layoutBerandaMainItem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getContext(), ReadActivity.class);
                                        intent.putExtra("id_poster", postSnapshot.child("id_poster").getValue().toString());
                                        intent.putExtra("id_post", postSnapshot.child("id_post").getValue().toString());
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_beranda_main_item:
                break;
        }
    }
}
