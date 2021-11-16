package com.example.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery.databinding.AlbumViewBinding

class AlbumRecyclerViewAdapter(
    private val list: List<Pair<String, List<String>>>,
    private val albumListener: AlbumListener,
) : RecyclerView.Adapter<AlbumRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: AlbumViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.album_view, parent, false
        )
        return MyViewHolder(binding, albumListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(
        val binding: AlbumViewBinding,
        private val albumListener: AlbumListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<String, List<String>>) {
            val (title, list) = item

            Glide.with(itemView)
                .load(list.first())
                .into(binding.ivPhotoAlbum)

            binding.tvTextAlbum.text = title

            binding.root.setOnClickListener {
                albumListener.onAlbumClick(list)
            }
        }
    }

    interface AlbumListener {
        fun onAlbumClick(list: List<String>)
    }
}