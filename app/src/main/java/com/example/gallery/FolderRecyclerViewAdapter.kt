package com.example.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery.databinding.AlbumViewBinding
import com.example.gallery.databinding.FolderViewBinding
import com.example.gallery.databinding.PhotoViewBinding

class FolderRecyclerViewAdapter(
    private val list: List<Pair<String, List<String>>>,
    private val albumListener: AlbumListener,
) : RecyclerView.Adapter<FolderRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: FolderViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.folder_view, parent, false
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
        val binding: FolderViewBinding,
        private val albumListener: AlbumListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<String, List<String>>) {
            val (title, list) = item

            Glide.with(itemView)
                .load(list.first())
                .into(binding.ivFolderView)

            binding.tvTextFolder.text = albumText(title)

            binding.root.setOnClickListener {
                albumListener.onAlbumClick(list)
            }
        }

        private fun albumText(str: String): String {
            var txt = ""
            var i = str.length-1
            while (i >= 0) {
                if (!(str[i].equals('/'))) {
                    txt += str[i]
                } else {
                    i = -1
                }
                i--
            }
            return txt.reversed()
        }
    }

    interface AlbumListener {
        fun onAlbumClick(list: List<String>)
    }
}