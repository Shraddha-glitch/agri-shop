package com.example.agrishopapp.farmer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agrishopapp.R;
import com.example.models.FarmProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddProduct extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText productNameEditText;
    private RadioGroup productTypeRadioGroup;
    private RadioButton radioButtonFruit;
    private RadioButton radioButtonVegetable;
    private RadioButton radioButtonEgg;
    private RadioButton radioButtonMilk;

    private EditText productStockEditText;
    private EditText productPriceEditText;
    private EditText productDescriptionEditText;
    private ImageView productImageView;
    private Uri imageUri;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    private Button addButton;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productNameEditText = findViewById(R.id.productname);
        productTypeRadioGroup = findViewById(R.id.radioGroupProductType);
        radioButtonFruit = findViewById(R.id.radioButtonFruit);
        radioButtonVegetable = findViewById(R.id.radioButtonVegetable);
        productStockEditText = findViewById(R.id.stock);
        productPriceEditText = findViewById(R.id.Price);
        productDescriptionEditText = findViewById(R.id.editTextDescription);
        productImageView = findViewById(R.id.product_image_view);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        Button addButton = findViewById(R.id.buttonadd);
        addButton.setOnClickListener(view -> onAddButtonClicked());
    }

    public void onChooseImageButtonClicked(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                productImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onAddButtonClicked() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String productName = productNameEditText.getText().toString();
        String productType;
        int selectedRadioButtonId = productTypeRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == R.id.radioButtonFruit) {
            productType = "fruit";
        } else if (selectedRadioButtonId == R.id.radioButtonVegetable) {
            productType = "vegetable";
        } else if (selectedRadioButtonId == R.id.radioButtonEgg) {
            productType = "egg";
        } else if (selectedRadioButtonId == R.id.radioButtonMilk) {
            productType = "milk";
        } else {
            // Handle the case when no radio button is selected
            productType = "unknown";
        }
        int stock = Integer.parseInt(productStockEditText.getText().toString()); // Adding stock field
        int price = Integer.parseInt(productPriceEditText.getText().toString());
        String description = productDescriptionEditText.getText().toString();
        String imageUriString = imageUri.toString();
        String datetime = getCurrentDatetime();
        FarmProductModel newProduct = new FarmProductModel();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Retrieve current farmer's username from the "users" collection
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String farmerUsernameFromDB = dataSnapshot.child("username").getValue(String.class);

                // Create a map with the product details
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("name", productName);
                productMap.put("type", productType);
                productMap.put("stock", stock);
                productMap.put("price", price);
                productMap.put("description", description);
                productMap.put("img_url", imageUriString);
                productMap.put("rating","0" ); // Set an initial rating
                productMap.put("farmerUsername", farmerUsernameFromDB); // Set the current farmer's username
                productMap.put("datetime", datetime);

                CollectionReference productsCollection = firestore.collection("AllProducts");

                // Add the product to the "AllProducts" collection in Firestore
//                firestore.collection("AllProducts")
//                        .add(productMap)
//                        .addOnSuccessListener(documentReference -> {
//                            Toast.makeText(AddProduct.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(AddProduct.this, farmerdashboard.class);
//                            startActivity(intent);
//                        })
//                        .addOnFailureListener(e -> {
//                            Log.e("AddProductActivity", "Firestore error: " + e.getMessage());
//                            Toast.makeText(AddProduct.this, "Failed to add product", Toast.LENGTH_SHORT).show();
//                        });
                productsCollection.add(productMap)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(AddProduct.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddProduct.this, farmerdashboard.class);
                            startActivity(intent);
                            String generatedProductId = documentReference.getId(); // Get the generated document ID
                            performUpdatesOrNavigate(generatedProductId);
                        })
                        .addOnFailureListener(e -> {
                            Log.e("AddProductActivity", "Firestore error: " + e.getMessage());
                            Toast.makeText(AddProduct.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle cancellation
                Log.e("AddProduct", "Failed to retrieve farmerUsername: " + databaseError.getMessage());
            }
        });

        Log.d("AddProductActivity", "onAddButtonClicked() method called");
    }
    private void performUpdatesOrNavigate(String productId) {
        // Check if you need to perform updates or navigate to EditProductActivity
        // For example, if you have some conditions, you can decide based on those conditions

        // If you need to navigate to EditProductActivity
        Intent intent = new Intent(AddProduct.this, EditProductActivity.class);
        intent.putExtra("productId", productId); // Pass the generated document ID


        // If you need to perform updates immediately, call your update method here
        // updateProduct(productId);
    }

    private String getCurrentDatetime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }
}
