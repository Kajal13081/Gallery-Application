package com.example.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.databinding.PhotoViewBinding;

import java.lang.reflect.Array;
import java.util.List;

public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<String> images;
    protected PhotoListener photoListener;

    public PhotosRecyclerViewAdapter(Context context,List<String> images,PhotoListener photoListener) {
        this.context = context;
        this.images = images;
        this.photoListener = photoListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PhotoViewBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.photo_view, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String image = images.get(position);
        Glide.with(context).load(image).into(holder.binding.ivPhotosView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoListener.onPhotoClick(holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return images.size();
    }

    public static final class MyViewHolder extends RecyclerView.ViewHolder {
        private final PhotoViewBinding binding;
        public MyViewHolder(@NonNull PhotoViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface PhotoListener{
        void onPhotoClick(int position);
    }
}

