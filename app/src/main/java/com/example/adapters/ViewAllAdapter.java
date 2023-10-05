package com.example.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agrishopapp.DetailedActivity;
import com.example.agrishopapp.R;
import com.example.models.ViewAllModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder> {

    Context context;
    List<ViewAllModel> list;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ViewAllModel viewAllModel=null;

    public ViewAllAdapter(Context context, List<ViewAllModel> list) {
        this.context = context;
        this.list = list;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);

        holder.name.setText(list.get(position).getName());
        holder.description.setText(list.get(position).getDescription());
        holder.rating.setText(list.get(position).getRating());
        holder.price.setText(list.get(position).getPrice() + " /kg");

        if (list.get(position).getType().equals("egg")) {
            holder.price.setText(list.get(position).getPrice() + " /dozen");
        } else if (list.get(position).getType().equals("milk")) {
            holder.price.setText(list.get(position).getPrice() + " /litre");
        }

        ViewAllModel currentProduct = list.get(position);

        holder.favBtn.setVisibility(currentProduct.isFavorite() ? View.GONE : View.VISIBLE);
        holder.favIcon.setVisibility(currentProduct.isFavorite() ? View.VISIBLE : View.GONE);

        holder.favBtn.setTag(position);
        holder.favIcon.setTag(position);

        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = (int) v.getTag();
                ViewAllModel clickedProduct = list.get(clickedPosition);

                // Here, call a method to add the clicked product to Firestore
                addToFavoritesFirestore(clickedProduct);
                clickedProduct.setFavorite(true);
                notifyDataSetChanged();
//                favBtnClicked();
            }
        });

        holder.favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = (int) v.getTag();
                ViewAllModel clickedProduct = list.get(clickedPosition);

                // Here, call a method to remove the clicked product from Firestore
                removeFromFavoritesFirestore(clickedProduct);
                clickedProduct.setFavorite(false);
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detail", list.get(position));
                context.startActivity(intent);
            }
        });
    }

//    private void favBtnClicked() {
//        final HashMap<String, Object> productMap = new HashMap<>();
//        productMap.put("name", viewAllModel.getName());
//        productMap.put("description", viewAllModel.getDescription());
//        productMap.put("rating", viewAllModel.getRating());
//        productMap.put("img_url", viewAllModel.getImg_url());
//        productMap.put("price", viewAllModel.getPrice());
//
//        firestore.collection("FavouritedItem")
//                .document(auth.getCurrentUser().getUid())
//                .collection("CurrentUser")
//                .add(productMap)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(context, "Added To Your Favourite List", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "Failed to add to Favorites", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private void addToFavoritesFirestore(ViewAllModel viewAllModel) {
        // Create a map with the product details
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("name", viewAllModel.getName());
        productMap.put("description", viewAllModel.getDescription());
        productMap.put("rating", viewAllModel.getRating());
        productMap.put("img_url", viewAllModel.getImg_url());
        productMap.put("price", viewAllModel.getPrice());

        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("FavouritedItem")
                .add(productMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Added To Your Favourite List", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to add to Favorites", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeFromFavoritesFirestore(ViewAllModel clickedProduct) {
        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("FavouritedItem")
                .whereEqualTo("name", clickedProduct.getName())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            firestore.collection("CurrentUser")
                                    .document(auth.getCurrentUser().getUid())
                                    .collection("FavouritedItem")
                                    .document(document.getId())
                                    .delete()
                                    .addOnCompleteListener(deleteTask -> {
                                        if (deleteTask.isSuccessful()) {
                                            Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Failed to remove from Favorites", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageButton favBtn,favIcon;
        TextView name, description, price, rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.view_img);
            name = itemView.findViewById(R.id.view_name);
            description = itemView.findViewById(R.id.view_description);
            price = itemView.findViewById(R.id.view_price);
            rating = itemView.findViewById(R.id.view_rating);
            favBtn = itemView.findViewById(R.id.favBtn);
            favIcon = itemView.findViewById(R.id.favIcon);
        }
    }
}
