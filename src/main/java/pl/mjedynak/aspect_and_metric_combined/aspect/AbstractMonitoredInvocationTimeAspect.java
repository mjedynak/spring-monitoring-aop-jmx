package pl.mjedynak.aspect_and_metric_combined.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.monitor.ExponentialMovingAverage;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import static org.springframework.integration.monitor.DirectChannelMetrics.DEFAULT_MOVING_AVERAGE_WINDOW;

@Component
@ManagedResource
public abstract class AbstractMonitoredInvocationTimeAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Object invokeWithMonitoring(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("Invoking " + joinPoint.getSignature().toShortString());
        StopWatch timer = new StopWatch();
        timer.start();
        Object result = joinPoint.proceed();
        timer.stop();
        exponentialMovingAverage.append(timer.getTotalTimeMillis());
        return result;
    }

    private final ExponentialMovingAverage exponentialMovingAverage = new ExponentialMovingAverage(DEFAULT_MOVING_AVERAGE_WINDOW);

    @ManagedAttribute
    public String getStatistics() {
        return exponentialMovingAverage.toString();
    }

    @ManagedAttribute
    public double getMean() {
        return exponentialMovingAverage.getMean();
    }
}
