#!/bin/bash

JAVA_HOME=${JAVA_HOME:-/opt/java-current}

SERVER_ROOT="/server"

APP_PATH="${SERVER_ROOT}/lib/BookieScrap-0.0.1-SNAPSHOT.jar"
PID_FILE="${SERVER_ROOT}/bin/boot.pid"

JAVA_OPTS=""
LOG_BACK_OPT="-Dlogging.config=${SERVER_ROOT}/config/logback-spring.xml"
APPLICATION_CONFIG_OPT="--spring.config.location=file:${SERVER_ROOT}/config/application-docker.yml"



APP_TAG="BOOKIE-SCRAP"


if [[ -n "$JAVA_HOME" && -x "$JAVA_HOME/bin/java" ]]; then
    JAVA_CMD="$JAVA_HOME/bin/java"
else
    JAVA_CMD="java"
fi


EXECUTE_CMD="$JAVA_CMD -Dtag=$APP_TAG $JAVA_OPTS $LOG_BACK_OPT -jar $APP_PATH $APPLICATION_CONFIG_OPT"

start() {
    echo "Starting $APP_TAG..."

    
    if [[ -f "$PID_FILE" ]] && ps -p "$(cat $PID_FILE)" > /dev/null; then
        echo "$APP_TAG is already running. Exiting..."
        exit 1
    else
        nohup $EXECUTE_CMD > /dev/null 2> /dev/null & echo $! > $PID_FILE
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

