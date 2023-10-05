package com.example.agrishopapp.farmer;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.agrishopapp.R;
import com.example.models.FarmProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class EditProductActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private EditText stockEditText;
    private RadioButton radioButtonFruit;
    private RadioButton radioButtonVegetable;
    private RadioButton radioButtonEgg;
    private RadioButton radioButtonMilk;
    private String selectedProductType = "";

    private ImageView productImageView;
    private Button chooseImageButton;

    private Uri selectedImageUri;
    private static final int IMAGE_PICKER_REQUEST = 1;

    private StorageReference storageReference;
    private Button updateButton;
    private Button cancelButton;

    private FarmProductModel product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        nameEditText = findViewById(R.id.productname);
        descriptionEditText = findViewById(R.id.editTextDescription);
        priceEditText = findViewById(R.id.Price);
        stockEditText = findViewById(R.id.stock);

        radioButtonFruit = findViewById(R.id.radioButtonFruit);
        radioButtonVegetable = findViewById(R.id.radioButtonVegetable);
        radioButtonEgg = findViewById(R.id.radioButtonEgg);
        radioButtonMilk = findViewById(R.id.radioButtonMilk);

        productImageView = findViewById(R.id.product_image_view);
        chooseImageButton = findViewById(R.id.choose_image_button);

        storageReference = FirebaseStorage.getInstance().getReference();

        updateButton = findViewById(R.id.buttonupdate);
        cancelButton = findViewById(R.id.cancelProduct);

        product = (FarmProductModel) getIntent().getSerializableExtra("Edit");

        String documentId = getIntent().getStringExtra("productDocumentId");
        if (product != null) {
            product.setDocumentId(documentId);
            nameEditText.setText(product.getName());
            descriptionEditText.setText(product.getDescription());
            priceEditText.setText(String.valueOf(product.getPrice()));
            stockEditText.setText(String.valueOf(product.getStock()));
            setSelectedProductType(product.getType());

            Glide.with(this).load(product.getImg_url()).into(productImageView);
            chooseImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImagePicker();
                }
            });
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product == null) {
                    Toast.makeText(EditProductActivity.this, "Product information is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }
                String documentId = getIntent().getStringExtra("productDocumentId");
                product = (FarmProductModel) getIntent().getSerializableExtra("product");

                updateProduct(documentId);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            productImageView.setImageURI(imageUri);
            selectedImageUri = imageUri;
        }
    }

    private void setSelectedProductType(String type) {
        switch (type) {
            case "fruit":
                radioButtonFruit.setChecked(true);
                selectedProductType = "fruit";
                break;
            case "vegetable":
                radioButtonVegetable.setChecked(true);
                selectedProductType = "vegetable";
                break;
            case "egg":
                radioButtonEgg.setChecked(true);
                selectedProductType = "egg";
                break;
            case "milk":
                radioButtonMilk.setChecked(true);
                selectedProductType = "milk";
                break;
        }
    }

    private void updateProduct(String documentId) {
        String updatedName = nameEditText.getText().toString();
        String updatedDescription = descriptionEditText.getText().toString();
        int updatedPrice = Integer.parseInt(priceEditText.getText().toString());
        int updatedStock = Integer.parseInt(stockEditText.getText().toString());

        product.setName(updatedName);
        product.setDescription(updatedDescription);
        product.setPrice(updatedPrice);
        product.setType(selectedProductType);
        product.setStock(updatedStock);

        String currentDateTime = getCurrentDateTime();
        product.setDatetime(currentDateTime);

        updateProductData();
    }

    private void updateProductData() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String documentId = product.getDocumentId();
        DocumentReference productRef = firestore.collection("AllProducts").document(documentId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", product.getName());
        updates.put("description", product.getDescription());
        updates.put("price", product.getPrice());
        updates.put("type", product.getType());
        updates.put("stock", product.getStock());
        updates.put("datetime", product.getDatetime());

        productRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("Error", "Failed to update product", e);
                        Toast.makeText(EditProductActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
