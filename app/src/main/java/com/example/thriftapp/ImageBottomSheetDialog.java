package com.example.thriftapp;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ImageBottomSheetDialog extends BottomSheetDialogFragment {

    private static final String TAG = "ImageDialog";
    ArrayList<Uri> uriList = new ArrayList<>(); // images' uri
    Context context;

    RecyclerView rvImages;
    MultiImageAdapter adapter;
    ActivityResultLauncher<Intent> galleryLauncher;

    LinearLayout layoutCamera;
    LinearLayout layoutGallery;
    ImageView ivPhoto;
    ActivityResultLauncher<Intent> resultLauncher;

    Button btnAddImage;

    public ImageBottomSheetDialog(Context context, ImageView imageView, RecyclerView rvImages, Button btnAddImage) {

        this.context = context;
        this.ivPhoto = imageView;
        this.rvImages = rvImages;
        this.btnAddImage = btnAddImage;
    }

    public MultiImageAdapter getAdapter() {

        return adapter;
    }

    @SuppressLint("IntentReset")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.imageview_bottom_sheet_layout, container, false);
        layoutCamera = view.findViewById(R.id.layoutCamera);
        layoutGallery = view.findViewById(R.id.layoutGallery);

        layoutCamera.setOnClickListener(v -> {

            Log.i(TAG, "Execute Camera application");
            /* Execute Camera Application */
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            resultLauncher.launch(intent);
        });

        layoutGallery.setOnClickListener(v -> {

            Log.i(TAG, "Execute Gallery Application");
            // EXECUTE GALLERY
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);
        });

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    assert result.getData() != null;
                    Bundle bundle = result.getData().getExtras();
                    /* Conversion to Bitmap */
                    Bitmap imgBitmap = (Bitmap) bundle.get("data");
                    Matrix rotateMatrix = new Matrix();
                    rotateMatrix.postRotate(90);

                    Bitmap sideInversionImg = Bitmap.createBitmap(imgBitmap, 0, 0,
                            imgBitmap.getWidth(), imgBitmap.getHeight(), rotateMatrix, false);

                    ivPhoto.setImageBitmap(sideInversionImg);

                    ivPhoto.setVisibility(View.VISIBLE);
                    rvImages.setVisibility(View.GONE);
                    btnAddImage.setVisibility(View.GONE);
                    dismiss();
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getData() == null) { // no image is selected

                        Toast.makeText(context, "Image is not selected", Toast.LENGTH_SHORT).show();
                    } else {    // at least 1 image is selected

                        if (result.getData().getClipData() == null) {   // if the image is only one

                            Uri imageUri = result.getData().getData();
                            uriList.add(imageUri);

                            adapter = new MultiImageAdapter(uriList, context);
                            rvImages.setAdapter(adapter);
                            rvImages.setLayoutManager(
                                    new LinearLayoutManager(
                                            context,
                                            LinearLayoutManager.HORIZONTAL,
                                            true
                                    )
                            );
                        } else {    // if 2 or more images are selected

                            ClipData clipData = result.getData().getClipData();
                            Log.i("Clip Data", String.valueOf(clipData.getItemCount()));

                            if (clipData.getItemCount() > 10) { // Maximum number of images is 10;

                                Toast.makeText(context, "Images can be added up to 10", Toast.LENGTH_SHORT).show();
                            } else {

                                Log.i(TAG, "Multiple Choice");

                                for (int i = 0; i < clipData.getItemCount(); i++) {

                                    Uri imageUri = clipData.getItemAt(i).getUri();
                                    Log.i(TAG, "Image URI = " + imageUri);

                                    try {

                                        uriList.add(imageUri);
                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }
                                }

                                adapter = new MultiImageAdapter(uriList, context);
                                rvImages.setAdapter(adapter);
                                rvImages.setLayoutManager(
                                        new LinearLayoutManager(
                                                context,
                                                LinearLayoutManager.HORIZONTAL,
                                                true
                                        ));
                            }
                        }

                        rvImages.setVisibility(View.VISIBLE);
                        ivPhoto.setVisibility(View.GONE);
                        btnAddImage.setVisibility(View.GONE);
                    }
                    dismiss();
                });
        return view;
    }
}
