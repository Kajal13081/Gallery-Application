package com.example.gallery

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jsibbold.zoomage.ZoomageView

class FullImageAdapter(private var context: Context?, private var imageList:List<String>,private var imageLink:String) : PagerAdapter() {


    override fun getCount(): Int {
     return imageList.size
    }



    override fun instantiateItem(container: ViewGroup, position: Int): Any {


        val itemLayout= LayoutInflater.from(context).inflate(R.layout.full_image_item_layout,container,false)

        // Set the images
        var imageView= itemLayout.findViewById<ZoomageView>(R.id.fullimageViewid)
        imageView.setImageURI(Uri.parse(imageList[position]))

        // Set the action on share button
        var navigationView= itemLayout.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        navigationView.setOnItemSelectedListener {
            val intent = Intent(Intent.ACTION_SEND)
            //                // putting uri of image to be shared
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageLink))
            //                // adding text to share
            //                // Add subject Here
            intent.putExtra(Intent.EXTRA_SUBJECT, "Add Subject")
            //                // setting type to image
            intent.type = "image/*"
            //                // calling startActivity() to share
            context?.startActivity(Intent.createChooser(intent, "Share Via"))
            true
        }

        // Add view to View Pager
        container.addView(itemLayout,0)

        return itemLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view==`object`
    }
}