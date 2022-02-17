package com.example.gallery.model

import android.net.Uri

data class ImageAndVideoData (
    //to distinguish video and image
    val viewType : Int,
    //for diff util
    val id : Long,
    // uri with id to make changes to file directly using content provider
    val uri: Uri,
    //this is for the path of the file, We may use it in the future
    val data : String
)
