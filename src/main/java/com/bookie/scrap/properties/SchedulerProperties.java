package com.bookie.scrap.properties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class SchedulerProperties implements InitializableProperties{

    public enum Key {
        NAME, JOB_CLASS, TYPE, EXPRESSION
    }

    private final List<Map<Key, String>> schedulerProps = new ArrayList<>();

    private static final SchedulerProperties INSTANCE = new SchedulerProperties();
    private boolean initialized = false;

    private SchedulerProperties() {}

    public static SchedulerProperties getInstance() {
        return INSTANCE;
    }

    public synchronized void init( List<Map<Key, String>> testProperties) {
        if (initialized) {
            throw new IllegalStateException("SchedulerProperties is already initialized");
        }
        schedulerProps.addAll(testProperties);
        initialized = true;
    }

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

            String[] schedulerNames = schedulerProperties.getProperty("schedulers").split(",");

            initialized = true;

            for (String schedulerName : schedulerNames) {
                Map<Key, String> propertyMap = new EnumMap<>(Key.class);

                String jobClass = schedulerProperties.getProperty(schedulerName.trim() + ".job.class");
                String schedulerType = schedulerProperties.getProperty(schedulerName.trim() + ".type");
                String schedulerExpression = schedulerProperties.getProperty(schedulerName.trim() + ".expression");

                propertyMap.put(Key.NAME, schedulerName.trim());
                propertyMap.put(Key.JOB_CLASS, jobClass);
                propertyMap.put(Key.TYPE, schedulerType);
                propertyMap.put(Key.EXPRESSION, schedulerExpression);

                schedulerProps.add(propertyMap);

                log.info("SCHEDULER NAME: {}", schedulerName);
                log.info("SCHEDULER JOB CLASS: {}", jobClass);
                log.info("SCHEDULER TYPE: {}", schedulerType);
                log.info("SCHEDULER EXPRESSION: {}\n", schedulerExpression);

            }

            log.info("<= SchedulerProperties initialized successfully");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load bookie properties", e);
        }
    }

    @Override
    public void verify() {
        for(Map<Key, String> schedulerPropMap : schedulerProps) {
            for (Key key : Key.values()) {
                if (!schedulerPropMap.containsKey(key)) {
                    throw new IllegalStateException("Missing required key: " + key + " in bookie.properties");
                }
            }
        }
    }

    public List<Map<Key, String>> getProps() {
        return this.schedulerProps;
    }
}
