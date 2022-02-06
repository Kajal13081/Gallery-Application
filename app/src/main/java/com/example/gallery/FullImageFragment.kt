package com.example.gallery

import android.Manifest
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
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
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

    private var fileUri : Uri? = null

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


//        val bundle = requireActivity().intent.extras        // cant use intent directly in fragment
//        if (bundle != null) {
//            position = bundle.getInt("pos", 0)
//            imageLink = bundle.getString("image").toString()
//        }

        // hiding bottom navigation bar
        (activity as MainActivity).hideBottomNavBar(true)

        val args =
            FullImageFragmentArgs.fromBundle(requireArguments()) // passing above values using safe args
        position = args.pos
        imageLink = args.img


        binding.fullimageViewid.setImageURI(Uri.parse(imageLink))

        binding.bottomNavigation.setOnItemSelectedListener {
            val intent = Intent(Intent.ACTION_SEND)

            // putting uri of image to be shared
            //this image link must be accessible by other apps( see permission in AndroidManifest.xml inside provider tag)
/*
Explanation of Coding...
*       you have selected a file from media Gallery
*You have a Uri from that file.
* But when you want to launch share intent, you have to pass a FileProvider Uri into the intent.
* But you can’t use the file Uri you have from Media folder. It is not under your app authority
* and it is not under your app’s file path. If you want to open the file, you have to copy the
*  file under your app’s file path. And get that copy file Uri with FileProvider and pass it to the intent
*
 */



            intent.putExtra(Intent.EXTRA_STREAM,fileUri )
            //                // adding text to share
            //                // Add subject Here
            intent.putExtra(Intent.EXTRA_SUBJECT, "Add Subject")
            //                // setting type to image
            intent.type = "image/*"
            //                // calling startActivity() to share
            startActivity(Intent.createChooser(intent, "Share Via"))
            true
        }
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()

        //Once the fragment gets destroyed , we do not need the file created earlier using method getBitmapFromDrawable()
        //So, removing the file (We don't wanna increase app's user data storage for every share by user)
        if (fileUri != null) {
            context?.contentResolver?.delete(fileUri!!,null,null)
        }

    }



    //As we need to give external app permission to access our uri, we need to create a local file and get the uri from there .
    /**
     * This method extract bitmap from imageview and return Uri accessible by any app.
     * We are saving files to (/Android/data/[packagename]/files)
     * Storage Call : Context.getExternalFilesDir() = data can be read/write by the app,
     * any apps granted with READ_STORAGE permission can read too, deleted when uninstalled (/Android/data/[packagename]/files)
     * @param imageview imageview from which we need to extract bitmap
     * @return Uri uri of the image extracted from imageview
     */
    private fun getBitmapFromDrawable(imageview: ImageView): Uri? {

        //get the drawable
        val drawable = imageview.drawable

        //Extract Bitmap from Imageview drawable
        var bmp: Bitmap? = if (drawable is BitmapDrawable) {
            (imageview.getDrawable() as BitmapDrawable).bitmap
        } else {
            Log.d("myTag","Drawable is null")
            return null
        }

        //Store image to default external storage directory
        var bmpUri : Uri? = null

        try{
            val file = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png")
            val out = FileOutputStream(file)


            bmp?.compress(Bitmap.CompressFormat.JPEG, 80, out)
            out.close()

            //as per documentation, Uri.fromFile(file) will fail for api>=24, using file provider instead
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ) {
                bmpUri = FileProvider.getUriForFile(requireActivity(),
                    "com.codepath.fileprovider", file)
            }
            else
            {
                bmpUri = Uri.fromFile(file)
            }
        }catch (e : IOException){
            e.message
        }
        return bmpUri
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            fileUri = getBitmapFromDrawable(binding.fullimageViewid)
        }

    }
}