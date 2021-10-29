package com.example.gallery;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gallery.databinding.PhotoViewBinding;

import java.lang.reflect.Array;

public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.MyViewHolder> {

    private int imageId[];

    public PhotosRecyclerViewAdapter(int img[]) {
        imageId = img;
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
        holder.binding.ivPhotosView.setImageResource(imageId[position]);
    }


    @Override
    public int getItemCount() {
        return imageId.length;
    }

    public static final class MyViewHolder extends RecyclerView.ViewHolder {
        private final PhotoViewBinding binding;
        public MyViewHolder(@NonNull PhotoViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
