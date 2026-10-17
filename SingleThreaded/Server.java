import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {

    public void run() throws IOException {
        int port = 8010;

        try(ServerSocket serverSocket = new ServerSocket(port)){

            System.out.println("Server started on port " + port);

            while(true){
                Socket clientSocket = serverSocket.accept();

                System.out.println("Connection accepted from client " + clientSocket.getRemoteSocketAddress());

                try(Socket client = clientSocket;
                    BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream(),StandardCharsets.UTF_8));

                    PrintWriter toClient = new PrintWriter(client.getOutputStream(), true, StandardCharsets.UTF_8))
                {
                    String message = fromClient.readLine();

                    if(message == null){
                        System.out.println("Client disconnected without sending a message.");
                        continue;
                    }

                    System.out.println("Client: " + message);
                    toClient.println("Server received: " + message);

                }
                catch(IOException ex){
                    System.err.println("Error handling client: " + ex.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();

        try {
            server.run();
        }
        catch (IOException ex) {
            System.err.println("Server error: " + ex.getMessage());
        }
    }
}
