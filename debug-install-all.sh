#!/bin/bash 
echo Starting Blockchain Debug Installs .................... 
./gradlew clean
./gradlew :crudder:installDebug
./gradlew :hospitalapp:installDebug
./gradlew :parlourapp:installDebug
./gradlew :insurancecompany:installDebug
echo Blockchain Debug Installs hopefuly successfully completed
