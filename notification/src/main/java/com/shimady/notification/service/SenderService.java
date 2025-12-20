package com.shimady.notification.service;

public interface SenderService<T> {

    void send(T message);
}
