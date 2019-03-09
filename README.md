# Telegram Bot Backend App
### To run this app you need:
- JVM installed
- proxy server with configured https. (For example Nginx)
- DB populated with business data

### Guide how to build the app
gradlew build fatJar

### Guide how to run app
java -jar build/libs/bot-backend-all-1.0-SNAPSHOT.jar 
 -t=T_BT
 -i=P_IP
 -c=CERT
 -p=APP_PORT
 -h=DB
 -u=DB_USER
 -s=DB_PASS
 &

##### Where:
- **T_BT** telegram bot token. _example: 666666666:AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA_
- **P_IP** your public. _example: https://100.100.10.10:443_
- **CERT** path to certificate. Certificate can either be verified or self-signed. _example: /etc/nginx/ssl/bot-public.pem_
- **APP_PORT** Web server port for this application. _example: 8082_
- **DB** Database connection url. _example: jdbc:mariadb://localhost:3306/fop_db_
- **DB_USER** Database user url. _example: user_
- **DB_PASS** Database password url. _example: mypass_

## App architecture
Blue rectangle on diagram is our application.
<br />![photo](https://raw.githubusercontent.com/e1-one/TelegramBotBackend/fop-bot/docs/TelegramBotArchitecture.png)