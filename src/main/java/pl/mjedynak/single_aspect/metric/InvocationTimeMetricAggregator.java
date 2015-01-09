package pl.mjedynak.single_aspect.metric;

import org.springframework.integration.monitor.ExponentialMovingAverage;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.integration.monitor.DirectChannelMetrics.DEFAULT_MOVING_AVERAGE_WINDOW;

@Component
@ManagedResource
public class InvocationTimeMetricAggregator {

    private static final String FAST_SERVICE = "pl.mjedynak.service.FastTimeService";
    private static final String SLOW_SERVICE = "pl.mjedynak.service.SlowTimeService";

    private Map<String, ExponentialMovingAverage> map = new ConcurrentHashMap<>();

    public InvocationTimeMetricAggregator() {
        map.put(FAST_SERVICE, new ExponentialMovingAverage(DEFAULT_MOVING_AVERAGE_WINDOW));
        map.put(SLOW_SERVICE, new ExponentialMovingAverage(DEFAULT_MOVING_AVERAGE_WINDOW));
    }

    public void updateMetric(String name, long milliseconds) {
        if (map.containsKey(name)) {
            ExponentialMovingAverage ema = map.get(name);
            ema.append(milliseconds);
        }
    }

    @ManagedAttribute
    public String getStatistics() {
        return map.toString();
    }

    @ManagedAttribute
    public double getFastTimeServiceMean() {
        return map.get(FAST_SERVICE).getMean();
    }

    @ManagedAttribute
    public double getSlowTimeServiceMean() {
        return map.get(SLOW_SERVICE).getMean();
    }

}
