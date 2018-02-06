#!/bin/bash 
echo Starting Blockchain Demo Build ....................
successCnt = 0;
./gradlew clean
./gradlew :crudder:build
./gradlew :hospitalapp:build
./gradlew :homeaffairs:build
./gradlew :beneficiary:build
./gradlew :parlourapp:build
./gradlew :insurancecompany:build
echo Blockchain Demo Build hopefuly successfully completed
