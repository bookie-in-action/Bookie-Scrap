#!/bin/bash

APP_NAME="BookieScrap"

#JAVA_BIN="/c/Java/jdk-11.0.22/bin/java"
MAIN_CLASS="com.bookie.scrap.startup.Main"
JAVA_BIN="$JAVA_HOME/bin/java"

HOME_DIR="/Users/hs_/Documents/github/bookie_pjt/Bookie-Scrap/server"
LIB_DIR="$HOME_DIR/lib"
RESOURCES_DIR="$HOME_DIR/resources"


# Java 실행 파일 경로
if [ -z "$JAVA_HOME" ]; then
    echo "JAVA_HOME is not set. Please set JAVA_HOME environment variable."
    exit 1
fi


# 실행 정보 출력

echo "==================================================="
echo "Running $APP_NAME Application...                  "
echo "                                                   "
echo "Main Class: $MAIN_CLASS                            "
echo "                                                   "
echo "Libraries Directory: $LIB_DIR                      "
echo "Resources Directory: $RESOURCES_DIR                "
echo "==================================================="

CLASSPATH=""
for jar in "$LIB_DIR"/*.jar; do
    CLASSPATH="$CLASSPATH$jar:"
    echo "Added jar: $jar"
done
echo "========================================================"
for resource in "$RESOURCES_DIR"/*; do
    echo "Added resource: $resource"
done
CLASSPATH="$CLASSPATH$RESOURCES_DIR:"
echo "========================================================"
echo "Final CLASSPATH: $CLASSPATH                             "
echo "========================================================"
echo "                                                        "

# JAR 실행
$JAVA_BIN -cp "$CLASSPATH" "$MAIN_CLASS"

if [ $? -eq 0 ]; then
    echo "                                                   "
    echo "==================================================="
    echo "Application exited successfully."
    echo "==================================================="
    echo "                                                   "

else
    echo "                                                   "
    echo "==================================================="
    echo "Application encountered an error. Exit code: $?"
    echo "==================================================="
    echo "                                                   "

fi
