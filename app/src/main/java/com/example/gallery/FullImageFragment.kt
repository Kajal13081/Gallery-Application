package com.example.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.gallery.databinding.FragmentFullImageBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File
import java.util.*
import kotlin.collections.HashMap


class FullImageFragment : Fragment() {
    private lateinit var binding: FragmentFullImageBinding
    private var position = 0
    private lateinit var imageLink: String
    private var barsHidden : Boolean = false
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

            if(it.itemId== R.id.nav_share){
                val intent = Intent(Intent.ACTION_SEND)

                val file = File(Uri.parse(imageLink).path)
                val uri = FileProvider.getUriForFile(requireContext(),
                    "com.codepath.fileprovider",
                    file)
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                intent.putExtra(Intent.EXTRA_STREAM,uri )
                //                // adding text to share
                //                // Add subject Here
                intent.putExtra(Intent.EXTRA_SUBJECT, "Shared using Snap!!")
                //                // setting type to image
                intent.type = "image/*"
                //                // calling startActivity() to share
                startActivity(Intent.createChooser(intent, "Share Via"))

            }else if(it.itemId == R.id.upload_image){

                    uploadToFirebase(imageLink)
                    it.setIcon(R.drawable.ic_baseline_cloud_done_24)

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
                bottomBar.animate().translationY(bottomBar.getHeight().toFloat())

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


    /**
     * Using Firebase Storage
     * This method upload the file to firebase storage.
     * @param passedUri -> image's Uri as String
     *
     */
    private fun uploadToFirebase(passedUri : String){

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


        var metadata = storageMetadata {
            contentType = mimeType
        }

        val storage = Firebase.storage
        val storageRef = storage.reference

        var ref : StorageReference? = storageRef.child("upload/"+ System.currentTimeMillis().toString())

        //uploading file with metadata
        var uploadTask = ref?.putFile(fileUri,metadata)



        uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref?.downloadUrl
        })?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.progressBar.visibility = View.GONE

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

