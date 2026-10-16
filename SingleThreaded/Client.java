import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {

    public void run() throws IOException {
        int port = 8010;
        InetAddress address = InetAddress.getByName("localhost");

        try(Socket socket = new Socket(address, port);
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);

            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)))
        {
            toServer.println("Hello from the client.");

            String response = fromServer.readLine();

            if(response != null){
                System.out.println("Server: " + response);
            }
            else{
                System.out.println("Server closed the connection without a response.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.run();
        }
        catch (IOException ex) {
            System.err.println("Client error: " + ex.getMessage());
        }
    }
}
