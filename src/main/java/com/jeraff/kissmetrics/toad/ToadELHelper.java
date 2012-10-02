package com.jeraff.kissmetrics.toad;

import com.jeraff.kissmetrics.client.KissMetricsException;
import com.jeraff.kissmetrics.client.KissMetricsProperties;
import com.jeraff.kissmetrics.client.el.JeraffELResolver;
import com.jeraff.kissmetrics.toad.Toad;
import com.jeraff.kissmetrics.toad.ToadProvider;
import com.jeraff.kissmetrics.toad.aop.Alias;
import com.jeraff.kissmetrics.toad.aop.Kissmetrics;
import com.jeraff.kissmetrics.toad.aop.Record;
import com.jeraff.kissmetrics.toad.aop.Set;

import javax.el.ExpressionFactory;
import java.util.Map;

public class ToadELHelper {
    private final static String EL_PREFIX = "${";
    private ToadProvider toadProvider;
    private JeraffELResolver elResolver;

    public ToadELHelper(ToadProvider toadProvider) {
        this.toadProvider = toadProvider;
    }

    public void populateToad(Kissmetrics annotation) throws KissMetricsException {
        Toad toad = toadProvider.getToad();

        // alias annotations
        for (Alias alias : annotation.alias()) {
            toad.user(getClientId(alias.id()))
                    .alias((String)resolve(alias.to()));
        }

        // set annotations
        for (Set set : annotation.set()) {
            if (!set.props().isEmpty()) {
                toad.user(getClientId(set.id()))
                        .set((KissMetricsProperties) resolve(set.props()));
            } else {
                toad.user(getClientId(set.id()))
                        .set((String)resolve(set.key()), (String)resolve(set.value()));
            }
        }

        // record annotations
        for (Record record : annotation.record()) {
            KissMetricsProperties props = (!record.props().isEmpty()) ?
                    (KissMetricsProperties) resolve(record.props()) :
                    new KissMetricsProperties();
            toad.user(getClientId(record.id()))
                    .record((String)resolve(record.event()), props);
        }
    }

    public Object resolve(String expression) throws KissMetricsException {
        if (expression.startsWith(EL_PREFIX)) {
            if (elResolver == null) {
                try {
                    if (toadProvider.getToad().getExpressionFactory() != null) {
                        elResolver = new JeraffELResolver(toadProvider.getToad().getExpressionFactory(), toadProvider);
                    }
                } catch (Exception e) {
                    throw new KissMetricsException("Couldn't instantiate EL Resolver", e);
                }
            }

            if (elResolver != null) {
                final Object value = elResolver.getValue(expression);

                // convert map object to KissMetricsProperties
                if (value instanceof Map) {
                    KissMetricsProperties props = convertMapToProps((Map)value);
                    return props;
                }

                return value;
            }
        }

        return expression;
    }

    private String getClientId(String id) throws KissMetricsException {
        if (id.isEmpty()) {
            return toadProvider.getDefaultKissClientId();
        }

        return (String)resolve(id);
    }

    private KissMetricsProperties convertMapToProps (Map mapToConvert) throws KissMetricsException {
        KissMetricsProperties props = new KissMetricsProperties();

        try {
            for (Object key : mapToConvert.keySet()) {
                props.put(key.toString(), mapToConvert.get(key).toString());
            }
        } catch(Exception e) {
            throw new KissMetricsException("Error converting map to KissMetricsProperties", e);
        }

        return props;
    }
}
