P2P Chat Example
===================

Example of a simple chat client in P2P

Overview
--------

The system is made up of 2 different components:
- A Java client that is launched by each user to chat with others
- An HTTP server that is run centrally and allows each client to register and be reachable for other clients

On top of that, each client acts as both a client for outgoing communications and a server for incoming communications from other clients.

Files needed (included in this repo)
------------------------------------

- Source code for the client: *package com.guillaumelacroix.p2pclient: ClientThread.java; ReceivingThread.java; User.java; Main.java*
- Source code for the server: *package com.guillaumelacroix.p2pserver: Server.java; ServerController.java; User.java*
- Build files: *p2pclient/pom.xml; p2pserver/pom.xml*

Build steps
-----------

Build each project with maven

```
p2pserver$ mvn install
p2pclient$ mvn install
```

Usage
-----

Run the server

```
$ java -jar p2pserver/target/p2pserver-1.0.0.jar
```

Run a client A with the following command

```
$ java -jar p2pclient/target/p2pclient-1.0.0.jar
```

Enter the port to listen to:

```
Enter the port to listen to: 
$ 4444
```

Enter a username:

```
Enter a username:
$ Alice
```

Run another client B to listen on port 4445 with username 'Bob'

Enter Alice's username to start a chat on Bob's client:
```
Enter the target's username
$ Alice
```

Bob can now send messages to Alice.

Refresh Alice's list of available users by entering anything within the command prompt, then enter Bob's username to start a chat:
```
Enter the target's username
$ Bob
```

Alice can now send messages to Bob too.

Choices and limitations
-----------------------

- The current server is a simple HTTP server that answers simple requests. It could be improved in order to ensure a proper authentification from a given user before considering them reachable for others.
- The sending and receiving of the messages is handled in 2 different threads by 2 different sockets. This allows for them to be configured or used independently if needs be. As such, the chatting service is a double one-way communication: this could be changed depending on the requirement of the system.
- As a consequence, since each way of the communication is handled in a single thread, the system can be scaled quite easily for multiple concurrent chats in either way (sending or receiving), given the appropriate GUI, depending on what the requirements would be.
- The input and log system are directly plugged into *System.in* and *System.out*. This should be reworked to be displayed in a proper GUI and enable better testing/stubbing capabilities.
- Data is sent directly as a String without any syntax. It could be replaced with some JSON (or any other language) to allow for more security and information within a single message.
- The interfaces (e.g. User) that are shared between the server side and the client side should be exported in another library that can then be included by both projects, instead of having an objectively horrible copy and paste of a class.