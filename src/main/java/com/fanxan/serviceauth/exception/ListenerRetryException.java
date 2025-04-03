package com.fanxan.serviceauth.exception;

import org.springframework.http.HttpStatus;

public class ListenerRetryException extends CustomListenerException {
    public ListenerRetryException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR.value(), "LISTENER_PROCESS_FAILED", "error.rabbitmq.listener.process_failed");
    }
}
