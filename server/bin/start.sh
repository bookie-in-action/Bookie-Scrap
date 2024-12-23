#!/bin/bash

# 환경 변수 및 설정
APP_NAME="BookieScrap"
JAR_FILE="path/to/your/BookieScrap-0.0.1-SNAPSHOT.jar"
LOG_DIR="logs"
PID_FILE="bookie.pid"

JAVA_BIN="path/to/your/java" # Java 실행 경로
JAVA_OPTS="-Dlogback.configurationFile=path/to/your/logback.xml"
CLASSPATH="lib/*:$JAR_FILE"

# 로그 디렉토리 생성
mkdir -p "$LOG_DIR"

# 이미 실행 중인지 확인
if [ -f "$PID_FILE" ]; then
  echo "$APP_NAME is already running. PID: $(cat $PID_FILE)"
  exit 1
fi

# 백그라운드에서 애플리케이션 실행
nohup $JAVA_BIN -cp "$CLASSPATH" $JAVA_OPTS com.bookie.scrap.startup.Main > "$LOG_DIR/app.log" 2>&1 &
PID=$!

# PID 파일 생성
echo $PID > "$PID_FILE"
echo "$APP_NAME started with PID $PID."
