package com.example.agrishopapp;

import com.example.models.FarmProductModel; // Import the FarmProductModel class
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseProductManager {
    private static final String PRODUCTS_NODE = "products";

    private DatabaseReference productsRef;

    public FirebaseProductManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        productsRef = database.getReference(PRODUCTS_NODE);
    }

    public void addProduct(FarmProductModel product) {
        // Generate a unique key for the product
        String productId = productsRef.push().getKey();

        // Add the product to the database
        productsRef.child(productId).setValue(product);
    }
}
