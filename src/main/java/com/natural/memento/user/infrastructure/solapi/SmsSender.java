package com.natural.memento.user.infrastructure.solapi;

public interface SmsSender {
    void send(String to, String text);
}