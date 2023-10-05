package com.example.agrishopapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class AboutUsFragment extends Fragment {
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_us, container, false);

        // Initialize FirebaseDatabase and FirebaseAuth
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        EditText nameEditText = root.findViewById(R.id.nameEditText);
        EditText addressEditText = root.findViewById(R.id.addressEditText);
        EditText reviewEditText = root.findViewById(R.id.reviewEditText);
        Button submitButton = root.findViewById(R.id.submitButton);

        // Handle submit button click
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditText fields
                String name = nameEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String review = reviewEditText.getText().toString();

                // Store the values in Firestore
                storeContactFormInfo(name, address, review);
            }
        });

        return root;
    }

    private void storeContactFormInfo(String name, String address, String review) {
        // Get the Firestore instance
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Create a new ContactFormInfo object
        ContactFormInfo contactFormInfo = new ContactFormInfo(name, address, review);

        // Store the data in Firestore
        firestore.collection("ContactForms")
                .document(auth.getCurrentUser().getUid())
                .set(contactFormInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Contact form submitted successfully.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error submitting contact form.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
