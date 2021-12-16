package com.example.thriftapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ImageBottomSheetDialog extends BottomSheetDialogFragment {

    LinearLayout layoutCamera;
    LinearLayout layoutGallery;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.imageview_bottom_sheet_layout, container, false);

        layoutCamera = view.findViewById(R.id.layoutCamera);
        layoutGallery = view.findViewById(R.id.layoutGallery);

        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("BottomSheet", "Execute Camera application");
            }
        });

        layoutGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("BottomSheet", "Execute Gallery Application");
            }
        });
        return view;
    }
}
