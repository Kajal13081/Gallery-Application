package com.example.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.gallery.databinding.FragmentFullImageBinding
import com.example.gallery.databinding.FullImageItemLayoutBinding


class FullImageFragment : Fragment() {
    private lateinit var binding: FragmentFullImageBinding
   // private lateinit var binding1:FullImageItemLayoutBinding


    private var position = 0
    private lateinit var imageLink: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_full_image, container, false
        )
       /* binding1 = DataBindingUtil.inflate(
                inflater, R.layout.full_image_item_layout, container, false
        )*/

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

        binding.bottomNavigation.setOnItemSelectedListener {
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
             true
        }
        val list= context?.let { ImagesGallery.listOfImage(it) }
        var currPosition= list?.indexOf(imageLink)

        var adapter= list?.let { currPosition?.let { it1 -> FullImageAdapter(context, it) } }
        // Inflate the layout for this fragment
         binding.viewPager.adapter= adapter

        if (list != null) {
            binding.viewPager.setCurrentItem(list.indexOf(imageLink))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //binding1.fullimageViewid.setImageURI(Uri.parse(imageLink))
       //

        }


    }
