import java.io.*;
import java.net.*;
import java.net.ServerSocket;
import java.util.HashMap; 

public class TCPServer {
    
    private ServerSocket serverSocket; 
    public static HashMap<String,GroupHandler> group = new HashMap<>(); 

    public TCPServer(ServerSocket serverSocket) //constructor 
    {
        this.serverSocket = serverSocket;
    }

    public void startServer()
    {
        try 
        {
            while (!serverSocket.isClosed()) // indefinitely listen
            {
                System.out.println("Listening for connections...");
                Socket socket = serverSocket.accept(); //indefinitely accept client connections 
                System.out.println("A new client has connected to the server.\n\n");
                ClientHandler clientHandler = new ClientHandler(socket); // talks to client
                Thread thread = new Thread(clientHandler); // creates a thread for each client hence multitthreaded server 
                thread.start(); 
            }
        }
        catch (IOException o)
        {
                o.printStackTrace(); 
                return; 
        }
    }
    public void closeServerSocket()
    {
        try
        {
            if(serverSocket != null)
            {
                serverSocket.close(); 
            }
        }
        catch(IOException e)
        {
            e.printStackTrace(); 
        }
    }
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(1234); 
        TCPServer server = new TCPServer(serverSocket); 
        server.startServer();
    }
}
