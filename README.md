# Chat game application

---

## Prerequisite

* Maven
* JDK 11

### Default settings

```properties
port=4440
## Will start server on port 4440 on start up
createServer=true
## Will create N players (max 10) on start
createPlayers=2
## Message limit for application
messageLimit=100
## Will set nicknames to Player${Number}
defaultNicknames=true
## Will send Hello in second connected player
sendHelloMessage=false
```

To change them edit ./src/main/resources/config.properties

**IMPORTANT:** If you changed settings you need to rebuild jar for new settings to work

---

## How to run application

Will check `target/*.jar` and if already exist, just runs the server
If not, then will build and run application

```bash
./startServer.sh
```

### Rebuild and run command

Will always build before run

```bash
./rebuild.sh
```

---

## Screen documentation

### Main screen

On main screen you can start server or start client (Player) that will connect to already existed server

* You can stop server by pressing "Stop Server" button it will close all Players active at that moment
* You can run server by "Create new server" on selected in text field port
* You can run 1 server for 1 application at 1 time and multiple clients

### Player UI

Consists of input field and chat area

* Player need to input his name.
* Empty or Already existed nicknames are not allowed.
* Empty messages are not allowed
* Messages counting only when there are 2 or more clients on the server
* To quit you can type `quit`

---