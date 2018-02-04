#!/bin/bash 
echo Starting Blockchain Release Installs: CLEAN .................... 
./gradlew clean

echo Starting Blockchain Release Installs: INSTALL APPS .................... 
./gradlew :crudder:installRelease
./gradlew :hospitalapp:installRelease
./gradlew :parlourapp:installRelease
./gradlew :insurancecompany:installRelease
./gradlew :homeaffairs:installRelease
./gradlew :beneficiary:installRelease

echo Blockchain Release Installs hopefuly successfully completed
