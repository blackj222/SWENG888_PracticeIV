package edu.psu.sweng888.practiceiv.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.psu.sweng888.practiceiv.R;
import edu.psu.sweng888.practiceiv.models.Product;

import java.util.UUID;

public class AddItemFragment extends Fragment {

    private EditText editTextName, editTextDescription, editTextPrice;
    private Button buttonSave;

    private DatabaseReference productsRef;

    public AddItemFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextName = view.findViewById(R.id.editTextItemName);
        editTextDescription = view.findViewById(R.id.editTextItemDescription);
        editTextPrice = view.findViewById(R.id.editTextItemPrice);
        buttonSave = view.findViewById(R.id.buttonSaveItem);

        productsRef = FirebaseDatabase.getInstance().getReference("products");

        buttonSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name required");
            return;
        }
        if (TextUtils.isEmpty(priceStr)) {
            editTextPrice.setError("Price required");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            editTextPrice.setError("Invalid price");
            return;
        }

        // Create a unique ID for the product
        String id = UUID.randomUUID().toString();
        Product product = new Product(id, name, description, price);

        productsRef.child(id).setValue(product)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Item added!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(getContext(), "Failed: " +
                                        (task.getException() != null ? task.getException().getMessage() : ""),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void clearFields() {
        editTextName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
    }
}
