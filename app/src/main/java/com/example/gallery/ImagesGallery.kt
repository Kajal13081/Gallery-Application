package com.example.gallery

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import java.util.ArrayList


object ImagesGallery {



    @JvmStatic
    fun listOfImage(context: Context): ArrayList<String> {
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        val projection1 = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        )
        val orderByDate = MediaStore.Images.Media.DATE_TAKEN + " DESC"
        val orderByName =   MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " DESC"
        val orderByModifiedDate = MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC"



        val  cursor1 = context.contentResolver.query(uri,
            projection1,null, null, orderByName )

        /* val orderBy = MediaStore.Video.Media.DATE_TAKEN
         cursor = context.contentResolver.query(
             uri, projection, null,
             null, "$orderBy DESC"
         )   // cannot use context.contentResolver in fragment

        */ val column_index_data: Int = cursor1!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        //get folder name
        //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor1.moveToNext()) {
            absolutePathOfImage = cursor1.getString(column_index_data)
            listOfAllImages.add(absolutePathOfImage)
        }

        return listOfAllImages
    }

    enum class SortOrder{
        Name,
        Date,
        Modified,
        Size
    }
    @JvmStatic
    fun listOfSortedImages(context: Context, sortOrder : SortOrder) : ArrayList<String>{

        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String

        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection1 = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        )

        var orderBy : String?
        when(sortOrder){
            SortOrder.Date -> {
                orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC"
            }
            SortOrder.Modified ->{
                orderBy = MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC"
            }
            SortOrder.Size ->{
                orderBy = MediaStore.Images.ImageColumns.SIZE + " DESC"
            }
            else ->{
                orderBy = MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " ASC"
            }
        }

        val  cursor1 = context.contentResolver.query(uri,
            projection1,null, null, orderBy )

        val column_index_data: Int = cursor1!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        //get folder name
        //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor1.moveToNext()) {
            absolutePathOfImage = cursor1.getString(column_index_data)
            listOfAllImages.add(absolutePathOfImage)
        }

        return listOfAllImages

    }

}