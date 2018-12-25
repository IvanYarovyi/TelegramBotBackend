echo Building.
call gradlew build fatJar
echo Copy to remote.
pscp ./build/libs/bot-backend-all-1.0-SNAPSHOT.jar root@192.168.31.100:/home/pi/java/as/bot-backend-all-1.0-SNAPSHOT.jar
echo Done.