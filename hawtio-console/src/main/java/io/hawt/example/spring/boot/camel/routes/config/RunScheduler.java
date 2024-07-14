package io.hawt.example.spring.boot.camel.routes.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
// import org.springframework.cloud.context.refresh.ContextRefresher;
@Component
@Slf4j
@RequiredArgsConstructor
public class RunScheduler {
    // private final ContextRefresher contextRefresher;
    @Scheduled(cron = "${my-camel.refresh.schedule:0 0 0 * * 0}")
    public void scheduleRefresh() {
        log.debug("Will refresh spring configurations");
        // contextRefresher.refresh();
        log.debug("Done refreshing spring configurations");
    }
}
