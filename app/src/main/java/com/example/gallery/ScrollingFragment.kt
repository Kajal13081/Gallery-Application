

package com.example.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.databinding.FragmentScrollingBinding
import com.google.android.material.snackbar.Snackbar


class ScrollingFragment : Fragment() {

    private lateinit var binding: FragmentScrollingBinding
    private lateinit var photosRecyclerViewAdapter: PhotosRecyclerViewAdapter
    private lateinit var albumRecyclerViewAdapter: AlbumRecyclerViewAdapter
    private lateinit var images: List<String>
issue#16
issue#16

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted->
            if(isGranted)
            {
                startCamera()
            }
            else{
                Snackbar.make(binding.root,
                    R.string.camera_permission_denied,
                    Snackbar.LENGTH_SHORT).show()
            }
        }

    private val My_READ_PERMISSION_CODE = 101
    private val CAMERA_PERMISSION_CODE = 993
 JWOC

JWOC

private val requestPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestPermission()){
        isGranted->
        if(isGranted)
        {
            startCamera()
        }
        else{
            Snackbar.make(binding.root,
            R.string.camera_permission_denied,
            Snackbar.LENGTH_SHORT).show()
        }
    }

    private val My_READ_PERMISSION_CODE = 101
    private val CAMERA_PERMISSION_CODE = 993
JWOC
 JWOC

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scrolling, container, false)

        binding.cameraFloatingButton.setOnClickListener {
            showCameraPreview()
        }

        return binding.root
    }

    private fun showCameraPreview() {

        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            startCamera()
        }
        else
            requestCameraPermission2()
    }
 issue#16

    private fun requestCameraPermission2() {

        if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
            Snackbar.make(binding.root,
                R.string.permission_required,
                Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok){
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }.show()
        }else
        {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

    }



    private fun requestCameraPermission2() {

        if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
            Snackbar.make(binding.root,
            R.string.permission_required,
            Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok){
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }.show()
        }else
        {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

    }

 JWOC

    private val requestReadPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted->
            if(isGranted)
            {
                loadImages()
            }
            else{
                Snackbar.make(binding.root,
                    R.string.read_permission_denied,
                    Snackbar.LENGTH_SHORT).show()
            }
        }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showImages()
issue#16
    }

    private fun showImages() {
        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            loadImages()
        }
        else
            requestReadPermission()

    }

    private fun requestReadPermission() {
        if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
            Snackbar.make(binding.root,
                R.string.permission_required,
                Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok){
                requestReadPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }.show()
        }else
        {
            requestReadPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }


    }


    }

    private fun showImages() {
        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            loadImages()
        }
        else
            requestReadPermission()

    }

    private fun requestReadPermission() {
        if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
            Snackbar.make(binding.root,
                R.string.permission_required,
                Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok){
                requestReadPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }.show()
        }else
        {
            requestReadPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }


    }

JWOC
    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Check if there exists an Activity to handle the intent, if yes, then send the intent for opening camera
        if (activity?.let { it1 -> intent.resolveActivity(it1.packageManager) } != null) {
            startActivity(intent)
        }
    }



    private fun loadImages() {

        Log.i("myTag","This method has been called after permission enabled")
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



}