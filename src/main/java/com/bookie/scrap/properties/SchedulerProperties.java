package com.bookie.scrap.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class SchedulerProperties implements InitializableProperties{

    @RequiredArgsConstructor
    public enum Key {
        NAME(""), JOB_CLASS(".job.class"), TYPE(".type"), EXPRESSION(".expression");
        private final String suffix;
    }

    @Getter
    private final Map<String, Map<Key, String>> SCHEDULER_PROPS = new HashMap<>();

    private static final SchedulerProperties INSTANCE = new SchedulerProperties();
    private boolean initialized = false;

    private SchedulerProperties() {}

    public static SchedulerProperties getInstance() {
        return INSTANCE;
    }

    //::TODO
    @Override
    public synchronized void init(String runningOption) {

        if (initialized) {
            throw new IllegalStateException("SchedulerProperties is already initialized");
        }

        log.info("=> Initializing SchedulerProperties");

        try (InputStream inputStream = SchedulerProperties.class.getClassLoader().getResourceAsStream("bookie.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("bookie.properties file not found in classpath");
            }

            Properties schedulerProperties = new Properties();
            schedulerProperties.load(inputStream);

            String[] schedulerNames = Arrays.stream(schedulerProperties.getProperty("schedulers").split(","))
                    .map(String::trim)
                    .toArray(String[]::new);

            //TODO::
            initialized = true;

            for (String schedulerName : schedulerNames) {
                Map<Key, String> propertyMap = new EnumMap<>(Key.class);

                propertyMap.put(Key.NAME, schedulerName);
                for(Key key : Key.values()) {
                    String property = schedulerProperties.getProperty(schedulerName + key.suffix);
                    propertyMap.put(key, property);
                    log.info("SCHEDULER {}: {}", key.name(), property);
                }

                SCHEDULER_PROPS.put(schedulerName, propertyMap);
            }

            log.info("<= SchedulerProperties initialized successfully");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load bookie properties", e);
        }
    }

    @Override
    public void verify() {
        for (Map.Entry<String, Map<Key, String>> entry : SCHEDULER_PROPS.entrySet()) {
            String schedulerName = entry.getKey();
            Map<Key, String> schedulerPropMap = entry.getValue();

            for (Key key : Key.values()) {
                if (!schedulerPropMap.containsKey(key)) {
                    throw new IllegalStateException("Missing required key: " + key + " in scheduler: " + schedulerName);
                }
            }
        }
    }

}
