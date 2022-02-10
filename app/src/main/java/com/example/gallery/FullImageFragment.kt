package com.example.gallery

import android.Manifest
import android.content.pm.PackageManager
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

        val list= context?.let { ImagesGallery.listOfSortedImages(it,ImagesGallery.SortOrder.Date) }
        var currPosition= list?.indexOf(imageLink)

        var adapter= list?.let { currPosition?.let { it1 -> FullImageAdapter(context, it,imageLink) } }
        // Inflate the layout for this fragment
        binding.viewPager.adapter= adapter

        if (list != null) {
            binding.viewPager.setCurrentItem(list.indexOf(imageLink))
        }

        return binding.root
    }

}
