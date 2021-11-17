package com.example.gallery

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import java.util.ArrayList

object ImagesGallery {
    @JvmStatic
    fun listOfImage(context: ScrollingFragment): ArrayList<String> {
        val cursor: Cursor?
        var column_index_folder_name: Int
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        val orderBy = MediaStore.Video.Media.DATE_TAKEN
        cursor = context.requireActivity().contentResolver.query(
            uri, projection, null,
            null, "$orderBy DESC"
        )   // cannot use context.contentResolver in fragment
        val column_index_data: Int = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        //get folder name
        //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }
}