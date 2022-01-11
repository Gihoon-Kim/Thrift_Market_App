package com.hoony.thriftapp;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImagesListDialog {

    private final static String TAG = "ImagesListDialog";

    private final Context context;
    private final ArrayList<ItemImages> list;

    public ImagesListDialog(Context context, ArrayList<ItemImages> list) {

        this.context = context;
        this.list = list;
    }

    public void CallDialog() {

        Dialog dialog = new Dialog(context);
        GetAllImagesDialogAdapter allImagesDialogAdapter = new GetAllImagesDialogAdapter(list, context);

        dialog.setContentView(R.layout.dialog_all_images);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);

        RecyclerView recyclerView = dialog.findViewById(R.id.rvImagesList);
        recyclerView.setAdapter(allImagesDialogAdapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        true
                )
        );

        dialog.show();
    }
}
