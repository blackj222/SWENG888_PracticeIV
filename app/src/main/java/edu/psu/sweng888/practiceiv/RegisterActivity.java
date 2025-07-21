package edu.psu.sweng888.practiceiv;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.UUID;

import edu.psu.sweng888.practiceiv.models.User;

public class RegisterActivity extends AppCompatActivity {

    final private String TAG = "register_activity";
    private EditText nameInput, emailInput, passwordInput, confirmInput;
    private Button registerButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.editTextName);
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        confirmInput = findViewById(R.id.editTextConfirm);
        registerButton = findViewById(R.id.buttonRegister);
        Button registerButton2 = findViewById(R.id.buttonRegister2);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        registerButton.setOnClickListener(v -> registerUser3());
        registerButton2.setOnClickListener(v -> registerUser2());

        // Enable the back arrow in the top app bar
        Button btnGoBack = findViewById(R.id.goBackButton);
        btnGoBack.setOnClickListener(v -> finish());

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // This runs once with all data at "users"
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Map to your model or get fields directly
                    User user = userSnapshot.getValue(User.class);

                    if (user != null) {
                        // Do something with the user
                        Log.d("Users", "Name: " + user.getName() + ", Email: " + user.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Users", "Failed to read users: " + error.getMessage());
            }
        });
    }

    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirm = confirmInput.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            return;
        }
        if (!password.equals(confirm)) {
            confirmInput.setError("Passwords do not match");
            return;
        }

        progressBar.setVisibility(android.view.View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(android.view.View.GONE);
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();

                            // Save extra details in database
                            usersRef.child(uid).child("name").setValue(name);
                            usersRef.child(uid).child("email").setValue(email);

                            Toast.makeText(RegisterActivity.this, "Account created!", Toast.LENGTH_SHORT).show();

                            // Redirect to Login or Main
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Registration failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void registerUser2() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirm = confirmInput.getText().toString().trim();

//        if (TextUtils.isEmpty(name)) {
//            nameInput.setError("Name is required");
//            return;
//        }
//        if (TextUtils.isEmpty(email)) {
//            emailInput.setError("Email is required");
//            return;
//        }
//        if (TextUtils.isEmpty(password)) {
//            passwordInput.setError("Password is required");
//            return;
//        }
//        if (!password.equals(confirm)) {
//            confirmInput.setError("Passwords do not match");
//            return;
//        }

        progressBar.setVisibility(android.view.View.VISIBLE);
        // String uid = mAuth.getCurrentUser().getUid();

        String newUUID = UUID.randomUUID().toString();
        User newUser = new User(newUUID, "John Doe", "john@example.com", "Qwerty1!");

        usersRef.child(newUUID).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("UsersActivity", "Write successful!");
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.e("UsersActivity", "Write failed: " + task.getException());
                    }
                });

        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
        if (current != null) {
            usersRef.child(current.getUid()).setValue(newUser);
        } else {
            Log.e("UsersActivity", "No user is signed in!");
        }

        progressBar.setVisibility(View.GONE);

        /*
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("send_user", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("send_user", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
         */
    }

    private void registerUser3() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirm = confirmInput.getText().toString().trim();

//        if (TextUtils.isEmpty(name)) {
//            nameInput.setError("Name is required");
//            return;
//        }
//        if (TextUtils.isEmpty(email)) {
//            emailInput.setError("Email is required");
//            return;
//        }
//        if (TextUtils.isEmpty(password)) {
//            passwordInput.setError("Password is required");
//            return;
//        }
//        if (!password.equals(confirm)) {
//            confirmInput.setError("Passwords do not match");
//            return;
//        }

        progressBar.setVisibility(android.view.View.VISIBLE);
        // String uid = mAuth.getCurrentUser().getUid();

        String newUUID = UUID.randomUUID().toString();
        User newUser = new User(newUUID, "John Doe", "john@example.com", "Qwerty1!");
        email = newUser.getEmail();
        password = newUser.getPassword();;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        progressBar.setVisibility(View.GONE);

        /*
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("send_user", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("send_user", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
         */
    }
}
