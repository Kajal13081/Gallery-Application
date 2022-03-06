package com.example.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.databinding.FragmentPhotosBinding
import com.example.gallery.model.ImageAndVideoData

class PhotosFragment : Fragment() {
    private lateinit var binding: FragmentPhotosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photos, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()
        val args = PhotosFragmentArgs.fromBundle(requireArguments())
        val lst1 = args.lst



        // hiding bottom navigation bar
        (activity as MainActivity).hideBottomNavBar(true)

        binding.photosRecyclerView.setHasFixedSize(true)
        val gridLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 3)
        binding.photosRecyclerView.layoutManager = gridLayoutManager

        val mediaList = ImagesGallery.getMedia(requireContext(),ImagesGallery.SortOrder.Modified)
        //creating  new List
        val newMediaList = mutableListOf<ImageAndVideoData>()
        mediaList.forEach { media->
            lst1.forEach {
                    imagelink->
                if(media.data==imagelink)
                {
                    newMediaList.add(media)
                }
            }
        }

        binding.back.setOnClickListener { requireActivity().onBackPressed() }

        val adapter =    PhotosRecyclerViewAdapter(newMediaList ,object :
            PhotosRecyclerViewAdapter.PhotoListener {
            override fun onPhotoClick(position: Int) {
                findNavController().navigate(
                    PhotosFragmentDirections.actionPhotosFragmentToFullImageFragment(
                        position,
                        newMediaList[position].data
                    )
                )
            }
        })
        binding.photosRecyclerView.adapter = adapter
        // Inflate the layout for this fragment
        return binding.root
    }
}