P2P Chat Example
===================

Example of a simple chat client in P2P

Files needed (included in this repo)
------------------------------------

- Source code: *package com.guillaumelacroix.p2pclient: ClientThread.java; ReceivingThread.java; Main.java*
- Build file: *pom.xml*

Build steps
-----------

Build the project with maven

```
$ mvn install
```

Usage
-----

Run a client A with the following command

```
$ java -jar target/p2pclient-1.0.0
```

Enter the port to listen to:

```
Enter the port to listen to: 
$ 4444
```

Run another client B to listen on port 4445

Enter client B's address and port to start a chat on client A:
```
Enter the target's [address:port] 
$ 127.0.0.1:4445
```

A can now send messages to B.


Enter client A's address and port to start a chat on client B:
```
Enter the target's [address:port] 
$ 127.0.0.1:4444
```

B can now send messages to A too.

Choices and limitations
-----------------------

- Each user enters who they want to connect to directly. This allows the system to work without a centralised server. It can easily be addressed with a server that each client would connect to in order to register and get the list of all available clients. Upon connection of a client, each client would be notified with the updated list of clients. This would also allow for more security as a client would have to be recognised (or even authentified) in order to be reachable by other clients.
- The sending and receiving of the messages is handled in 2 different threads by 2 different sockets. This allows for them to be configured or used independently if needs be. As such, the chatting service is a double one-way communication: this could be changed depending on the requirement of the system.
- As a consequence, since each way of the communication is handled in a single thread, the system can be scaled quite easily for multiple concurrent chats in either way (sending or receiving), given the appropriate GUI, depending on what the requirements would be.
- The input and log system are directly plugged into *System.in* and *System.out*. This should be reworked to be displayed in a proper GUI and enable better testing/stubbing capabilities.
- Data is sent directly as a String without any syntax. It could be replaced with some JSON (or any other language) to allow for more security and information within a single message.