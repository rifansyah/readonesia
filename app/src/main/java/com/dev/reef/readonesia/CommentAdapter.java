package com.dev.reef.readonesia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private ArrayList<Comment> listComment;
    private Context context;

    public CommentAdapter(ArrayList<Comment> listComment, Context context) {
        this.listComment = listComment;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bind(listComment.get(position), context);
    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }
}

class CommentViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageCommentProfile;
    private TextView name;
    private TextView commentView;

    public CommentViewHolder(View itemView) {
        super(itemView);
        imageCommentProfile = (ImageView) itemView.findViewById(R.id.image_comment);
        name = (TextView) itemView.findViewById(R.id.name);
        commentView = (TextView) itemView.findViewById(R.id.comment);
    }

    public void bind(final Comment comment, final Context context) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.placeholder);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").child(comment.getIdCommenter()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                options.placeholder(R.drawable.placeholder);

                try {
                    Glide.with(context)
                            .load(dataSnapshot.child("imageUrl").getValue().toString())
                            .apply(options)
                            .into(imageCommentProfile);
                } catch (Exception e) {

                }

                name.setText(dataSnapshot.child("name").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        commentView.setText(comment.getComment());
    }
}
