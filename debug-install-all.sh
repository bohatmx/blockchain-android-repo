#!/bin/bash 
echo Starting Blockchain Debug Installs .................... 
./gradlew :crudder:installDebug
./gradlew :hospitalapp:installDebug
./gradlew :homeaffairs:installDebug
./gradlew :beneficiary:installDebug
./gradlew :parlourapp:installDebug
./gradlew :insurancecompany:installDebug
echo Blockchain Debug Installs hopefuly successfully completed
