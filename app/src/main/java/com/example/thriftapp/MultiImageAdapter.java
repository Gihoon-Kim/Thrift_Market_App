package com.example.thriftapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MultiImageAdapter extends RecyclerView.Adapter<MultiImageAdapter.ViewHolder> {

    private final ArrayList<Uri> mData;
    private final Context mContext;

    public MultiImageAdapter(ArrayList<Uri> uriList, Context context) {

        this.mData = uriList;
        this.mContext = context;
    }

    @NonNull
    @Override
    // onCreateViewHolder - Return after creating ViewHolder object for ItemView
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        // Give back resources defined XML to form of View
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.multi_image_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    // Show data matched to position only item view of ViewHolder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Uri image_uri = mData.get(position);

        GlideApp.with(mContext)
                .load(image_uri)
                .into(holder.imageView);
    }

    public Uri getUri(int position) {

        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivImage);
        }
    }
}
