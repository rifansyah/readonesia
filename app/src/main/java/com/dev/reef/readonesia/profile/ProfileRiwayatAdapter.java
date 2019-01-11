package com.dev.reef.readonesia.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.reef.readonesia.R;

import java.util.ArrayList;

public class ProfileRiwayatAdapter extends RecyclerView.Adapter<ProfileRiwayatViewHolder> {
    private ArrayList<Riwayat> listRiwayat;
    private Context context;

    public ProfileRiwayatAdapter(ArrayList<Riwayat> listRiwayat, Context context) {
        this.listRiwayat = listRiwayat;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileRiwayatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat_profile, parent, false);
        return new ProfileRiwayatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRiwayatViewHolder holder, int position) {
        holder.bind(listRiwayat.get(position));
    }

    @Override
    public int getItemCount() {
        return listRiwayat.size();
    }
}
