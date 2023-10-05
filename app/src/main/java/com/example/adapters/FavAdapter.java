// FarmProductAdapter.java
package com.example.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agrishopapp.DetailedActivity;
import com.example.agrishopapp.R;
import com.example.agrishopapp.ViewAllActivity;
import com.example.agrishopapp.farmer.EditProductActivity;
import com.example.models.FarmProductModel;
import com.example.models.MyFavouriteModel;
import com.example.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private List<MyFavouriteModel> myFavouriteModels;
    private Context context;
    FirebaseFirestore firestore;
    FirebaseAuth auth;





    public FavAdapter(Context context, List<MyFavouriteModel> myFavouriteModels) {
        this.context = context;
        this.myFavouriteModels = myFavouriteModels;
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(myFavouriteModels.get(position).getImg_url()).into(holder.imageView);

        holder.name.setText(myFavouriteModels.get(position).getName());
        holder.description.setText(myFavouriteModels.get(position).getDescription());
        holder.rating.setText(myFavouriteModels.get(position).getRating());
        holder.price.setText(myFavouriteModels.get(position).getPrice() + " /kg");

        if (myFavouriteModels.get(position).getType().equals("egg")) {
            holder.price.setText(myFavouriteModels.get(position).getPrice() + " /dozen");
        }
        if (myFavouriteModels.get(position).getType().equals("milk")) {
            holder.price.setText(myFavouriteModels.get(position).getPrice() + " /litre");
            holder.favBtn.setVisibility(View.GONE);
            holder.favIcon.setVisibility(View.VISIBLE);
        } else {
            holder.favBtn.setVisibility(View.VISIBLE);
            holder.favIcon.setVisibility(View.GONE);
        }

    }
//    private void retrieveAndSetFavorites() {
//        firestore.collection("CurrentUser")
//                .document(auth.getCurrentUser().getUid())
//                .collection("FavouritedItem")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        List<MyFavouriteModel> favoritedItems = task.getResult().toObjects(MyFavouriteModel.class);
//                        for (int i = 0; i < myFavouriteModels.size(); i++) {
//                            MyFavouriteModel item = myFavouriteModels.get(i);
//                            for (MyFavouriteModel favoritedItem : favoritedItems) {
//                                if (item.getName().equals(favoritedItem.getName())) {
//                                    item.setFavorite(true);
//                                    item.setDocumentId(favoritedItem.getDocumentId());
//                                    break;
//                                }
//                            }
//                        }
//                        notifyDataSetChanged();
//                    }
//                });
//    }

//    private void addToFavoritesFirestore(MyFavouriteModel myFavouriteModels) {
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//
//        // Create a map with the product details
//        Map<String, Object> productMap = new HashMap<>();
//        productMap.put("name", myFavouriteModels.getName());
//        productMap.put("description", myFavouriteModels.getDescription());
//        productMap.put("rating", myFavouriteModels.getRating());
//        productMap.put("img_url", myFavouriteModels.getImg_url());
//        productMap.put("price", myFavouriteModels.getPrice());
//       // productMap.put("userId", auth.getCurrentUser().getUid()); // Save the user's UID
//
//
//
////        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
////                .collection("FavouritedItem").add(productMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
////                    @Override
////                    public void onComplete(@NonNull Task<DocumentReference> task) {
////                        myFavouriteModels.setFavorite(true);
////                        notifyDataSetChanged();
////                        Toast.makeText(context,"Added To Your Favourite List", Toast.LENGTH_SHORT).show();
////                    }
////                });
//
//
//    }
//    private void removeFromFavoritesFirestore(MyFavouriteModel clickedProduct) {
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//
//        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
//                .collection("FavouritedItem")
//                .whereEqualTo("name", clickedProduct.getName())
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (DocumentSnapshot document : task.getResult()) {
//                            firestore.collection("CurrentUser")
//                                    .document(auth.getCurrentUser().getUid())
//                                    .collection("FavouritedItem")
//                                    .document(document.getId())
//                                    .delete()
//                                    .addOnCompleteListener(deleteTask -> {
//                                        if (deleteTask.isSuccessful()) {
//                                            clickedProduct.setFavorite(false);
//                                            notifyDataSetChanged();
//                                            Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(context, "Failed to remove from Favorites", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        }
//                    }
//                });
//    }


    @Override
    public int getItemCount() {
        return myFavouriteModels.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, description, price, rating;
        ImageButton favBtn;
        ImageView favIcon;

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
