package com.example.thriftapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ImageBottomSheetDialog extends BottomSheetDialogFragment {

    LinearLayout layoutCamera;
    LinearLayout layoutGallery;
    ImageView ivPhoto;
    ActivityResultLauncher<Intent> resultLauncher;

    public ImageBottomSheetDialog(ImageView imageView) {

        this.ivPhoto = imageView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.imageview_bottom_sheet_layout, container, false);
        layoutCamera = view.findViewById(R.id.layoutCamera);
        layoutGallery = view.findViewById(R.id.layoutGallery);

        layoutCamera.setOnClickListener(v -> {

            Log.i("BottomSheet", "Execute Camera application");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            resultLauncher.launch(intent);
        });

        layoutGallery.setOnClickListener(v -> {

            Log.i("BottomSheet", "Execute Gallery Application");
            // TODO : EXECUTE GALLERY
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
                    dismiss();
                }
        );
        return view;
    }
}
