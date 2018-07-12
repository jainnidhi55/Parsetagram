package com.example.nidhij1.parsetagram.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nidhij1.parsetagram.PostDetails;
import com.example.nidhij1.parsetagram.R;

import org.parceler.Parcels;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    List <Post> mPosts;
    Context context;


    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.activity_item_ig, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Post post = mPosts.get(position);

        //post.getImage().getUrl()

        //populate
        holder.descrip.setText(post.getDescription());
        Glide.with(context).load(post.getImage().getUrl())
                .into(holder.pic);
        holder.date.setText(post.getCreatedAt().toString());
        holder.usernam.setText(post.getUser().getUsername().toString());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView pic;
        public TextView descrip;
        public TextView date;
        public TextView usernam;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.iv_IGpic);
            descrip = itemView.findViewById(R.id.tv_IGdescrip);
            date = itemView.findViewById(R.id.iv_dateDetails);
            usernam = itemView.findViewById(R.id.tv_userName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = mPosts.get(position);
                //create the intent to display moviedetailsact
                Intent intent = new Intent(context, PostDetails.class);
                //pass the movie as an extra serialized via parcels.wrap
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));


                //show the activity
                context.startActivity(intent);
            }

        }
    }


}
