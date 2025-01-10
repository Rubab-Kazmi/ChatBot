package com.example.chatbot.models;

public class MessageModel {
        private boolean isUser;
        private boolean isImage;
        private String message;

        public MessageModel(boolean isUser, boolean isImage, String message) {
            this.isUser = isUser;
            this.isImage = isImage;
            this.message = message;
        }

        public boolean isUser() {
            return isUser;
        }

        public void setUser(boolean user) {
            isUser = user;
        }

        public boolean isImage() {
            return isImage;
        }

        public void setImage(boolean image) {
            isImage = image;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
