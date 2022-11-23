import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TCPClient implements Runnable {
    public static boolean connected = false;
    public static String join;  

    public static ArrayList<String> groupsJoined = new ArrayList<>();
    
    private Socket socket;
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter; 
    private String username;
    
 
    public TCPClient(Socket socket, String username) throws IOException
    {
        try 
        {
        this.socket = socket;
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //iniralizes read and write buffer, and username for the client 
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.username = username;
        }
        catch(IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage()
    {
        try
        {
            bufferedWriter.write(username); 
            bufferedWriter.newLine(); // send the username to client handler 
            bufferedWriter.flush(); 

            try (Scanner scanner = new Scanner(System.in)) {
                while(socket.isConnected())
                {
                    try
                    {
                    
                    String messageToSend = scanner.nextLine();
                    bufferedWriter.write(messageToSend); 
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                   
                    if(messageToSend.equals("exit") || messageToSend.equals("leave"))
                    {
                        System.out.println("Leaving now.");  //end the program 
                        System.exit(0); 
                    }
                    if(messageToSend.equals("groups")) // show the groups available hard-coded
                    {
                        System.out.println("Groups available:\nGROUP 100\nGROUP 200\nGROUP 300\nGROUP 400\nGROUP 500\n");
                    }

                    }
                    catch(ArrayIndexOutOfBoundsException e) 
                    {
                        System.out.println("SERVER: Please write atleast 2 words.\n"); 
                    }
                }
            }
        }
        catch(IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage()
    {
        new Thread(new Runnable() {
            @Override 
            public void run()
            {
                String msgFromBoard; 

                while(socket.isConnected()) //while the socket is connected, this function will listen for messages and print them to the terminal
                {
                    try 
                    {
                        msgFromBoard = bufferedReader.readLine();
                        System.out.println(msgFromBoard);
                    }
                    catch(IOException e)
                    {
                        closeEverything(socket, bufferedReader, bufferedWriter); 
                    }
                }
            } //multithreaded, create a thread for each client
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        try
        {
            if(bufferedReader != null) // wrap up and close buffered reader and writer when finished
            {
                bufferedReader.close();
            }
            if(bufferedWriter != null)
            {
                bufferedWriter.close();
            }
            if(socket != null)
            {
                socket.close();   
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    } 
    
    public static void main(String[] args) throws UnknownHostException, IOException
    {
        try (Scanner scanner = new Scanner(System.in)) {

            //USER INTERACE 
            //SHOWS LIST OF COMMANDS, 
            //TAKES IN USERNAME AND FIRST FROUPNAME 
            //KEEPS GOING IN A WHILE LOOOP UNTIL CORRECT COMMAND IS ENTERED, AND IF WRONG PORT/GROUP IS USED, EXITS PROGRAM 

            
            System.out.println("\nWELCOME TO THE BULLETIN BOARD MESSAGING SYSTEM.\n"); 
            System.out.println("***LIST OF POSSIBLE COMMANDS***");
            System.out.println("<connect>   <port_number>\n<exit>"); 
            System.out.println("<message>  <messageID>\n<post> <subject> <message>\n <users>\n<join>\n<leave>\n\nOnce you enter a group: \n");
            System.out.println("<groupjoin>  <group_number>\n<groupleave>\n<groups>\n<grouppost>   <groupID>   <subject>   <message_content>\n<groupusers>  <groupID>\n<groupmessage>   <groupID>  <message_id>");
            System.out.println("\n\nEnter your username: ");
            String username = scanner.nextLine();
            System.out.println("\nConnect to the server first. Enter connect with port number (1234).");
            String s = scanner.nextLine();
            while(!s.startsWith("connect"))
            {
                System.out.println("CONNECT FIRST DUMMY\n");
                s = scanner.nextLine();
            }
            if (s.startsWith("connect"))
            {
                String[] splitMessage = s.split(" ", 2);            
                int port = Integer.parseInt(splitMessage[1]);
                Socket socket = new Socket("localhost", port);
                TCPClient client = new TCPClient(socket, username);
                System.out.println("\nSERVER: Connected to server!\n");
                System.out.println("Please use the join command before trying any of the others."); 
                connected = true; 
                client.listenForMessage();
                client.sendMessage();
                }
                

                 
        }
        
        catch (ConnectException e)
        {
            System.out.println("No connections on this port. Connection refused. Bye-bye!");
        }
        
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
}
