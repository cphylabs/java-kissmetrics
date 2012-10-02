package com.jeraff.kissmetrics.client;

import com.jeraff.kissmetrics.client.util.URLUTF8Encoder;

public class KissMetricsProperties {
    private StringBuilder sb = new StringBuilder();

    private void _put(String key, Object value) {
        if (value != null) {
            sb.append(URLUTF8Encoder.encode(key)).append("=").append(URLUTF8Encoder.encode(value.toString()))
              .append("&");
        }
    }

    public KissMetricsProperties put(String key, String value) {
        _put(key, value);
        return this;
    }

    public KissMetricsProperties put(String key, int value) {
        _put(key, value);
        return this;
    }

    public KissMetricsProperties put(String key, float value) {
        _put(key, value);
        return this;
    }

    public KissMetricsProperties put(String key, double value) {
        _put(key, value);
        return this;
    }

    public KissMetricsProperties put(String key, long value) {
        _put(key, value);
        return this;
    }

    public KissMetricsProperties put(String key, boolean value) {
        _put(key, value);
        return this;
    }

    public KissMetricsProperties putSafe(String key, String value) {
        final int starts = sb.indexOf(key + "=");

        if (starts != -1) {
            final int ends = sb.indexOf("&", starts) + 1; // + 1 to remove the & sign too
            sb.delete(starts, ends);
        }

        _put(key, value);
        return this;
    }

    public void clear() {
        sb = new StringBuilder();
    }

    public String getQueryString() {
        return sb.toString();
    }

    public void addAll(KissMetricsProperties props, boolean strict) {
        if (strict) {
            final String[] tokens = props.getQueryString().split("&");
            for (String keyValuePair : tokens) {
                final String[] prop = keyValuePair.split("=");

                // invalidly formatted so ignore
                if (prop.length != 2) {
                    continue;
                }

                final String key = prop[0];
                final String value = prop[1];

                putSafe(key, value);
            }
        } else {
            // just do a dumb append
            sb.append(props.getQueryString());
        }
    }
}
