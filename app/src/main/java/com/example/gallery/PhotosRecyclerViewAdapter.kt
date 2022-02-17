package com.example.gallery

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery.databinding.PhotoViewBinding
import com.example.gallery.databinding.VideoViewBinding
import com.example.gallery.model.ImageAndVideoData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem


const val ITEM_VIEW_TYPE_IMAGE = 101
const val ITEM_VIEW_TYPE_VIDEO = 111


class PhotosRecyclerViewAdapter(
    dataList: List<ImageAndVideoData>,
    private var photoListener:PhotoListener)
    : ListAdapter<ImageAndVideoData, RecyclerView.ViewHolder>(DiffCallBack) {


    //to hold the list
    private  var items : List<ImageAndVideoData> = dataList


    override fun getItemCount(): Int {
        return   items.size
    }

    interface PhotoListener {
        fun onPhotoClick(position: Int)
    }

    //Create view depending upon view Type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_IMAGE ->{
                ImageViewHolder.from(parent)
            }
            ITEM_VIEW_TYPE_VIDEO -> {
                VideoViewHolder.from(parent)
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }

    }

    //We need this method to distinguish between two different types
    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    //returns item at particular position
    override fun getItem(position: Int): ImageAndVideoData {
        return items[position]
    }


    //Bind the View  according to its view type
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ImageViewHolder -> {
                val imageItem = getItem(position)
                //calling function here to improve readability.
                holder.bind(imageItem)
                //binding listener
                holder.itemView.setOnClickListener {
                    photoListener.onPhotoClick(holder.adapterPosition)
                }
            }
            is VideoViewHolder -> {
                val videoItem = getItem(position)
                holder.bind(videoItem)

                //binding listener
                holder.itemView.setOnClickListener {
                    photoListener.onPhotoClick(holder.adapterPosition)
                }

            }
            else ->{
                Log.e("myTag","Holder type is unrecognized $holder")
            }
        }

    }


    //First View Type - IMAGE
    class ImageViewHolder private constructor(val context: Context,val binding: PhotoViewBinding) : RecyclerView.ViewHolder(
        binding.root
    ){
        fun bind(item: ImageAndVideoData){

            //handling loading of images using glide to increase the recyclerview performance.
            Glide.with(context).load(item.uri).into(binding.ivPhotosView)


            /*    binding.ivPhotosView.setImageURI(item.uri)
              -> this line of code was degrading the performance of recyclerview
              * */

            //update ui immediately
            binding.executePendingBindings()

        }

        companion object{
            fun from(parent: ViewGroup):ImageViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PhotoViewBinding.inflate(layoutInflater,parent,false)
                return ImageViewHolder(parent.context,binding)
            }
        }
    }


    //Second View Type - VIDEO
    class VideoViewHolder private constructor(val context: Context, val binding: VideoViewBinding) : RecyclerView.ViewHolder(
        binding.root
    ){

        //using Exoplayer instead of VideoView
        fun bind(item : ImageAndVideoData){
            //initializing exo player
            val player=  ExoPlayer.Builder(context).build().also {
                binding.myVideoView.player = it
            }

            //get the media Item
            val mediaItem = MediaItem.fromUri(item.uri)

            //setting media item to exo player initialized earlier
            player.setMediaItem(mediaItem)

            //this line of code returns error if too many videos are present , so better to avoid prepare
           // player.prepare()
            player.playWhenReady=true
            val duration = player.duration
            player.seekTo(duration/2)

            //update UI immediately
            binding.executePendingBindings()
        }



        companion object{
            fun from(parent: ViewGroup): VideoViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = VideoViewBinding.inflate(layoutInflater,parent, false)

                return VideoViewHolder( parent.context,binding)
            }
        }
    }


    //this list holds previously played player's position
    private val myHashMap = HashMap<ImageAndVideoData,Long>()


    //Overriding this method to set the player current position to  position saved in hashmap.
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {

        when(holder) {
            is VideoViewHolder -> {
                if(myHashMap.isNotEmpty()){

                    //there may be no key present in the hashmap for the selected item , for that uses 1 as default position
                    val getLastPlayedPosition = myHashMap[getItem(holder.layoutPosition)]?:1
                    val player = holder.binding.myVideoView.player
                    player?.seekTo(getLastPlayedPosition)

                }

            }
        }
        super.onViewAttachedToWindow(holder)
    }


    //Overriding this method to get the player current position and saving in hashmap, later used by onViewAttachedToWindow() Method
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {

        when(holder){
            is VideoViewHolder ->
            {
                val player = holder.binding.myVideoView.player
                //if player is null, then setting current position to 1
                val currentPosition =  player?.currentPosition?: 1
                myHashMap[getItem(holder.layoutPosition)] = currentPosition
                player?.release()
            }
        }

        super.onViewDetachedFromWindow(holder)
    }
}



//This object class handles if something changed in the list.
object DiffCallBack :DiffUtil.ItemCallback<ImageAndVideoData>() {
    override fun areItemsTheSame(oldItem: ImageAndVideoData, newItem: ImageAndVideoData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImageAndVideoData, newItem: ImageAndVideoData): Boolean {
        return oldItem == newItem
    }

}







/*

class PhotosRecyclerViewAdapter(
    private val context: Context,
    private val images: List<String>,
    private var photoListener: PhotoListener
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
}*/
