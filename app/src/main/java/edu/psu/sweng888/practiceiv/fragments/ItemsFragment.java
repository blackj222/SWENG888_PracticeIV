package edu.psu.sweng888.practiceiv.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.psu.sweng888.practiceiv.LoginActivity;
import edu.psu.sweng888.practiceiv.R;
import edu.psu.sweng888.practiceiv.RegisterActivity;
import edu.psu.sweng888.practiceiv.adapters.ProductAdapter;
import edu.psu.sweng888.practiceiv.models.Product;

public class ItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ArrayList<Product> productList = new ArrayList<>();
    private DatabaseReference productsRef;
    private FloatingActionButton fabAddItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        // Firebase reference (node: products)
        productsRef = FirebaseDatabase.getInstance().getReference("products");
        fabAddItem = view.findViewById(R.id.fabAddItem);

        // Adapter with delete functionality
        adapter = new ProductAdapter(productList, productsRef);

        recyclerView = view.findViewById(R.id.recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadProducts();

        // Find the FAB
        fabAddItem.setOnClickListener(v -> {
            // Replace current fragment with AddItemFragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddItemFragment())
                    .addToBackStack(null) // allows going back with back button
                    .commit();
        });

        return view;
    }

    private void loadProducts() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Product product = data.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load items.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
