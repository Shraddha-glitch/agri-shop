package com.example.agrishopapp.farmer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.agrishopapp.LoginPage;
import com.example.agrishopapp.R;
import com.example.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class farmerdashboard extends AppCompatActivity implements View.OnClickListener {

    private TextView usernameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmerdashboard);


        usernameTextView = findViewById(R.id.username_text_view);

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if (userModel != null) {
                            String username = userModel.getUsername();
                            usernameTextView.setText(username);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error if needed
                    }
                });


        // Find the CardView elements
        CardView addProductCard = findViewById(R.id.addProduct);
        CardView viewProductCard = findViewById(R.id.viewProduct);
        CardView viewOrderCard = findViewById(R.id.viewOrder);
        CardView viewReviewCard = findViewById(R.id.viewReview);
        CardView farmerProfileCard = findViewById(R.id.farmerProfile);
        CardView logoutCard = findViewById(R.id.logout);

        // Set click listeners to the CardView elements
        addProductCard.setOnClickListener(this);
        viewProductCard.setOnClickListener(this);
        viewOrderCard.setOnClickListener(this);
        viewReviewCard.setOnClickListener(this);
        farmerProfileCard.setOnClickListener(this);
        logoutCard.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        // Handle click events based on the CardView ID
        switch (v.getId()) {
            case R.id.addProduct:
                openAddProductActivity();
                break;
            case R.id.viewProduct:
                openViewProductActivity();
                break;
            case R.id.viewOrder:
                openViewOrderActivity();
                break;
            case R.id.viewReview:
                openViewReviewActivity();
                break;
            case R.id.farmerProfile:
                openProfileActivity();
                break;
            case R.id.logout:
                // Implement logout functionality here (e.g., clear session data)
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginPage.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void openAddProductActivity() {


        // Start the AddProduct activity
        Intent intent = new Intent(this, AddProduct.class);
        startActivity(intent);
    }

    private void openViewProductActivity() {
        String farmerUsername = usernameTextView.getText().toString();
        Intent intent = new Intent(this, ViewProductActivity.class);
        intent.putExtra("FarmerUsername", farmerUsername);
        startActivity(intent);
    }

    private void openViewOrderActivity() {
        Intent intent = new Intent(this, ViewOrderActivity.class);
        startActivity(intent);
    }

    private void openViewReviewActivity() {
        Intent intent = new Intent(this, ViewReviewActivity.class);
        startActivity(intent);
    }

    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        }
    }

