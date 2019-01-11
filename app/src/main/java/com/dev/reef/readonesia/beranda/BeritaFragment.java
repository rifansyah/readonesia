package com.dev.reef.readonesia.beranda;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev.reef.readonesia.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeritaFragment extends Fragment {
    private ProgressDialog progressDoalog;
    private RecyclerView recyclerView;
    private BeritaAdapter adapter;

    public BeritaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_berita, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.list_item);

//        progressDoalog = new ProgressDialog(getContext());
//        progressDoalog.setMessage("Loading....");
//        progressDoalog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ApiModel> call = service.getAllBerita();
        call.enqueue(new Callback<ApiModel>() {
            @Override
            public void onResponse(Call<ApiModel> call, Response<ApiModel> response) {
//                progressDoalog.dismiss();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<ApiModel> call, Throwable t) {
//                progressDoalog.dismiss();
                Toast.makeText(getContext(), "Data tidak bisa diambil", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void generateDataList(ApiModel photoList) {
        adapter = new BeritaAdapter((ArrayList<Berita>) photoList.articles, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
