package ThreadPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Server {
    private final ExecutorService threadPool;

    public Server(int poolSize){
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    private Consumer<Socket> getConsumer(){
        return client -> {
            try(
                Socket socket = client;
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter toClient = new PrintWriter(socket.getOutputStream(),true, StandardCharsets.UTF_8)
                )
            {
                socket.setSoTimeout(10_000);
                String message = fromClient.readLine();
                if(message == null){
                    System.out.println("Client disconnected without sending a message.");
                    return;
                }
                System.out.println("Client [" + Thread.currentThread().getName() + "]: " + message);
                toClient.println("Server received: " + message);

                if(toClient.checkError()){
                    System.err.println("Failed to send response to client.");
                }

            }
            catch(IOException ex){
                System.err.println("Error handling client: " + ex.getMessage());
            }
        };
    }

    public void start(int port) throws IOException {
        Consumer<Socket> handler = getConsumer();

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while(!Thread.currentThread().isInterrupted()){
                try{
                    Socket client = serverSocket.accept();
                    System.out.println("Connected to " + client.getRemoteSocketAddress());

                    try{
                        threadPool.execute(() -> handler.accept(client));
                    }
                    catch(RuntimeException ex){
                        try{
                            client.close();
                        }
                        catch(IOException closeEx){
                            ex.addSuppressed(closeEx);
                        }
                        throw ex;
                    }

                }
                catch(IOException ex){
                    if(serverSocket.isClosed()){
                        break;
                    }

                    System.err.println("Error accepting client: " + ex.getMessage());
                }
            }

        }
        finally{
            shutdownPool();
        }
    }

    private void shutdownPool(){
        System.out.println("Shutting down thread pool...");
        threadPool.shutdown();

        try{
            if(!threadPool.awaitTermination(5, TimeUnit.SECONDS)){
                System.out.println("Some tasks are still running. Requesting interruption...");
                threadPool.shutdownNow();

                if(!threadPool.awaitTermination(5, TimeUnit.SECONDS)){
                    System.err.println("Thread pool did not terminate within the timeout.");
                }
            }
        }
        catch(InterruptedException ex){
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("Thread pool shutdown finished.");
    }

    public static void main(String[] args) {
        int port = 8010;
        int poolSize = 3;

        Server server = new Server(poolSize);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown requested.");
            server.threadPool.shutdown();
        }));

        try{
            server.start(port);
        }
        catch(IOException ex){
            System.err.println("Server stopped due to an I/O error: " + ex.getMessage());
        }
    }
}