package com.dev.reef.readonesia.beranda;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.reef.readonesia.R;

import java.util.ArrayList;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaViewHolder> {
    ArrayList<Berita> listBerita;
    Context context;

    public BeritaAdapter(ArrayList<Berita> listBerita, Context context) {
        this.listBerita = listBerita;
        this.context = context;
    }

    @NonNull
    @Override
    public BeritaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beranda_list_item, parent, false);
        return new BeritaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BeritaViewHolder holder, int position) {
        holder.bind(listBerita.get(position), context);
    }

    @Override
    public int getItemCount() {
        return listBerita.size();
    }
}
