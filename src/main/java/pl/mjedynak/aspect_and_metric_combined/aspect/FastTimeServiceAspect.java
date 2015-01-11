package pl.mjedynak.aspect_and_metric_combined.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FastTimeServiceAspect extends AbstractMonitoredInvocationTimeAspect {

    @Around("execution(* pl.mjedynak.service.FastTimeService.getCurrentTime())")
    public Object monitoredInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        return invokeWithMonitoring(joinPoint);
    }

}
