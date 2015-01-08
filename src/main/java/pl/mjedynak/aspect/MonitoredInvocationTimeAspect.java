package pl.mjedynak.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import pl.mjedynak.metric.InvocationTimeMetric;

@Aspect
@Component
public class MonitoredInvocationTimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(MonitoredInvocationTimeAspect.class.getName());

    @Autowired private InvocationTimeMetric invocationTimeMetric;

    @Around("@annotation(pl.mjedynak.annotation.MonitoredInvocationTime)")
    public Object monitoredInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = getName(joinPoint);
        logger.debug("Invoking " + name + " " + joinPoint.getSignature().toShortString());
        StopWatch timer = new StopWatch();
        timer.start();
        Object result = joinPoint.proceed();
        timer.stop();
        invocationTimeMetric.updateMetric(name, timer.getTotalTimeMillis());
        return result;
    }

    private String getName(ProceedingJoinPoint joinPoint) {
        String targetName = joinPoint.getTarget().toString();
        int atCharIndex = targetName.indexOf('@');
        return targetName.substring(0, atCharIndex);
    }

}
