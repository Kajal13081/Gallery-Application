package com.example.gallery;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.databinding.AlbumViewBinding;
import com.example.gallery.databinding.PhotoViewBinding;

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.MyViewHolder> {

    private int imageId[];
    private String description[];

    public AlbumRecyclerViewAdapter(int img[], String des[]) {
        imageId = img;
        description = des;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AlbumViewBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.album_view, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.ivPhotoAlbum.setImageResource(imageId[position]);
        holder.binding.tvTextAlbum.setText(description[position]);
    }

    @Override
    public int getItemCount() {
        return imageId.length;
    }

    public static final class MyViewHolder extends RecyclerView.ViewHolder {
        private final AlbumViewBinding binding;
        public MyViewHolder(@NonNull AlbumViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
