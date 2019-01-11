package com.dev.reef.readonesia.Beradu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dev.reef.readonesia.Bacaan;
import com.dev.reef.readonesia.R;

import java.util.ArrayList;

public class GamePostAdapter extends RecyclerView.Adapter<GamePostViewHolder> {
    private ArrayList<Bacaan> listBacaan;
    private Context context;
    private LinearLayout loading;

    public GamePostAdapter(ArrayList<Bacaan> listBacaan, Context context, LinearLayout loading) {
        this.context = context;
        this.listBacaan = listBacaan;
        this.loading = loading;
    }

    @NonNull
    @Override
    public GamePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beranda_list_item, parent, false);
        return new GamePostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GamePostViewHolder holder, int position) {
        holder.bind(listBacaan.get(position), context, loading);
    }

    @Override
    public int getItemCount() {
        return listBacaan.size();
    }
}
