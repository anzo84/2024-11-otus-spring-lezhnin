package ru.otus.hw.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupHealthIndicator implements HealthIndicator {

    private volatile boolean applicationReady = false;

    private volatile long startupTime = 0;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        this.applicationReady = true;
        this.startupTime = System.currentTimeMillis() - event.getTimestamp();
    }

    @Override
    public Health health() {
        if (!applicationReady) {
            return Health.down()
                .withDetail("status", "STARTING")
                .withDetail("message", "Приложение запускается")
                .build();
        } else {
            return Health.up()
                .withDetail("status", "UP")
                .withDetail("startupTime", startupTime + " ms")
                .withDetail("message", "Приложение успешно запустилось")
                .build();
        }
    }
}