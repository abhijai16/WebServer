package MultiThreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class Server {

    private Consumer<Socket> getConsumer(){
        return client->{
            try(Socket socket = client;
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
                PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)
                )
            {
                String message = fromClient.readLine();

                if(message == null){
                    System.out.println("Client disconnected without sending a message.");
                    return;
                }

                System.out.println("Client: " + message);
                toClient.println("Server received: " + message);
            }
            catch(IOException ex){
                System.err.println("Error handling client: " + ex.getMessage());
            }
        };
    }

    public static void main(String[] args) throws IOException{
        int port = 8010;
        Server server = new Server();

        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server is listening on port : " + port);

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Connected to " + client.getRemoteSocketAddress());

                Thread thread = new Thread(() -> server.getConsumer().accept(client));
                thread.start();
            }
        }
        catch(IOException ex){
            System.err.println("Server error: " + ex.getMessage());
        }
    }
}
