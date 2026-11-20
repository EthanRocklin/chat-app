package com.example.chat_app.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chat_app.adapters.ChatAdapter;
import com.example.chat_app.databinding.ActivityChatBinding;
import com.example.chat_app.modules.ChatMessage;
import com.example.chat_app.modules.User;
import com.example.chat_app.utilities.Constants;
import com.example.chat_app.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore database;
    private List<ChatMessage> chatMessages;
    private PreferenceManager preferenceManager;
    private User receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadReceiverDetails();
        setListeners();
        init();
        listenToMessage();
    }

    /**
     *
     */
    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }

    /**
     *
     */
    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                getBitmapFromEncodedString(receiver.getImage()),
                preferenceManager.getString(Constants.KEY_USER_ID).toString()
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    /**
     * This method loads all messages from the sender and receiver that belong to the
     * currently signed-in user.
     */
    private void listenToMessage() {
        // All the messages sent to the other user
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID,
                        preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiver.getId())
                .addSnapshotListener(eventListener);
        // All the messages sent to the signed-in user
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID,
                        receiver.getId())
                .whereEqualTo(Constants.KEY_RECEIVER_ID,
                        preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    /**
     *
     */
    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        if (error != null) {
            return;
        }
        //
        if (value != null) {
            int count = chatMessages.size();
            for (DocumentChange docChange : value.getDocumentChanges()) {
                if (docChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSenderID(docChange.getDocument().getString(Constants.KEY_SENDER_ID));
                    chatMessage.setReceiverID(docChange.getDocument().getString(Constants.KEY_RECEIVER_ID));
                    chatMessage.setMessage(docChange.getDocument().getString(Constants.KEY_MESSAGE));
                    chatMessage.setTimeStamp(
                            getReadableDateTime(docChange.getDocument().getDate(Constants.KEY_TIMESTAMP)));
                    chatMessage.setDateObject(docChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.getDateObject().compareTo(obj2.getDateObject()));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(count, chatMessages.size() - count);
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            //
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        //
        binding.progressBar.setVisibility(View.GONE);
    });


    /**
     *
     * @param encodedImage
     * @return
     */
    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     *
     * @param date
     * @return
     */
    private String getReadableDateTime (Date date) {
        return new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    /**
     *
     */
    private void loadReceiverDetails() {
        receiver = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiver.getName());
    }

    /**
     *
     */
    private void sendMessage() {
        String text = binding.inputMessage.getText().toString().trim();
        if (text.isEmpty()) {
            return;
        }
        //
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiver.getId());
        message.put(Constants.KEY_MESSAGE, text);
        message.put(Constants.KEY_TIMESTAMP, new Date());
        //
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message)
                .addOnFailureListener(e -> Log.e("ChatActivity", "Send failed", e));
        binding.inputMessage.setText(null);
    }

}
