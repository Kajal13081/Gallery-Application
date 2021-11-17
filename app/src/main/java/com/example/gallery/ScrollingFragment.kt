package com.example.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.databinding.FragmentScrollingBinding
import java.util.ArrayList


class ScrollingFragment : Fragment() {
    private lateinit var binding: FragmentScrollingBinding
    private lateinit var photosRecyclerViewAdapter: PhotosRecyclerViewAdapter
    private lateinit var albumRecyclerViewAdapter: AlbumRecyclerViewAdapter
    private lateinit var images: List<String>

    private val My_READ_PERMISSION_CODE = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scrolling, container, false)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                My_READ_PERMISSION_CODE
            )
        } else {
            loadImages()
        }
        return binding.root
    }

    private fun loadImages() {
        binding.photosRecyclerView.setHasFixedSize(true)
        val gridLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 3)
        binding.photosRecyclerView.layoutManager = gridLayoutManager
        images = ImagesGallery.listOfImage(this)
        val linearLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.albumRecyclerView.layoutManager = linearLayoutManager
        albumRecyclerViewAdapter =
            AlbumRecyclerViewAdapter(AlbumConverter.getAlbum(images), object :
                AlbumRecyclerViewAdapter.AlbumListener {
                override fun onAlbumClick(list: List<String>) {
                    val bundle = Bundle()
                    bundle.putStringArrayList("photos", ArrayList(list))
                    PhotosActivity.start(requireContext(), bundle)
                }
            })
        binding.albumRecyclerView.adapter = albumRecyclerViewAdapter
        photosRecyclerViewAdapter = PhotosRecyclerViewAdapter(requireContext(), images, object :
            PhotosRecyclerViewAdapter.PhotoListener {
            override fun onPhotoClick(position: Int) {
                findNavController().navigate(
                    ScrollingFragmentDirections.actionScrollingFragmentToFullImageFragment(position,
                        images[position]
                    )
                )
            }
        })
        binding.photosRecyclerView.adapter = photosRecyclerViewAdapter
    }

    private val mPermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
}