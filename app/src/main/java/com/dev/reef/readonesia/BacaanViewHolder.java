package com.dev.reef.readonesia;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

public class BacaanViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView description;
    TextView author;
    TextView category;
    TextView date;
    ImageView photo;
    ConstraintLayout layout;

    public BacaanViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        author = (TextView) itemView.findViewById(R.id.author);
        category = (TextView) itemView.findViewById(R.id.category);
        date = (TextView) itemView.findViewById(R.id.date);
        photo = (ImageView) itemView.findViewById(R.id.image);
        layout = (ConstraintLayout) itemView.findViewById(R.id.layout_beranda_list_item);
    }

    public void bind(final Bacaan bacaan, final Context context) {
        title.setText(bacaan.getTitle());
        description.setText(bacaan.getDescription());
        author.setText(bacaan.getAuthor());
        category.setText(bacaan.getCategory());
        date.setText(bacaan.getPublishedAt());

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.placeholder);

        Glide.with(context)
                .load(bacaan.getUrlToImage())
                .apply(options)
                .into(photo);


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadActivity.class);
                intent.putExtra("id_poster", bacaan.getAuthorId());
                intent.putExtra("id_post", bacaan.getId());
                context.startActivity(intent);
            }
        });
    }
}
