#!/bin/bash

APP_NAME="BookieScrap"

#JAVA_BIN="/c/Java/jdk-11.0.22/bin/java"
MAIN_CLASS="com.bookie.scrap.startup.Main"
JAVA_BIN="$JAVA_HOME/bin/java"

HOME_DIR="/Users/hs_/Documents/github/bookie_pjt/Bookie-Scrap/server"
LOG_DIR="$HOME_DIR/logs"
LIB_DIR="$HOME_DIR/lib"
PID_FILE="$HOME_DIR/bin/pid/bookie.pid"
RESOURCES_DIR="$HOME_DIR/resources"

# Java 실행 파일 경로
if [ -z "$JAVA_HOME" ]; then
    echo "JAVA_HOME is not set. Please set JAVA_HOME environment variable."
    exit 1
fi

# 실행 정보 출력
echo "==================================================="
echo "Starting $APP_NAME Application...                  "
echo "                                                   "
echo "Main Class: $MAIN_CLASS                            "
echo "                                                   "
echo "Libraries Directory: $LIB_DIR                      "
echo "Resources Directory: $RESOURCES_DIR                "
echo "==================================================="
echo "Using Resources:"

CLASSPATH=""
for jar in "$LIB_DIR"/*.jar; do
    CLASSPATH="$CLASSPATH$jar;"
    echo "Added jar: $jar"
done

echo "==================================================="

for resource in "$RESOURCES_DIR"/*; do
    echo "Added resource: $resource"
done
CLASSPATH="$CLASSPATH$RESOURCES_DIR;"

echo "Final CLASSPATH: $CLASSPATH"

echo "                                                   "

# 로그 디렉토리 생성
mkdir -p "$LOG_DIR"

# 이미 실행 중인지 확인
if [ -f "$PID_FILE" ]; then
  echo "$APP_NAME is already running. PID: $(cat $PID_FILE)"
  exit 1
fi

# 백그라운드에서 애플리케이션 실행
nohup $JAVA_BIN -cp "$CLASSPATH" "$MAIN_CLASS" > "$LOG_DIR/app.log" 2>&1 &
PID=$!

disown $PID

echo "==================================================="
echo "                                                   "
# PID 파일 생성
echo $PID > "$PID_FILE"
echo "$APP_NAME started with PID $PID."
echo "                                                   "
echo "==================================================="
echo "                                                   "