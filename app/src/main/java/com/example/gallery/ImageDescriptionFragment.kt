package com.example.gallery

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.gallery.databinding.FragmentImageDescriptionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.util.*


class ImageDescriptionFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentImageDescriptionBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val args = ImageDescriptionFragmentArgs.fromBundle(requireArguments())
        var imageLink = args.imageURI
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_image_description, container, false)

        val name : TextView = binding.Fname
        val date : TextView = binding.fDate
        val size : TextView = binding.fSize
        val loc : TextView = binding.fLoc
        val res : TextView = binding.fReso

        val file: File = File(imageLink) //Get File from Uri
        val fileName = file.name //Name of the Image file
        val fileDate = Date(file.lastModified()).toString() //get Modified date
        var length = file.length() / 1024 // Size in KB
        var fileSize = ""
        if(length > 1024) {
            val rem = length%1024 //get remainder for MB conversion
            fileSize = (length/1024).toString() +"."+ rem.toString() + "MB" //get in MB
        } else {
            fileSize = length.toString() + "KB" //get in KB
        }
        var fileReso = getSize(Uri.parse(imageLink)) //returns image size in pixels

        name.setText("File Name: "+fileName)
        date.setText("Date created: "+fileDate)
        res.setText("Resolution: "+fileReso)
        size.setText("Size: "+fileSize)
        loc.setText("Location Path: "+imageLink)

        return binding.root
    }

    private fun getSize(uri: Uri) : String {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(uri.path).absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth
        var reso = imageHeight.toString()+"x"+imageWidth.toString()+"px"
        return reso
    }
}