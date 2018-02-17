#!/bin/bash 
echo Starting Blockchain Debug uninstalls ....................
echo ###################################

./gradlew :crudder:uninstallDebug
./gradlew :hospitalapp:uninstallDebug
./gradlew :parlourapp:uninstallDebug
./gradlew :insurancecompany:uninstallDebug
./gradlew :homeaffairs:uninstallDebug
./gradlew :beneficiary:uninstallDebug
./gradlew :bank:uninstallDebug
./gradlew :clientapp:uninstallDebug
echo Blockchain Debug uninstalls hopefuly successfully completed
echo #####################################################
