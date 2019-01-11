package com.dev.reef.readonesia.profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dev.reef.readonesia.R;

public class ProfileRiwayatViewHolder extends RecyclerView.ViewHolder {
    TextView winOrLose;
    TextView oppositeName;
    TextView point;

    public ProfileRiwayatViewHolder(View itemView) {
        super(itemView);
        winOrLose = (TextView) itemView.findViewById(R.id.win_or_lose);
        oppositeName = (TextView) itemView.findViewById(R.id.opposite_name);
        point = (TextView) itemView.findViewById(R.id.point);
    }

    public void bind(Riwayat riwayat) {
        winOrLose.setText(riwayat.getStatus());

        oppositeName.setText(riwayat.getOppositeName());

        point.setText(Integer.toString(riwayat.getPoint()));
    }
}
