package com.jeraff.kissmetrics.client;

import com.sun.xml.internal.ws.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class KissMetricsPropertiesTest {
    @Test
    public void testPutSafe() throws Exception {
        KissMetricsProperties props = new KissMetricsProperties();
        props.put(KissMetricsClient.PROP_EVENT_NAME, "event1");
        props.putSafe(KissMetricsClient.PROP_EVENT_NAME, "event2");

        Assert.assertEquals(-1, props.getQueryString().indexOf("event1"));
        Assert.assertTrue(props.getQueryString().indexOf("event2") >= 0);
    }

    @Test
    public void testPutAll() throws Exception {
        KissMetricsProperties props = new KissMetricsProperties();
        KissMetricsProperties props1 = new KissMetricsProperties();

        props.putSafe("k1","v1");
        props.putSafe("k2","v2");

        props1.putSafe("k3","v3");
        props1.putSafe("k4","v4");

        props.addAll(props1, false);

        Assert.assertTrue(props.getQueryString().indexOf("k3") >= 0);
        Assert.assertTrue(props.getQueryString().indexOf("v3") >= 0);
        Assert.assertTrue(props.getQueryString().indexOf("k4") >= 0);
        Assert.assertTrue(props.getQueryString().indexOf("v4") >= 0);
    }

    @Test
    public void testPutAllStrict() throws Exception {
        KissMetricsProperties props = new KissMetricsProperties();
        KissMetricsProperties props1 = new KissMetricsProperties();

        props.putSafe("k1","v1");
        props.putSafe("k2","v2");

        props1.putSafe("k2","v2");
        props1.putSafe("k3","v3");

        props.addAll(props1, true);

        final int count = org.apache.commons.lang.StringUtils.countMatches(props.getQueryString(), "k2");

        Assert.assertTrue(props.getQueryString().indexOf("k3") >= 0);
        Assert.assertTrue(props.getQueryString().indexOf("v3") >= 0);
        Assert.assertEquals(1, count);
    }

    @Test
    public void testPutNulls() throws Exception {
        KissMetricsProperties props = new KissMetricsProperties();
        props.put("test", null);
    }
}
