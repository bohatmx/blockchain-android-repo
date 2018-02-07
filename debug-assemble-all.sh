#!/bin/bash 
echo Starting Blockchain Debug Assembly ....................
./gradlew :crudder:assembleDebug
./gradlew :hospitalapp:assembleDebug
./gradlew :homeaffairs:assembleDebug
./gradlew :beneficiary:assembleDebug
./gradlew :parlourapp:assembleDebug
./gradlew :insurancecompany:assembleDebug
echo Blockchain Debug Assembly hopefuly successfully completed
