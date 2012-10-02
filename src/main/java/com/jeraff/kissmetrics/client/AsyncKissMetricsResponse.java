package com.jeraff.kissmetrics.client;

import java.util.HashMap;

public class AsyncKissMetricsResponse extends KissMetricsResponse {
    private Throwable error;

    public AsyncKissMetricsResponse(int status) {
        super(status, new HashMap<String, String>());
    }

    public AsyncKissMetricsResponse(int status, Throwable error) {
        super(status, new HashMap<String, String>());
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    @Override
    public boolean isOK() {
        return error == null && super.isOK();
    }
}
