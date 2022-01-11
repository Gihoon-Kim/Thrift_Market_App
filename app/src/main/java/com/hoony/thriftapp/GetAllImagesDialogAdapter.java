package com.hoony.thriftapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GetAllImagesDialogAdapter extends RecyclerView.Adapter<GetAllImagesDialogAdapter.ViewHolder> {

    private final ArrayList<ItemImages> mData;
    private final Context mContext;

    public GetAllImagesDialogAdapter(ArrayList<ItemImages> list, Context mContext) {

        this.mData = list;
        this.mContext = mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivImage);
        }
    }

    @NonNull
    @Override
    public GetAllImagesDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_all_images_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GetAllImagesDialogAdapter.ViewHolder holder, int position) {

        Bitmap image_bitmap = mData.get(position).getBitmap();

        GlideApp.with(mContext)
                .load(image_bitmap)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
