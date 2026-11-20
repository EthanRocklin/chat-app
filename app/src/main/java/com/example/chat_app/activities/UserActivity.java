package com.example.chat_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chat_app.adapters.UsersAdapter;
import com.example.chat_app.databinding.ActivityUsersBinding;
import com.example.chat_app.listeners.UserListener;
import com.example.chat_app.modules.User;
import com.example.chat_app.utilities.Constants;
import com.example.chat_app.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        setListeners();
        getUsers();
    }

    /**
     *
     */
    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    /**
     *
     */
    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS).get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            if (currentUserId.equals(snapshot.getId())) {
                                continue;
                            }
                            //
                            User user = new User();
                            user.setName(snapshot.getString(Constants.KEY_FIRST_NAME));
                            user.setEmail(snapshot.getString(Constants.KEY_EMAIL));
                            user.setImage(snapshot.getString(Constants.KEY_IMAGE));
                            user.setId(snapshot.getId());
                            user.setToken(snapshot.getString(Constants.KEY_FCM_TOKEN));

                            Log.d("UserActivity", "docId=" + snapshot.getId()
                                    + " userid=" + snapshot.getString(Constants.KEY_USER_ID)
                                    + " fields=" + snapshot.getData().keySet()
                                    + " | user.getId()=" + user.getId());

                            users.add(user);
                        }
                        //
                        if (users.size() > 0) {
                            UsersAdapter usersAdapater = new UsersAdapter(users, this);
                            binding.usersRecyclerView.setAdapter(usersAdapater);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }
                });
    }

    /**
     *
     * @param isLoading
     */
    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     *
     */
    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No users available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    /**
     *
     * @param user
     */
    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}