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


class FullImageFragment : Fragment() {
    private lateinit var binding: FragmentFullImageBinding
    private var position = 0
    private lateinit var imageLink: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
        // Inflate the layout for this fragment
        return binding.root
    }
}