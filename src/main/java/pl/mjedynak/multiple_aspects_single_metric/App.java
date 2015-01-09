package pl.mjedynak.multiple_aspects_single_metric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.mjedynak.service.TimeService;

import java.io.IOException;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TimeService fastTimeService = context.getBean("fastTimeService", TimeService.class);
        TimeService slowTimeService = context.getBean("slowTimeService", TimeService.class);
        logger.debug(fastTimeService.getCurrentTime().toString());
        logger.debug(fastTimeService.getCurrentTime().toString());
        logger.debug(slowTimeService.getCurrentTime().toString());
        logger.debug(slowTimeService.getCurrentTime().toString());
        logger.debug(slowTimeService.getCurrentTime().toString());
        System.in.read();
    }
}
