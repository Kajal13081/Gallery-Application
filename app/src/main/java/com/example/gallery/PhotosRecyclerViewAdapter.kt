package com.example.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery.databinding.PhotoViewBinding

class PhotosRecyclerViewAdapter(
    private val context: Context,
    private val images: List<String>,
    protected var photoListener: PhotoListener
) : RecyclerView.Adapter<PhotosRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: PhotoViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.photo_view, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val image = images[position]
        Glide.with(context).load(image).into(holder.binding.ivPhotosView)
        holder.itemView.setOnClickListener { photoListener.onPhotoClick(holder.adapterPosition) }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class MyViewHolder(val binding: PhotoViewBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    interface PhotoListener {
        fun onPhotoClick(position: Int)
    }
}