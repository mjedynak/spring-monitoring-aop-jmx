package pl.mjedynak.multiple_aspects_single_metric.metric;

import org.springframework.integration.monitor.ExponentialMovingAverage;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import static org.springframework.integration.monitor.DirectChannelMetrics.DEFAULT_MOVING_AVERAGE_WINDOW;

@ManagedResource(objectName="bean:name1=testBean1") //FIXME: exported via JMX only once even though is prototype
public class InvocationTimeMetric {

    private final ExponentialMovingAverage exponentialMovingAverage = new ExponentialMovingAverage(DEFAULT_MOVING_AVERAGE_WINDOW);

    public void updateMetric(long milliseconds) {
        exponentialMovingAverage.append(milliseconds);
    }

    @ManagedAttribute
    public String getStatistics() {
        return exponentialMovingAverage.toString();
    }

    @ManagedAttribute
    public double getMean() {
        return exponentialMovingAverage.getMean();
    }


}
