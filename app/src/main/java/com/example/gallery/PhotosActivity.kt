package com.example.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.PhotosRecyclerViewAdapter.PhotoListener
import com.example.gallery.databinding.ActivityPhotosBinding

class PhotosActivity : Activity() {

    companion object {
        @JvmStatic
        fun start(context: Context, bundle: Bundle) {
            val intent = Intent(context, PhotosActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    private val binding: ActivityPhotosBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_photos
        )
    }
    private val list by lazy { intent.extras?.getStringArrayList("photos") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.photosRecyclerView.setHasFixedSize(true)
        val gridLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 3)
        binding.photosRecyclerView.layoutManager = gridLayoutManager
        binding.photosRecyclerView.adapter =
            PhotosRecyclerViewAdapter(this, list?.toList()!!, object : PhotoListener {
                override fun onPhotoClick(position: Int) {
                    //Do something with photo
//                Toast.makeText(getApplicationContext(), "" + path, Toast.LENGTH_SHORT).show();
//                val intent = Intent(applicationContext, Full_Image_Activity::class.java)
//                intent.putExtra("image", images.get(position).toString())
//                intent.putExtra("pos", position)
//                startActivity(intent)
                }
            })

    }
}