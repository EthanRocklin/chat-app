package com.example.chat_app.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chat_app.R;
import com.example.chat_app.databinding.ActivityChatBinding;
import com.example.chat_app.modules.User;
import com.example.chat_app.utilities.Constants;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private User receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadReceiverDetails();
        setListeners();
    }

    private void loadReceiverDetails() {
        receiver = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiver.getName());
    }

    /**
     *
     */
    private void setListeners() {
       binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

}