package com.example.registerloginexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductItem> productList;

    public ProductAdapter(List<ProductItem> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductItem productItem = productList.get(position);

        if (productItem.getImageUri() != null) {
            Glide.with(holder.itemView)
                    .load(productItem.getImageUri())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_settings))
                    .into(holder.productImageView);
        } else {
            holder.productImageView.setImageDrawable(null);
        }

        holder.productNameTextView.setText(productItem.getProductName());
        holder.quantityTextView.setText(productItem.getQuantity());
        holder.expiryDateTextView.setText(productItem.getExpiryDate());
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void addItem(ProductItem productItem) {
        int position = productList.size(); // 가장 마지막 위치
        productList.add(productItem);
        notifyItemInserted(position);
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView quantityTextView;
        TextView expiryDateTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            expiryDateTextView = itemView.findViewById(R.id.expiryDateTextView);
        }
    }
}
