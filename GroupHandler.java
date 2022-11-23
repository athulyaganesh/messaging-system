import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

// HASHMAP STRUCTURE 

/*
 * GROUP NUMBER: GROUP HANDLER 
 * GROUP NUMBER IS A STRING 
 * GROUP HANDLER= EACH GROUP HAS A LIST OF CLIENTS (CLIENT HANDLER TYPE), A MESSAGE OBJECT (WHICH HAS THE GLOBAL ARRAY OF ALL MESSAGES), AND A STRING LIST OF ALL USERNAMES IN THE GROUP 
 */

public class GroupHandler implements Runnable { // takes care of each group
    public  ArrayList<ClientHandler> ClientsForGroup = new ArrayList<>();
    public  Message MessageForGroup = new Message(); 
    public  ArrayList<String> GroupUsers = new ArrayList<>();  // EACH GROUP NUMBER MAPS TO A GROUP HANDLER TYPE 

    // EACH GROUP HAS A LIST OF ITS USERS, A MESSAGE CLASS WHICH STORES EACH GROUPS MESSAGES, AND A LIST FOR EACH CLIENT IN THAT GROUP. 

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
    
}
