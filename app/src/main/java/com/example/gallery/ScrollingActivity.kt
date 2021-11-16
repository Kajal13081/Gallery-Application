package com.example.gallery

import android.Manifest
import com.example.gallery.ImagesGallery.listOfImage
import com.example.gallery.AlbumConverter.getAlbum
import com.example.gallery.PhotosActivity.Companion.start
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gallery.AlbumRecyclerViewAdapter.AlbumListener
import com.example.gallery.PhotosRecyclerViewAdapter.PhotoListener
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import android.widget.Toast
import com.example.gallery.databinding.ScrollingActivityBinding
import java.util.ArrayList

class ScrollingActivity : AppCompatActivity() {
    private lateinit var binding: ScrollingActivityBinding
    private lateinit var photosRecyclerViewAdapter: PhotosRecyclerViewAdapter
    private lateinit var albumRecyclerViewAdapter: AlbumRecyclerViewAdapter
    private lateinit var images: List<String>

    private val My_READ_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.scrolling_activity)

        // dummy data
//        val imgs = intArrayOf(
//            R.drawable.camera,
//            R.drawable.discord,
//            R.drawable.download,
//            R.drawable.screenshots,
//            R.drawable.telegram_images,
//            R.drawable.whatsapp_images
//        )
//        val desc = arrayOf(
//            "Camera",
//            "Discord Images",
//            "Downloads",
//            "ScreenShots",
//            "Telegram Images",
//            "Whatsapp Images"
//        )
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                My_READ_PERMISSION_CODE
            )
        } else {
            loadImages()
        }

//        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
//        binding.photosRecyclerView.setLayoutManager(gridLayoutManager);
//        photosRecyclerViewAdapter = new PhotosRecyclerViewAdapter(imgs);
//        binding.photosRecyclerView.setAdapter(photosRecyclerViewAdapter);
    }

    private fun loadImages() {
        binding.photosRecyclerView.setHasFixedSize(true)
        val gridLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 3)
        binding.photosRecyclerView.layoutManager = gridLayoutManager
        images = listOfImage(this) // remains to fix -> Images Gallery
        val linearLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.albumRecyclerView.layoutManager = linearLayoutManager
        albumRecyclerViewAdapter =
            AlbumRecyclerViewAdapter(getAlbum(images), object : AlbumListener {
                override fun onAlbumClick(list: List<String>) {
                    val bundle = Bundle()
                    bundle.putStringArrayList("photos", ArrayList(list))
                    start(this@ScrollingActivity, bundle)
                }
            })
        binding.albumRecyclerView.adapter = albumRecyclerViewAdapter
        photosRecyclerViewAdapter = PhotosRecyclerViewAdapter(this, images, object : PhotoListener {
            // remains to fix-> Adapter
            override fun onPhotoClick(position: Int) {
                //Do something with photo
//                Toast.makeText(getApplicationContext(), "" + path, Toast.LENGTH_SHORT).show();
                val intent = Intent(applicationContext, FullImageActivity::class.java)
                intent.putExtra("image", images.get(position))
                intent.putExtra("pos", position)
                startActivity(intent)
            }
        })
        binding.photosRecyclerView.adapter = photosRecyclerViewAdapter

//        gallery_number.setText("Photos(" + images.size() + ")");
    }

    //    @Override
    //    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //
    //        if (requestCode == My_READ_PERMISSION_CODE){
    //            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
    //                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
    //            }else {
    //                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
    //            }
    //        }
    //    }
    private val mPermissionResult = registerForActivityResult(
        RequestPermission()
    ) { result ->
        if (result) {
//                        Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
            Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
//                        Log.e(TAG, "onActivityResult: PERMISSION DENIED");
            Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
}