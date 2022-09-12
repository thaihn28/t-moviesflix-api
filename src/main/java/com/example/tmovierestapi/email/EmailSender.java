package com.example.tmovierestapi.email;

public interface EmailSender {
    void send(String recipient, String name, String link);
    void notifyNewMovie(String recipient, String name, String link);
}
