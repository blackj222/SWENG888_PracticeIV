package edu.psu.sweng888.practiceiv;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.psu.sweng888.practiceiv.models.User;

public class UsersActivity extends AppCompatActivity {

    private DatabaseReference refUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users); // make sure this layout exists!

        // ✅ Get reference to "users" node
        refUsers = FirebaseDatabase.getInstance().getReference("users");

        // ✅ Run as soon as activity opens
        checkExistence();

        Toast.makeText(UsersActivity.this, "No users found.", Toast.LENGTH_SHORT).show();
    }

    private void checkExistence() {
        refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // ✅ The "users" node exists in the database
                    Log.d("CheckUsers", "Users node exists with " + snapshot.getChildrenCount() + " children.");
                } else {
                    // ❌ The "users" node does not exist
                    Log.d("CheckUsers", "Users node does NOT exist.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CheckUsers", "Error checking users node: " + error.getMessage());
            }
        });
    }

    private void loadAllUsers() {
        refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> userList = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                        Log.d("UsersActivity", "Loaded user: " + user.getName());
                    }
                }

                if (userList.isEmpty()) {
                    Toast.makeText(UsersActivity.this, "No users found.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UsersActivity.this, "Loaded " + userList.size() + " users!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UsersActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
