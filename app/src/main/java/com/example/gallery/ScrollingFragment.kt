package com.example.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
    private val CAMERA_PERMISSION_CODE = 993
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

        // Camera button for opening camera
        val cameraButton = binding.cameraFloatingButton

        // OnClickListener for the camera button
        cameraButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            // Check if permissions have been granted, if not, then request permissions
            if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(Manifest.permission.CAMERA),CAMERA_PERMISSION_CODE)
                return@setOnClickListener
            }

            // Check if there exists an Activity to handle the intent, if yes, then send the intent for opening camera
            if(activity?.let { it1 -> intent.resolveActivity(it1.packageManager) } !=null)
            {
                startActivity(intent)
            }
        }

        return binding.root
    }

    private fun loadImages() {
        binding.photosRecyclerView.setHasFixedSize(true)
        val gridLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 3)
        binding.photosRecyclerView.layoutManager = gridLayoutManager
        images = ImagesGallery.listOfImage(requireContext())
        val linearLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.albumRecyclerView.layoutManager = linearLayoutManager
        albumRecyclerViewAdapter =
            AlbumRecyclerViewAdapter(AlbumConverter.getAlbum(images), object :
                AlbumRecyclerViewAdapter.AlbumListener {
                override fun onAlbumClick(list: List<String>) {
//                    val bundle = Bundle()
//                    bundle.putStringArrayList("photos", ArrayList(list))
//                    PhotosActivity.start(requireContext(), bundle)
                    findNavController().navigate(ScrollingFragmentDirections.actionScrollingFragmentToPhotosFragment(
                        list.toTypedArray()
                    ))
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            // Check if camera permissions were requested and if they have been granted now
            if(requestCode==CAMERA_PERMISSION_CODE && ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
            {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                // Check if there exists an Activity to handle the intent, if yes, then send the intent for opening camera
                if(activity?.let { intent.resolveActivity(it.packageManager) } !=null)
                {
                    startActivity(intent)
                }
            }
        }
    }
}