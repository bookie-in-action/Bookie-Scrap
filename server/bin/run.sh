#!/bin/bash

# 스크립트 설정
HOME_DIR=".."
#TARGET_JAR="$HOME_DIR/target/BookieScrap-0.0.1-SNAPSHOT.jar"
LIB_DIR="$HOME_DIR/lib"
RESOURCES_DIR="$HOME_DIR/resources"

JAVA_BIN="/c/Java/jdk-11.0.22/bin/java"
MAIN_CLASS="com.bookie.scrap.startup.Main"
#JAVA_BIN="$JAVA_HOME/bin/java"


# Java 실행 파일 경로
if [ -z "$JAVA_HOME" ]; then
    echo "JAVA_HOME is not set. Please set JAVA_HOME environment variable."
    exit 1
fi


# 실행 정보 출력
echo "==================================================="
echo "Starting Java Application..."
echo "==================================================="
echo "Main Class: $MAIN_CLASS"
echo "==================================================="
echo "Libraries Directory: $LIB_DIR"
echo "Resources Directory: $RESOURCES_DIR"
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

echo "==================================================="
echo "                                                   "

# JAR 실행
$JAVA_BIN -cp "$CLASSPATH" "$MAIN_CLASS"

# 실행 결과 확인
if [ $? -eq 0 ]; then
    echo "==================================================="
    echo "Application exited successfully."
    echo "==================================================="
    echo "                                                   "

else
    echo "==================================================="
    echo "Application encountered an error. Exit code: $?"
    echo "==================================================="
    echo "                                                   "

fi
