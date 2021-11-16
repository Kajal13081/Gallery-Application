package com.example.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.gallery.R
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.example.gallery.ScrollingActivity
import com.example.gallery.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        startActivity(Intent(requireContext(), ScrollingActivity::class.java))
        // Inflate the layout for this fragment
        return binding.root
    }
}