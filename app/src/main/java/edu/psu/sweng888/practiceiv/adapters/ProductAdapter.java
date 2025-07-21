package edu.psu.sweng888.practiceiv.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import edu.psu.sweng888.practiceiv.R;
import edu.psu.sweng888.practiceiv.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> products;        // list of products to display
    private DatabaseReference productsRef;      // reference to Firebase "products" node

    public ProductAdapter(ArrayList<Product> products, DatabaseReference productsRef) {
        this.products = products;
        this.productsRef = productsRef;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate a single row layout for each product
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Bind product data to UI
        Product product = products.get(position);

        holder.nameTextView.setText(product.getName());
        holder.descriptionTextView.setText(product.getDescription());
        holder.priceTextView.setText(String.valueOf(product.getPrice()));

        // Handle delete button click
        holder.deleteButton.setOnClickListener(v -> {
            if (product.getId() == null) {
                Toast.makeText(holder.itemView.getContext(),
                        "Invalid product ID, cannot delete.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show confirmation dialog before deleting
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete \"" + product.getName() + "\"?")
                    .setPositiveButton("Yes", (dialog, which) ->
                            // Remove product from Firebase on confirmation
                            productsRef.child(product.getId()).removeValue()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(holder.itemView.getContext(),
                                                    "Deleted " + product.getName(),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(holder.itemView.getContext(),
                                                    "Delete failed: " + (task.getException() != null
                                                            ? task.getException().getMessage()
                                                            : ""),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return products.size(); // number of products in the list
    }

    // ViewHolder holds references to the UI elements in each row
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView, priceTextView;
        Button deleteButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            descriptionTextView = itemView.findViewById(R.id.textViewDesc);
            priceTextView = itemView.findViewById(R.id.textViewPrice);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
