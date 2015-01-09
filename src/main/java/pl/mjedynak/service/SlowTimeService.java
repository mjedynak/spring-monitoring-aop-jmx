package pl.mjedynak.service;

import org.springframework.stereotype.Component;
import pl.mjedynak.single_aspect.annotation.MonitoredInvocationTime;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class SlowTimeService implements TimeService {

    @MonitoredInvocationTime
    @Override
    public LocalDateTime getCurrentTime() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return LocalDateTime.now();
    }
}
