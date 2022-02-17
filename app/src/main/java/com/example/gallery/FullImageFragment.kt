package com.example.gallery

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.example.gallery.databinding.FragmentFullImageBinding
import java.io.File
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import kotlin.collections.HashMap


class FullImageFragment : Fragment() {
    private lateinit var binding: FragmentFullImageBinding
    private var position = 0
    private lateinit var imageLink: String
    private var barsHidden : Boolean = false

    private var favoritePic : Boolean = false
    private lateinit var externalUri: Uri
    private lateinit var resolver: ContentResolver
    private var mediaId : Long = 0L
    private lateinit var contentUri : Uri
    private lateinit var menuItem: MenuItem


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
            when (it.itemId) {
                R.id.nav_share -> {
                    val intent = Intent(Intent.ACTION_SEND)

                    val file = File(Uri.parse(imageLink).path)
                    val uri = FileProvider.getUriForFile(requireContext(),
                        "com.codepath.fileprovider",
                        file)
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

                    intent.putExtra(Intent.EXTRA_STREAM,uri )
                    //                // adding text to share
                    //                // Add subject Here
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Shared using Snap!!")
                    //                // setting type to image
                    intent.type = "image/*"
                    //                // calling startActivity() to share
                    startActivity(Intent.createChooser(intent, "Share Via"))
                }
                R.id.delete -> {
                    DeletePhotoDialog.create(imageLink).show((activity as MainActivity).supportFragmentManager,"DELETE_IMAGE")

                }
                R.id.Info -> {
                    findNavController().navigate(FullImageFragmentDirections.actionFullImageFragmentToImageDescriptionFragment2(imageLink))

                }
                R.id.upload_image->{
                    uploadToFirebase(imageLink,it)
                    it.setIcon(R.drawable.ic_baseline_cloud_done_24)
                }
                R.id.favorite_selector ->
                {
                    setFavorite()
                }

            }
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
                bottomBar.animate().translationY(bottomBar.height.toFloat())

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



    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        resolver = requireContext().contentResolver
        mediaId = ImageUtil.getFilePathToMediaID(imageLink,requireContext())
        contentUri = ContentUris.withAppendedId(externalUri,mediaId)
        menuItem =  binding.bottomNavigation.menu.findItem(R.id.favorite_selector)


        if(isFavorite(resolver,contentUri))
        {
            favoritePic = true
            menuItem.setIcon(R.drawable.ic_baseline_favorite_24)
        }else
        {
            favoritePic = false
            menuItem.setIcon(R.drawable.ic_baseline_favorite_border_24)
        }

    }

    /**
     *This [setFavorite] method set the isFavorite Column in media store to True if it initially False and vice Versa.
     *
     */
    private fun setFavorite()  {

        //Get the current state of the pic , either favorite or not
        val state = favoritePic

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val pendingIntent =
                MediaStore.createFavoriteRequest(resolver, arrayListOf(contentUri), !state)
            startIntentSenderForResult(pendingIntent.intentSender,
                FAVORITE_REQUEST_CODE, null, 0, 0, 0, null)
        }
        //else Is_Favorite Feature is not available below api level 11 , so do nothing or we can show user that this feature is not available
        else{
            Toast.makeText(requireContext(),getString(R.string.feature_not_available),Toast.LENGTH_SHORT).show()
        }

    }


    /*
    *Check for result
     */

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== FAVORITE_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            favoritePic = isFavorite(resolver,contentUri)
        }
        if(favoritePic)
        {
            menuItem.setIcon(R.drawable.ic_baseline_favorite_24)
        }else
            menuItem.setIcon(R.drawable.ic_baseline_favorite_border_24)

    }


    /**
     * This method [isFavorite] checks for the image is marked favorite by user or not
     * @param[resolver] is a contentResolver
     * @param[contentUri] is a uri of the image of which we need to check
     * @return[Boolean] returns whether image is favorite or not
     * * IS_FAVORITE column on Media is available for api level 30 and above, so the annotation
     */

    @RequiresApi(Build.VERSION_CODES.R)
    private fun isFavorite (
        resolver: ContentResolver,
        contentUri: Uri,
    ) : Boolean {

        var flag = false
        val selection = MediaStore.MediaColumns.IS_FAVORITE + " =?"
        val selectionArgs = arrayOf("1")
        val cursor = resolver.query(contentUri, null, selection, selectionArgs, null)

        cursor?.use {
            if (it.count !=0 && it.count==1) {
                val columnFavorite =
                    it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.IS_FAVORITE)
                while (it.moveToNext()) {
                    val fav = it.getInt(columnFavorite)
                    if(fav ==1)
                        flag= true
                }
            }
        }
        return flag
    }

    companion object{
        const val FAVORITE_REQUEST_CODE = 1001
    }



    /**
     * Using Firebase Storage
     * This method upload the file to firebase storage.
     * @param passedUri -> image's Uri as String
     *
     */
    private fun uploadToFirebase(passedUri : String, cloudItem: MenuItem){

        binding.progressBar.visibility = View.VISIBLE

        //making File , as it is already added File will not create it.
        val file = File(Uri.parse(passedUri).path)

        //using provider to get the Uri in the format "content://...." necessary for firebase

        val fileUri = FileProvider.getUriForFile(
            requireContext(),
            "com.codepath.fileprovider",
            file)


        //getting the mimeType
        val mimeType = requireActivity().contentResolver.getType(fileUri)


        val metadata = storageMetadata {
            contentType = mimeType
        }

        val storage = Firebase.storage
        val storageRef = storage.reference

        val ref : StorageReference = storageRef.child("upload/"+ System.currentTimeMillis().toString())

        //uploading file with metadata
        val uploadTask = ref.putFile(fileUri,metadata)

        uploadTask.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.progressBar.visibility = View.GONE
                cloudItem.setIcon(R.drawable.ic_baseline_cloud_done_24)
                Snackbar.make(binding.root,getString(R.string.upload_done),Snackbar.LENGTH_LONG)
                    .setAction(R.string.saveUrl){
                        saveUploadedImageUri(task.result.toString())
                    }
                    .show()


            } else {
                Log.d("myTag","Failed uploading ")
                // Handle failures
            }


        }?.addOnFailureListener{
            Toast.makeText(requireContext(),getString(R.string.file_upload_failed),Toast.LENGTH_SHORT).show()
            Log.d("myTag","It failed because ${it.message} \n ${it.cause}")
            cloudItem.setIcon(R.drawable.ic_baseline_cloud_upload_24)

        }


    }

    //optionally letting user to store the reference in firebase fireStore
    private fun saveUploadedImageUri(firebaseUri : String){

        val dataToSave = HashMap<String,String>()
        dataToSave["imageUrl"] = firebaseUri

        val db = Firebase.firestore

        db.collection("uploads")
            .add(dataToSave)
            .addOnSuccessListener {
                docRef->
            Toast.makeText(requireContext(),getString(R.string.ref_store),Toast.LENGTH_LONG).show()
            }

            .addOnFailureListener{
                Toast.makeText(requireContext(),getString(R.string.ref_store_failed),Toast.LENGTH_LONG).show()

            }

    }

//function to verify if file is already uploaded or not , if it already uploaded item icon will get change.
    /***
     *      ......    in progress....
     */


}

