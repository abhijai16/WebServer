# Java Web Server

A learning project to understand how servers handle client connections using Java.

The goal of this project is to explore the fundamentals of client-server communication, TCP sockets, multithreading, and thread management by implementing the same basic server in three different ways.

Instead of directly relying on frameworks, this project focuses on understanding what's happening behind the scenes.

## Project Structure

```text
WebServer/
│
├── SingleThreaded/
│   ├── Server.java
│   └── Client.java
│
├── MultiThreaded/
│   ├── Server.java
│   └── Client.java
│
├── ThreadPool/
│   ├── Server.java
│   └── Client.java
│
├── .gitignore
└── README.md
```

## Implementations

### 1. Single-Threaded Server

The simplest implementation.

- Uses `ServerSocket` to listen for incoming connections.
- Uses `Socket` to communicate with clients.
- Processes one client at a time.
- A client being processed can prevent the server from handling another client until the current operation finishes.

**Key concept:** Sequential execution and blocking I/O.

### 2. Multithreaded Server

An improvement over the single-threaded approach.

- Creates a separate thread for each client connection.
- Allows multiple clients to be handled concurrently.
- Uses Java's `Thread` and `Runnable`.
- Tested with multiple client connections.

**Key concept:** Concurrent execution and thread-per-client architecture.

### 3. Thread Pool-Based Server

A more controlled approach to handling multiple clients.

- Uses Java's `ExecutorService`.
- Implements a fixed-size thread pool.
- Reuses worker threads instead of creating a new thread for every client.
- Additional tasks wait in a queue when all workers are busy.

**Key concept:** Thread reuse, task scheduling, and resource management.

## Comparison

| Feature | Single Thread | Multithreaded | Thread Pool |
|---|---|---|---|
| Execution | Sequential | Concurrent | Concurrent |
| Thread management | One worker | One per client | Fixed worker pool |
| Thread reuse | Not applicable | No | Yes |
| Resource control | Simple | Less controlled | More controlled |
| Scalability | Limited | Resource-dependent | Better controlled |

## Technologies Used

- **Java**
- **TCP/IP Networking**
- **Java Sockets API**
- **Multithreading**
- **ExecutorService**
- **Java I/O**

## Concepts Learned

While building this project, I explored:

- How TCP client-server communication works.
- How `ServerSocket` and `Socket` establish connections.
- Blocking I/O and client handling.
- Java Threads and Runnable.
- Handling multiple client connections.
- Thread pools and ExecutorService.
- Task queues and worker thread reuse.
- Resource management and exception handling.

## Future Improvements

- Implement HTTP request parsing and HTTP responses.
- Handle client disconnections gracefully.
- Add proper error handling and logging.
- Experiment with different thread pool sizes.
- Measure performance under concurrent client loads.
- Explore bounded queues and backpressure.


