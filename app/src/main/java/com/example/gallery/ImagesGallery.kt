package com.example.gallery

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.gallery.model.ImageAndVideoData
import kotlinx.coroutines.*
import java.util.ArrayList


object ImagesGallery {

    @JvmStatic
    fun listOfImage(context: Context): ArrayList<String> {
        val cursor: Cursor?
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        val orderBy = MediaStore.Video.Media.DATE_TAKEN
        cursor = context.contentResolver.query(
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

    /* @JvmStatic
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
    }
*/
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
*Method [getMedia()] to get media under MediaStore
* this method works on all api levels And this method is intended to use in Image Slider feature.
* @param  Context - Context of the activity.
*         SortOrder - Enum
* @return ImageData POJO having name of the image and uri
*
 */
    @JvmStatic
    fun getMedia(context: Context,sortOrder: SortOrder) : ArrayList<ImageAndVideoData> {

        var imageDataList : ArrayList<ImageAndVideoData> = ArrayList()
        val resolver = context.contentResolver

        val external_uri = MediaStore.Files.getContentUri("external")

        var orderBy : String?
        when(sortOrder){
            SortOrder.Date -> {
                orderBy = MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
            }
            SortOrder.Modified ->{
                orderBy = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC"
            }
            SortOrder.Size ->{
                orderBy = MediaStore.Files.FileColumns.SIZE + " DESC"
            }
            else ->{
                orderBy = MediaStore.Files.FileColumns.DISPLAY_NAME + " ASC"
            }
        }

        val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)



        val query = resolver.query(external_uri,
            null,
            selection,
            null,
            orderBy)

        val imageExtensionList = arrayOf("jpg", "bmp", "png", "jpeg")
        val videoExtensionList = arrayOf("mp4", "3gp", "gif")
        //by using use here, we are sure that cursor will get close (Kotlin feature)
        query?.use { cursor ->
            //if no error occurred but there is no media then do nothing
            if (cursor.count != 0) {
                val columnId = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val columnMimeTypeInfo =
                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                while (cursor.moveToNext()) {
                    val uri = ContentUris.withAppendedId(external_uri, cursor.getLong(columnId))
                    val id = cursor.getLong(columnId)
                    val data = cursor.getString(dataColumn)
                    val fileExtension = MimeTypeMap.getSingleton()
                        .getExtensionFromMimeType(cursor.getString(columnMimeTypeInfo))

                    if (imageExtensionList.contains(fileExtension)) {
                        imageDataList.add(ImageAndVideoData(ITEM_VIEW_TYPE_IMAGE,
                            id,
                            uri,
                            data))
                    } else if (videoExtensionList.contains(fileExtension)) {
                        imageDataList.add(ImageAndVideoData(ITEM_VIEW_TYPE_VIDEO,
                            id,
                            uri,
                            data))
                    }

                }

            }
        }


        return    imageDataList

    }

}