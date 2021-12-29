package com.example.gallery

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.mainBottomNavigation.setupWithNavController(navController)

        // to change the fragment in FragmentContainerView
//        val scrollFrag= ScrollingFragment()
//        val folderFrag= FolderFragment()
//
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.fragmentContainerView,scrollFrag)
//        }
//
//        binding.mainBottomNavigation.setOnItemSelectedListener {
//            when(it.itemId) {
//                R.id.nav_folders -> {
//                    supportFragmentManager.beginTransaction().apply {
//                        replace(R.id.fragmentContainerView,folderFrag)
//                        commit()
//                    }
//                    Log.d(TAG,"folder is selected")
//                    true
//                }
//
//                R.id.nav_photos -> {
//                    supportFragmentManager.beginTransaction().apply {
//                        replace(R.id.fragmentContainerView,scrollFrag)
//                        commit()
//                    }
//                    Log.d(TAG,"photos is selected")
//                    true
//                }
//                else -> false
//            }
//        }
    }
}