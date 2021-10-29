package com.example.gallery;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gallery.databinding.FragmentMainBinding;

import java.sql.Array;
import java.util.ArrayList;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    PhotosRecyclerViewAdapter photosRecyclerViewAdapter;
    AlbumRecyclerViewAdapter albumRecyclerViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        // dummy data
        int imgs[] = new int[]
                {
                        R.drawable.c_plus_plus,
                        R.drawable.c_sharp,
                        R.drawable.java,
                        R.drawable.javascript,
                        R.drawable.kotlin,
                        R.drawable.python,
                        R.drawable.ruby,
                        R.drawable.swift,
                        R.drawable.typescript,
                        R.drawable.visual_studio
                };

        String desc[] = new String[]{
                "C++",
                "C#",
                "Java",
                "JavaScript",
                "Kotlin",
                "Python",
                "Ruby",
                "Swift",
                "TypeScript",
                "Visual Studio"
        };

        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        binding.photosRecyclerView.setLayoutManager(gridLayoutManager);
        photosRecyclerViewAdapter = new PhotosRecyclerViewAdapter(imgs);
        binding.photosRecyclerView.setAdapter(photosRecyclerViewAdapter);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.albumRecyclerView.setLayoutManager(linearLayoutManager);
        albumRecyclerViewAdapter = new AlbumRecyclerViewAdapter(imgs,desc);
        binding.albumRecyclerView.setAdapter(albumRecyclerViewAdapter);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}