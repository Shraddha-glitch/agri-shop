// FarmProductAdapter.java
package com.example.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agrishopapp.DetailedActivity;
import com.example.agrishopapp.R;
import com.example.agrishopapp.farmer.EditProductActivity;
import com.example.agrishopapp.farmer.ViewProductActivity;
import com.example.models.FarmProductModel;

import java.util.List;

public class FarmProductAdapter extends RecyclerView.Adapter<FarmProductAdapter.ViewHolder> {

    private List<FarmProductModel> farmProductModels;
    private Context context;

    private OnEditClickListener editClickListener;
    public FarmProductAdapter(Context context, List<FarmProductModel> farmProductModels) {
        this.context = context;
        this.farmProductModels = farmProductModels;
    }
    public interface OnEditClickListener {
        void onEditClick(int position); // Define the edit click method
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener; // Set the listener
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.farm_item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(farmProductModels.get(position).getImg_url()).into(holder.imageView);

        holder.name.setText(farmProductModels.get(position).getName());
        holder.description.setText(farmProductModels.get(position).getDescription());
        holder.rating.setText(farmProductModels.get(position).getRating());
        holder.price.setText(farmProductModels.get(position).getPrice() + " /kg");

        if (farmProductModels.get(position).getType().equals("egg")) {
            holder.price.setText(farmProductModels.get(position).getPrice() + " /dozen");
        }
        if (farmProductModels.get(position).getType().equals("milk")) {
            holder.price.setText(farmProductModels.get(position).getPrice() + " /litre");
        }

        holder.editButton.setTag(position);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int clickedPosition = (int) v.getTag();
//                FarmProductModel clickedProduct = farmProductModels.get(clickedPosition);
//                String documentId = clickedProduct.getDocumentId();
//
//                // Pass the FarmProductModel object and its document ID to the EditProductActivity
//                Intent intent = new Intent(context, EditProductActivity.class);
//                intent.putExtra("productDocumentId", documentId);
//
//                intent.putExtra("Edit", clickedProduct);
//                context.startActivity(intent);
//            }
@Override
public void onClick(View v) {
    if (editClickListener != null) {
        int clickedPosition = (int) v.getTag();
        editClickListener.onEditClick(clickedPosition); // Notify the listener
        FarmProductModel clickedProduct = farmProductModels.get(clickedPosition);
        if(clickedProduct!= null){
            String documentId = clickedProduct.getDocumentId();

            // Pass the FarmProductModel object and its document ID to the EditProductActivity
            Intent intent = new Intent(context, EditProductActivity.class);
            intent.putExtra("productDocumentId", documentId);

            intent.putExtra("Edit", clickedProduct);
            context.startActivity(intent);
        }

    }
}
        });
    }

    @Override
    public int getItemCount() {
        return farmProductModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, description, price, rating;
        Button editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.farm_view_img);
            name = itemView.findViewById(R.id.farm_view_name);
            description = itemView.findViewById(R.id.farm_view_description);
            price = itemView.findViewById(R.id.farm_view_price);
            rating = itemView.findViewById(R.id.farm_view_rating);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
