#!/bin/bash

CUSTOM_JAVA_HOME=/home/scrap/java/jdk-17.0.0.1

APP_PATH="../lib/BookieScrap-0.0.1-SNAPSHOT.jar"
PID_FILE="boot.pid"

JAVA_OPTS=

LOG_BACK_OPT=-Dlogging.config=../config/logback.xml
APPLICATION_CONFIG_OPT=--spring.config.location=file:../config/application.yml

APP_TAG=BOOKIE-SCRAP


if [[ -n "$CUSTOM_JAVA_HOME" && -x "$CUSTOM_JAVA_HOME/bin/java" ]]; then
    JAVA_CMD="$CUSTOM_JAVA_HOME/bin/java"
else
    JAVA_CMD="java"
fi


EXECUTE_CMD="$JAVA_CMD -Dtag=$APP_TAG $JAVA_OPTS $LOG_BACK_OPT -jar $APP_PATH $APPLICATION_CONFIG_OPT"

start() {
    echo "Starting $APP_TAG..."

    if pgrep -f "$APP_TAG" > /dev/null; then
        echo "$APP_TAG is already running. Exiting..."
        exit 1
    else
        nohup $EXECUTE_CMD > /dev/null 2> apim.err & echo $! > $PID_FILE
        echo "$APP_NAME started with PID $(cat $PID_FILE)"
    fi
}

run() {
    echo "Running $APP_TAG..."
    $EXECUTE_CMD
}

stop() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat $PID_FILE)
        echo "Stopping $APP_TAG (PID: $PID)..."
        kill -15 $PID  # SIGTERM for graceful shutdown
        sleep 5  # Allow time for cleanup
        rm -f $PID_FILE
        echo "$APP_TAG stopped gracefully."
    else
        echo "No PID file found. Is $APP_TAG running?"
    fi
}

status() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat $PID_FILE)
        if ps -p $PID > /dev/null; then
            echo "$APP_TAG is running with PID $PID"
        else
            echo "$APP_TAG is not running, but PID file exists."
        fi
    else
        echo "$APP_TAG is not running."
    fi
}

case "$1" in
    start) start ;;
    stop) stop ;;
    status) status ;;
    run) run ;;
    *)
        echo "Usage: $0 {start|run|stop|status}"
        exit 1
        ;;
esac

