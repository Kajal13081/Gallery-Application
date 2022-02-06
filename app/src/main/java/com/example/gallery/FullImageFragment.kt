package com.example.gallery

import android.Manifest
import android.app.ActionBar
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.gallery.databinding.FragmentFullImageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class FullImageFragment : Fragment() {
    private lateinit var binding: FragmentFullImageBinding
    private var position = 0
    private lateinit var imageLink: String
    private var barsHidden : Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_full_image, container, false
        )
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PackageManager.PERMISSION_GRANTED
        )

        // hiding bottom navigation bar
        (activity as MainActivity).hideBottomNavBar(true)

        val args =
            FullImageFragmentArgs.fromBundle(requireArguments()) // passing above values using safe args
        position = args.pos
        imageLink = args.img

        binding.fullimageViewid.setImageURI(Uri.parse(imageLink))

        binding.bottomNavigation.setOnItemSelectedListener {
            val intent = Intent(Intent.ACTION_SEND)

            val file = File(Uri.parse(imageLink).path)
            val uri = FileProvider.getUriForFile(requireContext(),
                "com.codepath.fileprovider",
                file)
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            intent.putExtra(Intent.EXTRA_STREAM,uri )
            //                // adding text to share
            //                // Add subject Here
            intent.putExtra(Intent.EXTRA_SUBJECT, "Shared using Snap!!")
            //                // setting type to image
            intent.type = "image/*"
            //                // calling startActivity() to share
            startActivity(Intent.createChooser(intent, "Share Via"))
            true
        }

        val bottomBar = binding.bottomNavigation
        val fullImage = binding.fullimageViewid

        // OnClickListener for the image
        fullImage.setOnClickListener {
            // If bars are already hidden then make them appear
            if(barsHidden)
            {
                bottomBar.animate().translationY(0F)

               //as Flag is deprecated
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    activity?.window?.let { WindowCompat.setDecorFitsSystemWindows(it, false) }
                    val windowInsetsController =
                        activity?.window?.let { ViewCompat.getWindowInsetsController(it.decorView) }
                            ?: return@setOnClickListener
                    // Hide both the status bar and the navigation bar
                    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                }
                else
                (activity as MainActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

                barsHidden = false
            }

            // If bars are not hidden then hide them
            else
            {
                bottomBar.animate().translationY(bottomBar.getHeight().toFloat())

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    activity?.window?.let {
                        WindowCompat.setDecorFitsSystemWindows(it,true)
                    }
                    val windowInsetsController =
                        activity?.window?.let { ViewCompat.getWindowInsetsController(it.decorView) } ?:return@setOnClickListener
                    // show both the status bar and the navigation bar
                    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())

                }
                else
                (activity as MainActivity).window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

                barsHidden = true;
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }


}