package com.example.thriftapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {

    private final ArrayList<ProductsInformation> unFilteredList;
    private ArrayList<ProductsInformation> filteredList;

    private OnItemClickListener mListener = null;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();

                if (charString.isEmpty()) {

                    filteredList = unFilteredList;
                } else {

                    ArrayList<ProductsInformation> filteringList = new ArrayList<>();
                    for (int i = 0; i < unFilteredList.size(); i++) {

                        Log.i("Adapter", "in for loop");
                        if (unFilteredList.get(i).getProductName().toLowerCase().contains(charString.toLowerCase())) {

                            filteringList.add(unFilteredList.get(i));
                        }
                    }

                    filteredList = filteringList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredList = (ArrayList<ProductsInformation>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    // ViewHolder Class that save Item View
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName;
        TextView tvProductDesc;
        TextView tvProductOwner;
        TextView tvProductPrice;
        TextView tvTradeLocation;
        TextView tvAddedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductDesc = itemView.findViewById(R.id.tvProductDesc);
            tvProductOwner = itemView.findViewById(R.id.tvProductOwner);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvTradeLocation = itemView.findViewById(R.id.tvTradeLocation);
            tvAddedDate = itemView.findViewById(R.id.tvAddedDate);

            itemView.setOnClickListener(v -> {

                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {

                    if (mListener != null) {

                        mListener.onItemClick(v, pos);
                    }
                }
            });
        }
    }

    // Constructor get Data list object
    ProductAdapter(ArrayList<ProductsInformation> list) {

        this.unFilteredList = list;
        this.filteredList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);

        return new ViewHolder(view);
    }

    // Show data on Item view that is matched position
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String productName = filteredList.get(position).getProductName();
        String productDesc = filteredList.get(position).getProductDesc();
        String productOwner = filteredList.get(position).getProductOwner();
        String productPrice = filteredList.get(position).getProductPrice();
        String tradeLocation = filteredList.get(position).getTradeLocation();
        String addedDate = filteredList.get(position).getAddedDate();

        holder.tvProductName.setText("product Name : " + productName);
        holder.tvProductDesc.setText("product Description : " + productDesc);
        holder.tvProductOwner.setText("product Owner : " + productOwner);
        holder.tvProductPrice.setText("product Price : " + productPrice);
        holder.tvTradeLocation.setText("Trade Location : " + tradeLocation);
        holder.tvAddedDate.setText("Registered Date : " + addedDate);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public interface OnItemClickListener {

        void onItemClick(View v, int position);
    }

    public void SetOnItemClickListener(OnItemClickListener listener) {

        this.mListener = listener;
    }
}
