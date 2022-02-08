package com.example.gallery

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.jsibbold.zoomage.ZoomageView

class FullImageAdapter(private var context: Context?, private var imageList:List<String>) : PagerAdapter() {


    override fun getCount(): Int {
     return imageList.size
    }



    override fun instantiateItem(container: ViewGroup, position: Int): Any {


        val itemLayout= LayoutInflater.from(context).inflate(R.layout.full_image_item_layout,container,false)
        var imageView= itemLayout.findViewById<ZoomageView>(R.id.fullimageViewid)
        imageView.setImageURI(Uri.parse(imageList[position]))
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