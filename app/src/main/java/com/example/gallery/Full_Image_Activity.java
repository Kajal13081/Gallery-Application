package com.example.gallery;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Full_Image_Activity extends AppCompatActivity {


    int position;
    ImageView imageView;
    String imageLink;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        imageView = findViewById(R.id.fullimageViewid);
        button = findViewById(R.id.button);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            position = bundle.getInt("pos",0);
           imageLink =  bundle.getString("image");
        }

        imageView.setImageURI(Uri.parse(imageLink));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
//                // putting uri of image to be shared
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageLink));
//                // adding text to share
                intent.putExtra(Intent.EXTRA_TEXT, "Add Caption");
//                // Add subject Here
                intent.putExtra(Intent.EXTRA_SUBJECT, "Add Subject");
//                // setting type to image
                intent.setType("image/png");
//                // calling startActivity() to share
                startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });
    }
}