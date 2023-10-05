package com.example.agrishopapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapters.FavAdapter;
import com.example.models.MyFavouriteModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyFavouriteFragment extends Fragment  {

      FirebaseFirestore db;
    FirebaseAuth auth;
     RecyclerView recyclerView;
     FavAdapter favAdapter;
     List<MyFavouriteModel> myFavouriteModels;
     ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_favourite, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

//        progressBar = root.findViewById(R.id.fav_progressbar);
//        progressBar.setVisibility(View.VISIBLE);

        recyclerView = root.findViewById(R.id.fav_recyclerview);
        //   recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myFavouriteModels = new ArrayList<>();
        favAdapter = new FavAdapter(getActivity(), myFavouriteModels);
        recyclerView.setAdapter(favAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("FavouritedItem").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                MyFavouriteModel myFavouriteModel = documentSnapshot.toObject(MyFavouriteModel.class);
                                myFavouriteModels.add(myFavouriteModel);

                                favAdapter.notifyDataSetChanged();
//                                progressBar.setVisibility(View.GONE);
//                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

        return root;
    }

}
