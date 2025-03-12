package com.bookie.scrap.common.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class SchedulerProperties implements InitializableProperties {

    @RequiredArgsConstructor
    public enum Key {
        PK(""), JOB_CLASS(".job.class"), MODE(".type"), EXPRESSION(".expression"), ENABLED(".enable");
        private final String suffix;
    }

    @Getter
    private final Map<String, Map<Key, String>> SCHEDULER_SETTINGS_BY_PK = new HashMap<>();

    private static final SchedulerProperties INSTANCE = new SchedulerProperties();
    private boolean initialized = false;

    private SchedulerProperties() {}

    public static SchedulerProperties getInstance() {
        return INSTANCE;
    }

    /**
     * schedulers에 선언되어 있는 name을 pk로 나머지 값들을 로딩
     *
     * @param runningOption (사용하지 않음)
     */
    @Override
    public synchronized void init(String runningOption) {

        if (initialized) {
            log.debug("SchedulerProperties is already initialized");
            return;
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

            initialized = true;

            log.info("============ [SCHEDULER PROPERTIES] ============");

            int paddingLength = 15;
            for (String schedulerName : schedulerNames) {
                Map<Key, String> propertyMap = new EnumMap<>(Key.class);


                propertyMap.put(Key.PK, schedulerName);
                log.info("SCHEDULER {}: {}", getFormat("PK", paddingLength), schedulerName);

                for(Key key : Key.values()) {
                    if(key.equals(Key.PK)) {continue;}

                    String property = schedulerProperties.getProperty(schedulerName + key.suffix);
                    propertyMap.put(key, property);
                    log.info("SCHEDULER {}: {}", getFormat(key.name(), paddingLength), property);
                }

                log.info("");

                SCHEDULER_SETTINGS_BY_PK.put(schedulerName, propertyMap);
            }

            log.info("================================================");

            log.info("<= SchedulerProperties initialized successfully");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load bookie properties", e);
        }
    }

    private static String getFormat(String value, int paddingLength) {
        return String.format("%-" + paddingLength + "s", value);
    }

    /**
     * KEY로 선언한 값이 모두 properties 파일에 설정되어 있는지 확인
     */
    @Override
    public void verify() {
        for (Map.Entry<String, Map<Key, String>> entry : SCHEDULER_SETTINGS_BY_PK.entrySet()) {
            String schedulerName = entry.getKey();
            Map<Key, String> schedulerPropMap = entry.getValue();

            for (Key key : Key.values()) {
                if (!schedulerPropMap.containsKey(key)) {
                    throw new IllegalStateException("Missing required key: " + key + " in scheduler: " + schedulerName);
                }
            }

            // TODO: enable 값 true or false verify
            // TODO: expression cron or interval verify
        }
    }

    public Map<SchedulerProperties.Key, String> getSchedulerSettingsByPk(String schedulerName) {
        return SCHEDULER_SETTINGS_BY_PK.get(schedulerName);
    }
}
