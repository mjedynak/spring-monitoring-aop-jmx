package pl.mjedynak.multiple_aspects_single_metric.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import pl.mjedynak.multiple_aspects_single_metric.metric.InvocationTimeMetric;

@Component
public abstract class AbstractMonitoredInvocationTimeAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired protected InvocationTimeMetric invocationTimeMetric;

    public Object invokeWithMonitoring(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("Invoking " + joinPoint.getSignature().toShortString());
        StopWatch timer = new StopWatch();
        timer.start();
        Object result = joinPoint.proceed();
        timer.stop();
        invocationTimeMetric.updateMetric(timer.getTotalTimeMillis());
        return result;
    }
}
