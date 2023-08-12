package com.example.registerloginexample;

import android.content.Context;
import android.content.Intent;
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
    private Context context;

    public ProductAdapter(List<ProductItem> productList, Context context) {
        this.productList = productList;
        this.context = context;
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
            Glide.with(context)
                    .load(productItem.getImageUri())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_settings))
                    .into(holder.productImageView);
        } else {
            holder.productImageView.setImageDrawable(null);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ProductItem productItem = productList.get(position);
                    Intent intent = new Intent(context, Detail.class);
                    // 여기에 필요한 데이터를 인텐트에 추가할 수 있습니다.
                    context.startActivity(intent);
                }
            }
        });

        holder.productNameTextView.setText(productItem.getProductName());
        holder.quantityTextView.setText(productItem.getQuantity());
        holder.expiryDateTextView.setText(productItem.getExpiryDate());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;

        TextView refIdTextView;
        TextView productNameTextView;
        TextView quantityTextView;
        TextView expiryDateTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            refIdTextView=itemView.findViewById(R.id.refIdTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            expiryDateTextView = itemView.findViewById(R.id.expiryDateTextView);
        }
    }
}
