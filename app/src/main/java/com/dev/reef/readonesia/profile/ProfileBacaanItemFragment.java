package com.dev.reef.readonesia.profile;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.reef.readonesia.Bacaan;
import com.dev.reef.readonesia.BacaanAdapter;
import com.dev.reef.readonesia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileBacaanItemFragment extends Fragment {
    private ArrayList<Bacaan> listBacaan;
    private RecyclerView recyclerView;
    private BacaanAdapter bacaanAdapter;

    private DatabaseReference mDatabase;

    public ProfileBacaanItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_bacaan_item, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        listBacaan = new ArrayList<Bacaan>();

        recyclerView = (RecyclerView) v.findViewById(R.id.list_item);

        bacaanAdapter = new BacaanAdapter(listBacaan, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bacaanAdapter);

//        prepareBacaanData();
        getBacaanData();
        return v;
    }

    public void getBacaanData() {
        DatabaseReference bacaanPath = mDatabase.child("users").child(FirebaseAuth.getInstance().getUid());
        bacaanPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listBacaan.clear();
                for (final DataSnapshot postSnapshot : dataSnapshot.child("post").getChildren() ) {
                    String title = postSnapshot.child("title").getValue().toString();
                    String description = postSnapshot.child("description").getValue().toString();
                    String category = postSnapshot.child("category").getValue().toString();
                    String author = dataSnapshot.child("name").getValue().toString();
                    String publishedAt = postSnapshot.child("publishedAt").getValue().toString();
                    String urlToImage = postSnapshot.child("urlToImage").getValue().toString();
                    String id = postSnapshot.getKey().toString();
                    String authorId = dataSnapshot.getKey().toString();

                    Bacaan bacaan = new Bacaan(title, description, category, author, publishedAt, urlToImage, id, authorId);
                    listBacaan.add(bacaan);
                }
                bacaanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    public void prepareBacaanData() {
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

}
