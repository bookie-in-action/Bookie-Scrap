#!/usr/bin/env bash
set -euo pipefail

BIN_DIR="$(cd "$(dirname "$0")" && pwd)"
APP_HOME="$(cd "$BIN_DIR/.." && pwd)"
LIB_DIR="$APP_HOME/lib"
CLASS_DIR="$APP_HOME/classes"
PID_FILE="$BIN_DIR/scrap.pid"
LOG_FILE="$BIN_DIR/console.out"

# 이미 실행 중인지 확인
if [[ -f "$PID_FILE" ]] && kill -0 "$(cat "$PID_FILE")" 2>/dev/null; then
  echo "❌ 이미 PID $(cat "$PID_FILE") 로 실행 중입니다."
  exit 1
fi

JAR_NAME="$(ls "$LIB_DIR"/*.jar | head -n1)"

# 백그라운드 구동
nohup java ${JAVA_OPTS:-} \
  -Dspring.config.additional-location=file:"$CLASS_DIR"/ \
  -Dlogging.config=file:"$CLASS_DIR"/logback.xml \
  -jar "$JAR_NAME" \
  >>"$LOG_FILE" 2>&1 &

echo $! > "$PID_FILE"
echo "✅ 애플리케이션 시작 (PID $(cat "$PID_FILE")) — 로그: $LOG_FILE"
