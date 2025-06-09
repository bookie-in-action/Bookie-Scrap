#!/usr/bin/env bash
set -euo pipefail

# --------- 경로 세팅 ---------------------------------------------------------
BIN_DIR="$(cd "$(dirname "$0")" && pwd)"
APP_HOME="$(cd "$BIN_DIR/.." && pwd)"
LIB_DIR="$APP_HOME/lib"
CLASS_DIR="$APP_HOME/classes"

JAR_NAME="$(ls "$LIB_DIR"/*.jar | head -n1)"   # lib 안 jar 하나일 때

# --------- 애플리케이션 구동 --------------------------------------------------
exec java ${JAVA_OPTS:-} \
  -Dspring.config.additional-location=file:"$CLASS_DIR"/ \
  -Dlogging.config=file:"$CLASS_DIR"/logback.xml \
  -jar "$JAR_NAME"
