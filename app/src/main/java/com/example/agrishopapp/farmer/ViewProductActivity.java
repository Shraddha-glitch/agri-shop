package com.example.agrishopapp.farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adapters.FarmProductAdapter;
import com.example.agrishopapp.R;
import com.example.models.FarmProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewProductActivity extends AppCompatActivity implements FarmProductAdapter.OnEditClickListener {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    FarmProductAdapter farmProductAdapter;
    List<FarmProductModel> farmProductModelList;
    Toolbar toolbar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.farm_view_all_rec);

        recyclerView.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        farmProductModelList = new ArrayList<>();
        farmProductAdapter = new FarmProductAdapter(this, farmProductModelList);
        farmProductAdapter.setOnEditClickListener(this); // Set the edit click listener
        recyclerView.setAdapter(farmProductAdapter);

        String farmerUsername = getIntent().getStringExtra("FarmerUsername");

        // Fetch products where the farmer's username matches
        firestore.collection("AllProducts")
                .whereEqualTo("farmerUsername", farmerUsername)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                FarmProductModel farmProductModel = documentSnapshot.toObject(FarmProductModel.class);
                                String documentId = documentSnapshot.getId(); // Get the document ID
                                farmProductModel.setDocumentId(documentId); // Set the document ID in the model
                                farmProductModelList.add(farmProductModel);
                            }

                            farmProductAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ViewProductActivity.this, "Error fetching products", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onEditClick(int position) {
        FarmProductModel clickedProduct = farmProductModelList.get(position);
        String documentId = clickedProduct.getDocumentId();

        // Pass the FarmProductModel object and its document ID to the EditProductActivity
        Intent intent = new Intent(this, EditProductActivity.class);
        intent.putExtra("productDocumentId", documentId);
        intent.putExtra("Edit", clickedProduct);
        startActivity(intent);
    }
}
