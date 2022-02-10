package com.example.gallery

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.util.ArrayList


object ImagesGallery {


/*
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

        *//* val orderBy = MediaStore.Video.Media.DATE_TAKEN
         cursor = context.contentResolver.query(
             uri, projection, null,
             null, "$orderBy DESC"
         )   // cannot use context.contentResolver in fragment

        *//* val column_index_data: Int = cursor1!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        //get folder name
        //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor1.moveToNext()) {
            absolutePathOfImage = cursor1.getString(column_index_data)
            listOfAllImages.add(absolutePathOfImage)
        }
        cursor1.close()

        return listOfAllImages
    }*/

    enum class SortOrder{
        Name,
        Date,
        Modified,
        Size
    }

    /**
     * Got issue that this method isn't working below api level 29 in some phone (need further investigation)
     * Reason I found -> Data field of Media Store is deprecated
     */
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
            SortOrder.Name ->{
                orderBy = MediaStore.Images.ImageColumns.DISPLAY_NAME + " ASC"
            }
            SortOrder.Size ->{
                orderBy = MediaStore.Images.ImageColumns.SIZE + " DESC"
            }
            SortOrder.Modified ->{
                orderBy = MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC"
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


    /*
    *Method [getImages()] to get Images under MediaStore
    * this method works on all api levels And this method is intended to use in Image Slider feature.
    * @param  Context - Context of the activity.
    *         SortOrder - Enum
    * @return ImageData POJO having name of the image and uri
    *
     */
  /*  @JvmStatic
    fun getImages(context: Context,sortOrder: SortOrder) : ArrayList<ImageData>{

        var imageDataList : ArrayList<ImageData> = ArrayList()
        val resolver = context.contentResolver
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val external_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

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
                orderBy = MediaStore.Images.ImageColumns.DISPLAY_NAME + " ASC"
            }
        }



        val query = resolver.query(external_uri,
            projection,
            null,
            null,
            orderBy)

        //by using use here, we are sure that cursor will get close (Kotlin feature)
        query?.use {
            cursor->
            //if no error occurred but there is no images then do nothing
            if(cursor.count!= 0){
                val columnId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val columnName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

                while(cursor.moveToNext()){
                    val imageUri = ContentUris.withAppendedId(external_uri,cursor.getLong(columnId))
                    val imageName = cursor.getString(columnName)
                    imageDataList.add(ImageData(imageName,imageUri))
               }

            }
        }

        return imageDataList

    }

    data class ImageData(
        var imageName : String,
                var uri : Uri
    )

*/
}