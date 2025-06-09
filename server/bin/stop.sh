#!/usr/bin/env bash
set -euo pipefail

BIN_DIR="$(cd "$(dirname "$0")" && pwd)"
PID_FILE="$BIN_DIR/scrap.pid"
TIMEOUT=30   # 초

if [[ ! -f "$PID_FILE" ]]; then
  echo "❌ PID 파일이 없습니다. (이미 중지되었을 수 있음)"
  exit 1
fi

PID="$(cat "$PID_FILE")"
if ! kill -0 "$PID" 2>/dev/null; then
  echo "⚠️  프로세스 $PID 가 이미 종료되어 있습니다."
  rm -f "$PID_FILE"
  exit 0
fi

echo "⏳ PID $PID 종료 시도 (SIGTERM)…"
kill -15 "$PID"

# graceful wait
for ((i=0; i<TIMEOUT; i++)); do
  if ! kill -0 "$PID" 2>/dev/null; then
    echo "✅ 정상 종료 완료"
    rm -f "$PID_FILE"
    exit 0
  fi
  sleep 1
done

echo "⌛️ $TIMEOUT 초 초과 — 강제 종료(SIGKILL)"
kill -9 "$PID" || true
rm -f "$PID_FILE"
