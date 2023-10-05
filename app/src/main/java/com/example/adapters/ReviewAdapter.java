
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
import com.example.models.ReviewModel;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<ReviewModel> reviewModels;
    private Context context;

      public ReviewAdapter(Context context, List<ReviewModel> reviewModels) {
        this.context = context;
        this.reviewModels = reviewModels;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_all, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(reviewModels.get(position).getName());
        holder.address.setText(reviewModels.get(position).getAddress());
        holder.review.setText(reviewModels.get(position).getReview());
    }


    @Override
    public int getItemCount() {
        return reviewModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, review;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.view_name);
            address = itemView.findViewById(R.id.address);
            review = itemView.findViewById(R.id.review);


        }
    }
}
