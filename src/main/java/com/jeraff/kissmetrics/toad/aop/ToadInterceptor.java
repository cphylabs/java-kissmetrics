package com.jeraff.kissmetrics.toad.aop;

import com.jeraff.kissmetrics.client.KissMetricsException;
import com.jeraff.kissmetrics.toad.Toad;
import com.jeraff.kissmetrics.toad.ToadELHelper;
import com.jeraff.kissmetrics.toad.ToadProvider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ToadInterceptor implements MethodInterceptor {
    private ToadProvider toadProvider;
    private Toad toad;
    private ToadELHelper elHelper;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = null;
        try {
            toadProvider = (ToadProvider) invocation.getThis();

            if (toadProvider != null) {
                toad = toadProvider.getToad();

                if (toad != null) {
                    elHelper = new ToadELHelper(toadProvider);

                    if (elHelper != null) {
                        final Kissmetrics kissAnnotation = invocation.getMethod().getAnnotation(Kissmetrics.class);

                        if (kissAnnotation != null) {
                            if (kissAnnotation.runBefore()) {
                                elHelper.populateToad(kissAnnotation);
                                toad.run();
                                return invocation.proceed();
                            }

                            result = invocation.proceed();
                            elHelper.populateToad(kissAnnotation);
                            toad.run();
                            return result;
                        }
                    }
                }
            }
        }
        catch(KissMetricsException ke) {
            // swallow any of our own errors and make sure to still run the invocation
            if (result == null) {
                return invocation.proceed();
            }
            System.err.println("ToadInterceptor:invoke - KissMetricsException caught");
        }
        catch(Exception e) {
            // something in the ToadProvider's code threw an exception, just let this fall through and return whatever result
            // we got
            System.err.println("ToadInterceptor:invoke - Exception caught");
        }

        return result;
    }
}
