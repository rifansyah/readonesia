package com.dev.reef.readonesia.beranda;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dev.reef.readonesia.Bacaan;
import com.dev.reef.readonesia.R;
import com.dev.reef.readonesia.ReadActivity;

public class BeritaViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView description;
    TextView author;
    TextView category;
    TextView date;
    ImageView photo;
    ConstraintLayout layout;

    public BeritaViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        author = (TextView) itemView.findViewById(R.id.author);
        category = (TextView) itemView.findViewById(R.id.category);
        date = (TextView) itemView.findViewById(R.id.date);
        photo = (ImageView) itemView.findViewById(R.id.image);
        layout = (ConstraintLayout) itemView.findViewById(R.id.layout_beranda_list_item);
    }

    public void bind(final Berita bacaan, final Context context) {
        title.setText(bacaan.getTitle());
        description.setText(bacaan.getDescription());
        author.setText(bacaan.getAuthor());
        date.setText(bacaan.getPublishedAt());

        category.setVisibility(View.GONE);

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.placeholder);

        try{
            Glide.with(context)
                    .load(bacaan.getUrlToImage())
                    .apply(options)
                    .into(photo);

        } catch (Exception e) {

        }

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BeritaWebViewActivity.class);
                intent.putExtra("url", bacaan.getUrl());
                context.startActivity(intent);
            }
        });
    }
}
