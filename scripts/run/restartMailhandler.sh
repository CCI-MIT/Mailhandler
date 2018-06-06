#!/bin/bash

BASE_FILENAME=mailhandler
VERSION=0.0.1-SNAPSHOT

echo "#####################################################################################"
echo "[INFO] Starting mailhandler..."

PID_FILE="${BASE_FILENAME}.pid"
if [ -f ${PID_FILE} ]; then
    echo "[INFO] Sending kill signal to ${BASE_FILENAME}"
    kill $(<"${PID_FILE}")
    rm ${PID_FILE}
fi

JAR_NAME=${BASE_FILENAME}-${VERSION}.jar
echo "[INFO] Downloading jar from binaries server..."
scp binaries@cognosis2.mit.edu:mailhandler/${JAR_NAME} ./

OUT_FILE="${BASE_FILENAME}.out"
rm ${OUT_FILE} > /dev/null 2>&1
echo "[INFO] Starting jar..."
exec java -jar ${BASE_FILENAME}-${VERSION}.jar > ${OUT_FILE}  & echo $! > ${PID_FILE}

echo "[INFO] Done."
echo "#####################################################################################"
