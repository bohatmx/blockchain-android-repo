#!/bin/bash 
echo Starting Blockchain Demo Build .................... 
./gradlew clean
./gradlew :crudder:build
./gradlew :hospitalapp:build
./gradlew :parlourapp:build
./gradlew :insurancecompany:build
echo Blockchain Demo Build hopefuly successfully completed
