package pl.mjedynak.single_aspect_aggregated_metric.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import pl.mjedynak.single_aspect_aggregated_metric.metric.InvocationTimeMetricAggregator;

@Aspect
@Component
public class MonitoredInvocationTimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(MonitoredInvocationTimeAspect.class.getName());

    @Autowired private InvocationTimeMetricAggregator invocationTimeMetricAggregator;

    @Around("@annotation(pl.mjedynak.single_aspect_aggregated_metric.annotation.MonitoredInvocationTime)")
    public Object monitoredInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = getName(joinPoint);
        logger.debug("Invoking " + name + " " + joinPoint.getSignature().toShortString());
        StopWatch timer = new StopWatch();
        timer.start();
        Object result = joinPoint.proceed();
        timer.stop();
        invocationTimeMetricAggregator.updateMetric(name, timer.getTotalTimeMillis());
        return result;
    }

    private String getName(ProceedingJoinPoint joinPoint) {
        String targetName = joinPoint.getTarget().toString();
        int atCharIndex = targetName.indexOf('@');
        return targetName.substring(0, atCharIndex);
    }

}
