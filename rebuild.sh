#!/bin/bash
SERVICE=$1
./gradlew :$SERVICE:bootJar && docker compose up --build $SERVICE