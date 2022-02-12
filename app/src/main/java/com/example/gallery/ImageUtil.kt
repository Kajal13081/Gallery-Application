package com.example.gallery

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File


object ImageUtil {

    @RequiresApi(Build.VERSION_CODES.R)
    fun deleteImageR(activity : Activity, image : String){
        val file = File(image)
        val mediaID = getFilePathToMediaID(file.absolutePath,  activity)
        val uri = ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"),mediaID)
        val listUri = arrayListOf(uri)
        val pendingIntent = MediaStore.createDeleteRequest(activity.contentResolver,listUri)
        activity.startIntentSenderForResult(pendingIntent.intentSender,45,null,0,0,0,null)
    }

    fun getFilePathToMediaID(imagePath: String, context: Context): Long {
        var id: Long = 0
        val cr: ContentResolver = context.contentResolver
        val uri = MediaStore.Files.getContentUri("external")
        val selection = MediaStore.Audio.Media.DATA
        val selectionArgs = arrayOf(imagePath)
        val projection = arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE)
        val cursor: Cursor? = cr.query(uri, projection, "$selection=?", selectionArgs, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val idIndex: Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                id = cursor.getString(idIndex).toLong()
            }
        }
        cursor?.close()
        return id
    }

    fun deleteImage(context : Context ,image: String) {
        val file = File(image)
        val mediaID = getFilePathToMediaID(file.absolutePath, context)
        try {
            context.contentResolver.delete(
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediaID),
                null,
                null
            )
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }

}