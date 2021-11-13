package com.example.gallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.databinding.ScrollingActivityBinding;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {
    private ScrollingActivityBinding binding;

    PhotosRecyclerViewAdapter photosRecyclerViewAdapter;
    AlbumRecyclerViewAdapter albumRecyclerViewAdapter;

    List<String> images;

    final int My_READ_PERMISSION_CODE = 101;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.scrolling_activity);

        // dummy data
        int imgs[] = new int[]
                {
                        R.drawable.camera,
                        R.drawable.discord,
                        R.drawable.download,
                        R.drawable.screenshots,
                        R.drawable.telegram_images,
                        R.drawable.whatsapp_images
                };

        String desc[] = new String[]{
                "Camera",
                "Discord Images",
                "Downloads",
                "ScreenShots",
                "Telegram Images",
                "Whatsapp Images"
        };

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, My_READ_PERMISSION_CODE);
        } else {
            loadImages();
        }

//        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
//        binding.photosRecyclerView.setLayoutManager(gridLayoutManager);
//        photosRecyclerViewAdapter = new PhotosRecyclerViewAdapter(imgs);
//        binding.photosRecyclerView.setAdapter(photosRecyclerViewAdapter);


    }

    private void loadImages() {
        binding.photosRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.photosRecyclerView.setLayoutManager(gridLayoutManager);
        images = ImagesGallery.listofImages(this);  // remains to fix -> Images Gallery

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.albumRecyclerView.setLayoutManager(linearLayoutManager);
        albumRecyclerViewAdapter = new AlbumRecyclerViewAdapter(AlbumConverter.getAlbum(images), new AlbumRecyclerViewAdapter.AlbumListener() {
            @Override
            public void onAlbumClick(@NonNull List<String> list) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("photos", new ArrayList(list));
                PhotosActivity.start(ScrollingActivity.this, bundle);
            }
        });

        binding.albumRecyclerView.setAdapter(albumRecyclerViewAdapter);
        photosRecyclerViewAdapter = new PhotosRecyclerViewAdapter(this, images, new PhotosRecyclerViewAdapter.PhotoListener() {    // remains to fix-> Adapter
            @Override
            public void onPhotoClick(int position) {
                //Do something with photo
//                Toast.makeText(getApplicationContext(), "" + path, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), Full_Image_Activity.class);
                intent.putExtra("image", String.valueOf(images.get(position)));
                intent.putExtra("pos", position);
                startActivity(intent);
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
                    if (result) {
//                        Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
//                        Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
            });
}
