# 360T Assignment application

---

## Implementation comments

Current implementation will not close second application but will close server and application which run server and disconnect all users from second application

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

---

### Task:

Having a Player class - an instance of this class with that can communicate with other Player(s) (other instances of this class)

### The use case for this task is as bellow:

1. create 2 players
2. one of the players should send a message to second player (let's call this player "initiator")
3. when a player receives a message should send back a new message that contains the received message concatenated with
   the message counter that this player sent.
4. finalize the program (gracefully) after the initiator sent 10 messages and received back 10 messages (stop condition)
5. both players should run in the same java process (strong requirement)
6. document for every class the responsibilities it has.
7. opposite to 5: have every player in a separate JAVA process (different PID).

- [x] Please use pure Java as much as possible (no additional frameworks like spring, etc.)
- [ ] Please deliver one single maven project with the source code only (no jars).
- [ ] Please send the maven project as archive
  attached to e-mail (eventual links for download will be ignored due to security policy).
- [ ] Please provide a shell script to start the program.

Everything what is not clearly specified is to be decided by developer.
Everything what is specified is a hard requirement.

Please focus on design and not on technology, the technology should be the simplest possible that is achieving the
target.

The focus of the exercise is to deliver the cleanest and clearest design that you can achieve (and the system has to be
functional).
