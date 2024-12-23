#!/bin/bash

# 환경 변수 및 설정
APP_NAME="BookieScrap"
PID_FILE="bookie.pid"

# PID 파일 확인
if [ ! -f "$PID_FILE" ]; then
  echo "$APP_NAME is not running (PID file not found)."
  exit 1
fi

# PID 읽기
PID=$(cat "$PID_FILE")

# SIGTERM 신호로 프로세스 종료
kill $PID

# 종료 확인
if [ $? -eq 0 ]; then
  echo "$APP_NAME stopped gracefully."
  rm -f "$PID_FILE"
else
  echo "Failed to stop $APP_NAME. You may need to stop it manually."
  exit 1
fi
