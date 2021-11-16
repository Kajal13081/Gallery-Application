package com.example.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.Manifest.permission
import android.content.pm.PackageManager
import android.os.StrictMode.VmPolicy
import android.os.StrictMode
import android.content.Intent
import android.net.Uri
import androidx.databinding.DataBindingUtil
import com.example.gallery.databinding.ActivityFullImageBinding

class FullImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullImageBinding
    private var position = 0
    private lateinit var imageLink: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_image)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE),
            PackageManager.PERMISSION_GRANTED
        )
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val bundle = intent.extras
        if (bundle != null) {
            position = bundle.getInt("pos", 0)
            imageLink = bundle.getString("image").toString()
        }
        binding.fullimageViewid.setImageURI(Uri.parse(imageLink))
        binding.button.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            //                // putting uri of image to be shared
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageLink))
            //                // adding text to share
            //                // Add subject Here
            intent.putExtra(Intent.EXTRA_SUBJECT, "Add Subject")
            //                // setting type to image
            intent.type = "image/*"
            //                // calling startActivity() to share
            startActivity(Intent.createChooser(intent, "Share Via"))
        }
    }
}