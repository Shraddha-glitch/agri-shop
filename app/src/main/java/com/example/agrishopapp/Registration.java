package com.example.agrishopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agrishopapp.farmer.farmerdashboard;
import com.example.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity {


    private EditText editTextUsername, editTextFullName, editTextPassword, editTextReenterPassword, editTextEmail;
    private RadioGroup radioGroupUserType;
    private Button buttonRegister;
    private TextView login;
    FirebaseAuth auth;
    FirebaseDatabase database;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextReenterPassword = findViewById(R.id.editTextReenterPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        radioGroupUserType = findViewById(R.id.radioGroupUserType);
        buttonRegister = findViewById(R.id.buttonRegister);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, LoginPage.class));
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    private void createUser() {
        final String userName = editTextUsername.getText().toString();
        String fullName = editTextFullName.getText().toString();
        String password = editTextPassword.getText().toString();
        String rePassword = editTextReenterPassword.getText().toString();
        String email = editTextEmail.getText().toString();
        String userType;

        if (userName.isEmpty() || fullName.isEmpty() || password.isEmpty() || rePassword.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password length must be greater than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(rePassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the username already exists
        DatabaseReference usernameRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usernameRef.orderByChild("username").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Username already exists
                    Toast.makeText(Registration.this, "Username already taken", Toast.LENGTH_SHORT).show();
                } else {
                    // Username is available, proceed with registration
                    registerUser(userName, fullName, email, password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void registerUser(String userName, String fullName, String email, String password) {
        final String userType;
        int selectedRadioButtonId = radioGroupUserType.getCheckedRadioButtonId();
        if (selectedRadioButtonId == R.id.radioButtonFarmer) {
            userType = "Farmer";
        } else if (selectedRadioButtonId == R.id.radioButtonCustomer) {
            userType = "Customer";
        } else {
            // Handle the case where no radio button is selected
            Toast.makeText(this, "Please select a user type", Toast.LENGTH_SHORT).show();
            return;
        }

        //create User
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            UserModel userModel = new UserModel(userName, fullName, email, password, userType);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(userModel);
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(Registration.this, "Registration Success", Toast.LENGTH_SHORT).show();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Registration.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
