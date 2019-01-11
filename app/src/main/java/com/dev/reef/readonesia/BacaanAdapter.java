package com.dev.reef.readonesia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BacaanAdapter extends RecyclerView.Adapter<BacaanViewHolder> {
    private ArrayList<Bacaan> listBacaan;
    private Context context;

    public BacaanAdapter(ArrayList<Bacaan> listBacaan, Context context) {
        this.listBacaan = listBacaan;
        this.context = context;
    }

    @NonNull
    @Override
    public BacaanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beranda_list_item, parent, false);
        return new BacaanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BacaanViewHolder holder, int position) {
        holder.bind(listBacaan.get(position), context);
    }

    @Override
    public int getItemCount() {
        return listBacaan.size();
    }
}
