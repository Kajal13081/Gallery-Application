package com.example.gallery

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.io.File
import java.util.*

class ImageDescription : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_description)
        val imageLink:String = intent.getStringExtra("URI").toString()

        val back = findViewById<Button>(R.id.back)
        val name = findViewById<TextView>(R.id.Fname)
        val date = findViewById<TextView>(R.id.fDate)
        val size = findViewById<TextView>(R.id.fSize)
        val loc = findViewById<TextView>(R.id.fLoc)
        val res = findViewById<TextView>(R.id.fReso)

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

        back.setOnClickListener {view ->
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }

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