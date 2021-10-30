package com.example.gallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gallery.databinding.FragmentMainBinding;

import java.util.List;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    PhotosRecyclerViewAdapter photosRecyclerViewAdapter;
    AlbumRecyclerViewAdapter albumRecyclerViewAdapter;

    List<String> images;

    final int My_READ_PERMISSION_CODE = 101;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, My_READ_PERMISSION_CODE);
        } else {
            loadImages();
        }

//        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
//        binding.photosRecyclerView.setLayoutManager(gridLayoutManager);
//        photosRecyclerViewAdapter = new PhotosRecyclerViewAdapter(imgs);
//        binding.photosRecyclerView.setAdapter(photosRecyclerViewAdapter);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.albumRecyclerView.setLayoutManager(linearLayoutManager);
        albumRecyclerViewAdapter = new AlbumRecyclerViewAdapter(imgs, desc);
        binding.albumRecyclerView.setAdapter(albumRecyclerViewAdapter);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void loadImages() {
        binding.photosRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        binding.photosRecyclerView.setLayoutManager(gridLayoutManager);
        images = ImagesGallery.listofImages(requireContext());  // remains to fix -> Images Gallery
        photosRecyclerViewAdapter = new PhotosRecyclerViewAdapter(requireContext(), images, new PhotosRecyclerViewAdapter.PhotoListener() {    // remains to fix-> Adapter
            @Override
            public void onPhotoClick(String path) {
                //Do something with photo
                Toast.makeText(requireContext(), "" + path, Toast.LENGTH_SHORT).show();
            }
        });
        binding.photosRecyclerView.setAdapter(photosRecyclerViewAdapter);

//        gallery_number.setText("Photos(" + images.size() + ")");
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == My_READ_PERMISSION_CODE){
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result) {
//                        Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
//                        Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                        Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
            });
}