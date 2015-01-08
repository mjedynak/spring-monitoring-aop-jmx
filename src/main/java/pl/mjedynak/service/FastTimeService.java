package pl.mjedynak.service;

import org.springframework.stereotype.Component;
import pl.mjedynak.annotation.MonitoredInvocationTime;

import java.time.LocalDateTime;

@Component
public class FastTimeService implements TimeService {

    @MonitoredInvocationTime
    @Override
    public LocalDateTime getCurrentTime() {
//        return LocalDateTime.now();
        throw new RuntimeException("aaaa");
    }
}
