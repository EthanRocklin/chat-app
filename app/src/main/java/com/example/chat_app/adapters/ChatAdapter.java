package com.example.chat_app.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.databinding.ItemContainerReceivedBinding;
import com.example.chat_app.databinding.ItemContainerSentBinding;
import com.example.chat_app.modules.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Bitmap receiverProfileImage;
    private final List<ChatMessage> chatMessages;
    private final String senderID;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_SENT = 1;

    /**
     *
     * @param chatMessages
     * @param receiverProfileImage
     * @param senderID
     */
    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderID) {
        this.receiverProfileImage = receiverProfileImage;
        this.chatMessages = chatMessages;
        this.senderID = senderID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SendMessageViewHolder(ItemContainerSentBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else  {
            return new ReceiverMessageViewHolder(ItemContainerReceivedBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SendMessageViewHolder)holder).setData(chatMessages.get(position));
        } else {
            ((ReceiverMessageViewHolder)holder)
                    .setData(chatMessages.get(position), receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).getSenderID().equals(senderID)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    /**
     *
     */
    static class SendMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerSentBinding binding;

        public SendMessageViewHolder(ItemContainerSentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         *
         * @param message
         */
        void setData(ChatMessage message) {
            binding.textMessage.setText(message.getMessage());
            binding.textTimeStamp.setText(message.getTimeStamp().toString());
        }
    }

    /**
     *
     */
    static class ReceiverMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerReceivedBinding binding;

        public ReceiverMessageViewHolder(ItemContainerReceivedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         *
         * @param message
         */
        void setData(ChatMessage message, Bitmap receiverProfileImage) {
            binding.textMessage.setText(message.getMessage());
            binding.textTimeStamp.setText(message.getTimeStamp().toString());
            binding.imageProfile.setImageBitmap(receiverProfileImage);
        }
    }

}
