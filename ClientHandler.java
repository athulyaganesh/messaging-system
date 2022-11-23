/*CLIENT HANDLER HANDLES EACH INDIVIDUAL CLIENT AND ACTS AS THE MIDDLEMAN BETWEEN THE SERVER AND CLIENT */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap; 

//IF GROUPNUM == 0, SET TO PUBLIC VIEW

public class ClientHandler implements Runnable
{
  
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public static ArrayList<String> usernames = new ArrayList<>();
    public static ArrayList<Message> publicMessages = new ArrayList<>(); 

    private Socket socket; 
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter; 
    private String clientUsername;
    public static HashMap<String, GroupHandler> group = new HashMap<>(); 

    static {
        GroupHandler gh1 = new GroupHandler(); // HASHMAP TO STORE DATA FOR EACH GROUP 
        GroupHandler gh2 = new GroupHandler();
        GroupHandler gh3 = new GroupHandler();
        GroupHandler gh4 = new GroupHandler();
        GroupHandler gh5 = new GroupHandler();
        group.put("100", gh1); 
        group.put("200", gh2); 
        group.put("300", gh3); 
        group.put("400", gh4); 
        group.put("500", gh5); 
    }

  
    
    public ClientHandler() {}

    public ClientHandler(Socket socket) // CONSTRUCTOR
    {
        try
        { 
            this.socket = socket; // CONSTRUCTOR INITIALIZES OUR PRIVATE/PUBLIC VARIABLES LIKE SOCKET, THE BUFFERED READER, USERNAME for each client 
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //READ IN THE USERNAME FROM USER INPUT
            String s  = bufferedReader.readLine();
            this.clientUsername = s;
            
        }
        
        catch(IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter); 
        }
    }

    

    public void userHasEnteredChat(String groupNumber) // THIS FUNCTION ADDS THE USER TO THE GROUP AND DISPLAYS TO THE OTHER USERS THAT A NEW USER HAS JOINED THE CHAT 
    {
        if(groupNumber.equals("0")) // PUBLIC 
        {
            broadcastMessage("\nSERVER: " + this.clientUsername + " has entered the chat.\n\n", groupNumber);
            broadcastMessageToThis("\nSERVER: " + this.clientUsername + " has entered the chat.\n\n");
        }

        else 
        { //PRIVATE MESSAGE BOARD 
            if(!group.get(groupNumber).GroupUsers.contains(this.clientUsername)) // IF THE USERNAME IS NOT ALREADY CONTAINED IN THE GROUP, THEN ADD IT
            {
            broadcastMessage("\nSERVER: " + this.clientUsername + " has entered the chat.\n\n", groupNumber);
            broadcastMessageToThis("\nSERVER: " + this.clientUsername + " has entered the chat.\n\n"); 
            group.get(groupNumber).ClientsForGroup.add(this); 
            group.get(groupNumber).GroupUsers.add(this.clientUsername); 
            } 
            else
            {
                broadcastMessageToThis("SERVER: Cannot add user to GROUP .\n\n");                
            } 
        }
       
      
    }

    public void printMessageView(String GroupNumber) // WHEN A NEW USER ENTERS THE CHAT, THIS FUNCTION SHOWS THEM THE LAST 2 MESSAGES. IT DISPLAYS ANOTHER MESSAGE
    // IF NO MESSAGES HAVE BEEN TYPED, AND DISPLAYS ONLY ONE MESSAGE IF THERE IS ONLY ONE MESSAGE THAT HAS BEEN TYPED. CALLS ANOTHER FUNCTION postLastHowManyEverMessages() that actually broadcasts the message.  
    {
        if(GroupNumber.equals("0"))
        {
            if(Message.PublicMessages.size() == 0)
            {
                broadcastMessageToThis("\nSERVER: No messages available to view.\n\n");
            }
            else if(Message.PublicMessages.size() == 1)
            {
                postLastHowManyEverMessages(0,"0");
            }
            else // 2 or more than 2. 
            {
                postLastHowManyEverMessages(0,"0");
                postLastHowManyEverMessages(1,"0");
            }
        }
        else 
        {
            ArrayList<Message> m = new ArrayList<>(); 
            if(GroupNumber.equals("100"))
            {
                m = group.get(GroupNumber).MessageForGroup.Messages1; 
            }
            else if(GroupNumber.equals("200"))
            {
                m = group.get(GroupNumber).MessageForGroup.Messages2;
            }
            else if(GroupNumber.equals("300"))
            {
                m = group.get(GroupNumber).MessageForGroup.Messages3;
            }
            else if(GroupNumber.equals("400"))
            {
                m = group.get(GroupNumber).MessageForGroup.Messages4;
            }
            else if(GroupNumber.equals("500"))
            {
                m = group.get(GroupNumber).MessageForGroup.Messages5;
            }
            else{
                broadcastMessageToThis("SERVER: Invalid Group ID. \n");
            }

            if(m.size() == 0)
            {
                broadcastMessageToThis("\nSERVER: No messages available to view.\n\n");
            }
            else if(m.size()== 1)
            {
                postLastHowManyEverMessages(0, GroupNumber);
            }
            else // 2 or more than 2. 
            {
                postLastHowManyEverMessages(0, GroupNumber);
                postLastHowManyEverMessages(1, GroupNumber);
            }
        }
        
    }

    public void printUsernames(String GroupNumber) // THIS FUNCTION PRINTS OUT THE LIST OF ACTIVE USERS PRESENT IN A GROUP CURRENTLY
    {
        if(GroupNumber.equals("0"))
        {
            broadcastMessageToThis("\nList of Active Users in the Server: "); 

           
            if(!usernames.isEmpty())
            {
                for(String name : usernames)
                {
                    broadcastMessageToThis(name); 
                }
            }
            else 
            {
                broadcastMessageToThis("\nNo active users.\n"); 
            }
        }
        else 
        {
            broadcastMessageToThis("\nList of Active Users in group : "+GroupNumber); 
           
                if(!group.get(GroupNumber).GroupUsers.isEmpty())
                {
                    for(String name :group.get(GroupNumber).GroupUsers)
                    {
                        broadcastMessageToThis(name); 
                    }
                }
                else 
                {
                    broadcastMessageToThis("\nNo active users.");  // if the list is empty i.e no active users 
                }
            }
            
        broadcastMessageToThis("\n\n"); 
    }


    public void postLastHowManyEverMessages(int num, String GroupNumber) // when a new user joins, Prints out the most recent message.  
    // num == 0 MOST RECENT MESSAGE, num == 1 2nd last message AND SO ON  
    {
        Message m = new Message(); 
        Message message = new Message(); 
        if(GroupNumber.equals("0"))
        {
        
        message= m.PublicMessages.get(m.PublicMessages.size() - 1 - num);
        }
        else 
        {
            if(GroupNumber.equals("100"))
            {
                message= m.Messages1.get(m.Messages1.size() - 1 - num);
            }
            else if(GroupNumber.equals("200"))
            {
                message= m.Messages2.get(m.Messages2.size() - 1 - num);
            }
            else if(GroupNumber.equals("300"))
            {
                message= m.Messages3.get(m.Messages3.size() - 1 - num);
            }
            else if(GroupNumber.equals("400"))
            {
                message= m.Messages4.get(m.Messages4.size() - 1 - num); 
            }
            else if(GroupNumber.equals("500"))
            {
                message= m.Messages5.get(m.Messages5.size() - 1 - num);            
            }
            else 
            {
                broadcastMessageToThis("SERVER: Invalid Group ID.");
            }
        }

        broadcastMessageToThis("\n\n***MOST RECENT MESSAGE***");
        broadcastMessageToThis("FROM: " + message.user); 
        broadcastMessageToThis("MESSAGE ID: "+ message.ID);
        broadcastMessageToThis("POST DATE: " + message.postDate); 
        broadcastMessageToThis("SUBJECT: " + message.subjectMessage); 
        broadcastMessageToThis("TO SEE THE CONTENT OF THE MESSAGE, PLEASE CONTACT THE SERVER AND INPUT THE MESSAGE ID\n\n"); 
        
   }

    public void broadcastMessage(String messageToSend, String GroupNumber) // BROADCASTS MESSAGE TO ALL USERS EXCEPT USER WHO THE MESSAGE ORIGINATES FROM 
    {

        if(GroupNumber.equals("0"))
        {
            for(ClientHandler clientHandler: clientHandlers)
            {
                try
                {
                    if(!clientHandler.clientUsername.equals(clientUsername))
                    {
                        clientHandler.bufferedWriter.write(messageToSend); 
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush(); 
                    }
                }
                catch(IOException e)
                {
                    closeEverything(socket, bufferedReader, bufferedWriter); 
                }
            }
        }
        else 
        {
            for(ClientHandler clientHandler : group.get(GroupNumber).ClientsForGroup)
            {
                try
                {
                    if(!clientHandler.clientUsername.equals(this.clientUsername))
                    {
                        clientHandler.bufferedWriter.write(messageToSend); 
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush(); 
                    }
                }
                catch(IOException e)
                {
                    closeEverything(socket, bufferedReader, bufferedWriter); 
                }
            }
        }
        
    }
    @Override
    public void run() {
        String messageFromClient; 

        while(socket.isConnected())
        {
            try
            {
                messageFromClient = bufferedReader.readLine(); // READS FROM CLIENTS INPUT 

                //COMMAND IMPLEMENTATIONS
                
                
                if(messageFromClient.equals("users"))
                {
                    printUsernames("0"); 
                }
                if(messageFromClient.equals("leave"))
                {

                }
                else if(messageFromClient.startsWith("message"))
                {
                    Message m = new Message(); 
                    String[] splitMessage = messageFromClient.split(" ", 2);
                    String msgID = splitMessage[1]; 
                    
                    broadcastMessageToThis(m.returnContents(Integer.parseInt(msgID), "0"));
                }
                else if(messageFromClient.startsWith("post"))
                {
                    String[] splitMessage = messageFromClient.split(" ", 3);  
                    String head = splitMessage[1];
                    String body = splitMessage[2]; 
                    LocalDate date = LocalDate.now(); 
                    int id = (int)(Math.random()*(1000-0+1)+0); 
 
                    Message m = new Message(id, clientUsername, date.toString(), head, body, "0");
                    m.add(m,"0"); 
                   
                    Thread t = new Thread(m); 
                    t.start(); 

                    broadcastMessage("\n***NEW MESSAGE ALERT***\n", "0");  //PRINTS IT OUT TO ALL OTHER USERS IN THE GROUP 
                    broadcastMessage("FROM USERNAME: " + clientUsername, "0");
                    broadcastMessage("MESSAGE ID: "+ id, "0"); 
                    broadcastMessage("DATE: " + date.toString(), "0"); 
                    broadcastMessage("SUBJECT: " + head, "0"); 
                    broadcastMessage("TO VIEW THE CONTENT OF THE MESSAGE, CONTACT THE SERVER\n\n", "0"); 

                }
                   

                else if(messageFromClient.equals("join"))
                {

                        if(!usernames.contains(this.clientUsername))
                        {
                            clientHandlers.add(this);
                            usernames.add(clientUsername); 
                            broadcastMessageToThis("SERVER: Joined the Message Board!\n"); // allows user to join the group if they are not already a part 
                            userHasEnteredChat("0");
                            printUsernames("0");
                            printMessageView("0");
                        }
                        else 
                        {
                            broadcastMessageToThis("SERVER: Cannot add user.");
                        }
                        
                }


                else if(messageFromClient.startsWith("groupusers")) // DISPLAYS USERS IN A PARTICULAR GROUP 
                {
                    String[] splitMessage = messageFromClient.split(" ", 2);
                    String joinGroup = splitMessage[1]; 
                   
                    if(joinGroup.equals("100") || joinGroup.equals("200")|| joinGroup.equals("300") || joinGroup.equals("400") || joinGroup.equals("500"))
                    {
                        printUsernames(joinGroup); //CALLS FUNCTION PRINTUSERNAME TO DO THIS 
                    } 
                    else 
                    {
                        broadcastMessageToThis("SERVER: Invalid Group ID!"); // IF USER INPUTS ANY OTHER GROUP NUMBER 
                    }
                }



                else if (messageFromClient.startsWith("groupmessage")){ // SHOWS MESSAGE WHEN INPUTTED WITH A GROUPid AND MESSAGE ID
                    String[] splitMessage = messageFromClient.split(" ", 3);
                    String groupid = splitMessage[1]; 
                    int msgID = Integer.parseInt(splitMessage[2]);
                    boolean isIn = false; 

                    GroupHandler g = group.get(groupid); 
                    ArrayList<String> users = g.GroupUsers; 
                    for(String ch:users)
                    {
                        if(ch.equals(this.clientUsername)) //SEARCHES THROUGH ALL MESSAGES TO RETRIEVE THE CORRECT ONE 
                        {
                            broadcastMessageToThis(group.get(groupid).MessageForGroup.returnContents(msgID, groupid)+"\n\n");
                            isIn = true; 
                        }
                    }

                    if(!isIn) // IF USER IS NOT PART OF THE GROUP
                    {
                        broadcastMessageToThis("SERVER: You can only see messages when you are a part of the group.");
                    }
                }





                else if(messageFromClient.startsWith("grouppost")) // ALLOWS USER TO POST MESSAGES WITH A GROUPID, SUBJECT AND CONTENT 
                {
                    Boolean isIn = false; 
                    String[] splitMessage = messageFromClient.split(" ", 4);  
                    String groupID = splitMessage[1];          
                    String head = splitMessage[2];
                    String body = splitMessage[3]; 
                    LocalDate date = LocalDate.now(); 
                    int id = (int)(Math.random()*(1000-0+1)+0); 

                    for(ClientHandler ch: group.get(groupID).ClientsForGroup)
                    {
                        if(this==ch)
                        {
                            
                            Message m = new Message(id, clientUsername, date.toString(), head, body, groupID);

                            if(groupID.equals("100"))
                            {
                                group.get(groupID).MessageForGroup.Messages1.add(m);
                            }
                            else if(groupID.equals("200"))
                            {
                                group.get(groupID).MessageForGroup.Messages2.add(m);
                            }
                            else if(groupID.equals("300"))
                            {
                                group.get(groupID).MessageForGroup.Messages3.add(m);
                            }
                            else if(groupID.equals("400"))
                            {
                                group.get(groupID).MessageForGroup.Messages4.add(m);
                            }
                            else if(groupID.equals("500"))
                            {
                                group.get(groupID).MessageForGroup.Messages5.add(m);
                            }

                            // group.get(groupID).MessageForGroup.AllMessages.add(m);  //ADDS THE MESSAGE TO OUR GLOBAL HASH MAP 
                            isIn = true; 
                            Thread t = new Thread(m); 
                            t.start(); 

                            broadcastMessage("\n***NEW MESSAGE ALERT***\n", groupID);  //PRINTS IT OUT TO ALL OTHER USERS IN THE GROUP 
                            broadcastMessage("FROM USERNAME: " + clientUsername, groupID);
                            broadcastMessage("MESSAGE ID: "+ id, groupID); 
                            broadcastMessage("DATE: " + date.toString(), groupID); 
                            broadcastMessage("SUBJECT: " + head, groupID); 
                            broadcastMessage("TO VIEW THE CONTENT OF THE MESSAGE, CONTACT THE SERVER\n\n", groupID); 
                        }
                    }
                    if(!isIn)
                    {
                        broadcastMessageToThis("SERVER: You can't post messages where you are not a group member.");
                    }


                } 






                else if(messageFromClient.startsWith("connect")) //IMPLEMENTED IN MAIN 
                {
                    broadcastMessageToThis("Already connected to a port. Leave this group if you wish to join another");
                }


                else if(messageFromClient.startsWith("groupjoin"))
                { // IF A USER WANTS TO JOIN A NEW GROUP, ENTER GROUPJOIN WITH THE GROUP THEY WISH TO ENTER 
                    String[] splitMessage = messageFromClient.split(" ", 2);
                    String joinGroup = splitMessage[1]; 
                    if(joinGroup.equals("100") || joinGroup.equals("200")|| joinGroup.equals("300") || joinGroup.equals("400") || joinGroup.equals("500"))
                    {
                        
                        if(!group.get(joinGroup).GroupUsers.contains(this.clientUsername))
                        {
                            System.out.println("SERVER: Joined the group!\n"); // allows user to join the group if they are not already a part 
                            userHasEnteredChat(joinGroup);
                            printUsernames(joinGroup);
                            printMessageView(joinGroup);
                        }
                        else 
                        {
                            broadcastMessageToThis("SERVER: Already in group");
                        }
                    }
                    else 
                    {
                        broadcastMessageToThis("SERVER: Invalid Group ID");
                    }
                }




                else if(messageFromClient.startsWith("groupleave")) // allows user to leave a group 
                {
                    String[] splitMessage = messageFromClient.split(" ", 2);
                    String id = splitMessage[1];
                    //GroupHandler g = group.get(id); 
                    // ArrayList<String> users = g.GroupUsers;

                    if(group.get(id).GroupUsers.contains(this.clientUsername))
                    {
                        group.get(id).GroupUsers.remove(this.clientUsername); 
                        group.get(id).ClientsForGroup.remove(this);
                        broadcastMessageToThis("SERVER: You have left group "+id);
                    }
                    else 
                    {
                        broadcastMessageToThis("SERVER: Can't leave a group that you are not in.");
                    }

                     
                }




                else if(messageFromClient.startsWith("exit")) //implemented in main 
                {
                    broadcastMessageToThis("SERVER: Bye bye");
                }

                else if(messageFromClient.startsWith("groups"))
                {
                    // broadcastMessageToThis("LIST OF GROUPS:");  
                }

                //else 
                //{
                 //   broadcastMessageToThis("SERVER: Invalid command, please use the list of commands."); 
                // }
            }
            catch(IOException e)
            {
                closeEverything(socket, bufferedReader, bufferedWriter); 
                break; 
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                broadcastMessageToThis("SERVER: Invalid entry.");
            }
        }
        
    }

    
    public void broadcastMessageToThis(String messageToSend) // message sent to the user who initiated a command
    {
        try {
            this.bufferedWriter.write(messageToSend);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();

        } catch (IOException e) {
           closeEverything(socket, bufferedReader, bufferedWriter);
        }
        catch(NullPointerException e)
        {
            System.out.println("SERVER: MAKE SURE YOUR STRING IS NOT NULL.");
        } 
         
    }
    public void removeClientHandler(String groupNum) // allows to wrap and close up clients well if they are being removed. 
    {
        if(groupNum.equals("0"))
        {
            broadcastMessage("SERVER: " + clientUsername + " has left the chat. \n\n","0");
            clientHandlers.remove(this);
            usernames.remove(clientUsername);          
        }
        else 
        {
            ArrayList<String> nums= new ArrayList<>(); 
            nums.add("100");
            nums.add("200");
            nums.add("300"); 
            nums.add("400"); 
            nums.add("500"); 

            for(String g: nums)
            { 
                if(group.get(g).ClientsForGroup.contains(this)) // if list of client Handlers for this spefici group has the current client, remove it 
                {
                    broadcastMessage("SERVER: " + clientUsername + " has left the chat. \n\n", g);
                    group.get(g).ClientsForGroup.remove(this);
                    group.get(g).GroupUsers.remove(clientUsername);
                }
                
            }
        }   
                  
    }


    public void closeEverything(Socket socket, BufferedReader bufferedReaader, BufferedWriter bufferedWriter) //wrap and close everything up including buffered reader and writer once we are done writing and reading
    {
        removeClientHandler("0");
        try
        {
            if(bufferedReader != null)
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

}
