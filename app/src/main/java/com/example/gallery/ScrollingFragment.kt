package com.example.gallery

import android.Manifest
import android.R.attr.bitmap
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.databinding.FragmentScrollingBinding
import com.example.gallery.model.ImageAndVideoData
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.OutputStream
import java.util.*


class ScrollingFragment : Fragment() {
    private lateinit var binding: FragmentScrollingBinding
    private lateinit var photosRecyclerViewAdapter: PhotosRecyclerViewAdapter
    private lateinit var albumRecyclerViewAdapter: AlbumRecyclerViewAdapter
    private lateinit var images: List<String>
    private lateinit var mediaList : List<ImageAndVideoData>
    private val cameraRequestId = 1222

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scrolling, container, false)

        binding.cameraFloatingButton.setOnClickListener {
            showCameraPreview()
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_menu, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }*/

    private fun showCameraPreview() {

        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            startCamera()
        }
        else
            requestCameraPermission2()
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


    private val requestReadPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted->
            if(isGranted)
            {
                loadImages(ImagesGallery.SortOrder.Date)

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
    }

    private fun showImages() {
        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            loadImages(ImagesGallery.SortOrder.Modified)
            //    ImagesGallery.getImages(requireContext(),ImagesGallery.SortOrder.Date)
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

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Check if there exists an Activity to handle the intent, if yes, then send the intent for opening camera
        if (activity?.let { it1 -> intent.resolveActivity(it1.packageManager) } != null) {
            startActivityForResult(intent, cameraRequestId)
        }
    }

    override fun onActivityResult (requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestId) {
            /**save to Image In layout*/
            val images: Bitmap = data?.extras?.get("data") as Bitmap
            // myImage.setImageBitmap(images)


            val fos: OutputStream

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val resolver: ContentResolver = requireContext().getContentResolver()
                    val contentValues = ContentValues()
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + ".jpg")
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    contentValues.put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + File.separator.toString() + "TestFolder"
                    )
                    val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    fos = Objects.requireNonNull(imageUri)?.let { resolver.openOutputStream(it) }!!
                    images.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    Objects.requireNonNull(fos)
                    Toast.makeText(context, "Image Saved", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Image not saved \n$e", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun loadImages(sortOrder: ImagesGallery.SortOrder) {

        binding.photosRecyclerView.setHasFixedSize(true)

        images = ImagesGallery.listOfSortedImages(requireContext(),sortOrder)

        //loading media both image and video for photo fragment (Change this fragment name now, as it contains both media)
        mediaList =    ImagesGallery.getMedia(requireContext(),ImagesGallery.SortOrder.Date)

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

        photosRecyclerViewAdapter = PhotosRecyclerViewAdapter( mediaList,object :
            PhotosRecyclerViewAdapter.PhotoListener {
            override fun onPhotoClick(position: Int) {
                findNavController().navigate(
                    ScrollingFragmentDirections.actionScrollingFragmentToFullImageFragment(position,
                        mediaList[position].data
                    )
                )
            }
        })
        binding.photosRecyclerView.adapter = photosRecyclerViewAdapter
        val gridLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 3)
        binding.photosRecyclerView.layoutManager = gridLayoutManager


    }


}